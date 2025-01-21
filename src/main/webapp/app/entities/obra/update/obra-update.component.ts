import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { EstadoObra } from 'app/entities/enumerations/estado-obra.model';
import { ObraService } from '../service/obra.service';
import { IObra } from '../obra.model';
import { ObraFormGroup, ObraFormService } from './obra-form.service';

@Component({
  selector: 'jhi-obra-update',
  templateUrl: './obra-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ObraUpdateComponent implements OnInit {
  isSaving = false;
  obra: IObra | null = null;
  estadoObraValues = Object.keys(EstadoObra);

  empresasSharedCollection: IEmpresa[] = [];
  clientesSharedCollection: ICliente[] = [];

  protected obraService = inject(ObraService);
  protected obraFormService = inject(ObraFormService);
  protected empresaService = inject(EmpresaService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ObraFormGroup = this.obraFormService.createObraFormGroup();

  compareEmpresa = (o1: IEmpresa | null, o2: IEmpresa | null): boolean => this.empresaService.compareEmpresa(o1, o2);

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ obra }) => {
      this.obra = obra;
      if (obra) {
        this.updateForm(obra);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const obra = this.obraFormService.getObra(this.editForm);
    if (obra.id !== null) {
      this.subscribeToSaveResponse(this.obraService.update(obra));
    } else {
      this.subscribeToSaveResponse(this.obraService.create(obra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IObra>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(obra: IObra): void {
    this.obra = obra;
    this.obraFormService.resetForm(this.editForm, obra);

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing<IEmpresa>(
      this.empresasSharedCollection,
      obra.empresa,
    );
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      obra.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing<IEmpresa>(empresas, this.obra?.empresa)))
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.obra?.cliente)))
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
