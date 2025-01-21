import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { EstadoMaquinaria } from 'app/entities/enumerations/estado-maquinaria.model';
import { MaquinariaService } from '../service/maquinaria.service';
import { IMaquinaria } from '../maquinaria.model';
import { MaquinariaFormGroup, MaquinariaFormService } from './maquinaria-form.service';

@Component({
  selector: 'jhi-maquinaria-update',
  templateUrl: './maquinaria-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaquinariaUpdateComponent implements OnInit {
  isSaving = false;
  maquinaria: IMaquinaria | null = null;
  estadoMaquinariaValues = Object.keys(EstadoMaquinaria);

  empresasSharedCollection: IEmpresa[] = [];

  protected maquinariaService = inject(MaquinariaService);
  protected maquinariaFormService = inject(MaquinariaFormService);
  protected empresaService = inject(EmpresaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaquinariaFormGroup = this.maquinariaFormService.createMaquinariaFormGroup();

  compareEmpresa = (o1: IEmpresa | null, o2: IEmpresa | null): boolean => this.empresaService.compareEmpresa(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maquinaria }) => {
      this.maquinaria = maquinaria;
      if (maquinaria) {
        this.updateForm(maquinaria);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const maquinaria = this.maquinariaFormService.getMaquinaria(this.editForm);
    if (maquinaria.id !== null) {
      this.subscribeToSaveResponse(this.maquinariaService.update(maquinaria));
    } else {
      this.subscribeToSaveResponse(this.maquinariaService.create(maquinaria));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaquinaria>>): void {
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

  protected updateForm(maquinaria: IMaquinaria): void {
    this.maquinaria = maquinaria;
    this.maquinariaFormService.resetForm(this.editForm, maquinaria);

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing<IEmpresa>(
      this.empresasSharedCollection,
      maquinaria.empresa,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing<IEmpresa>(empresas, this.maquinaria?.empresa)),
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));
  }
}
