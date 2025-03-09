import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';
import { EmpleadoFormGroup, EmpleadoFormService } from './empleado-form.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-empleado-update',
  templateUrl: './empleado-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmpleadoUpdateComponent implements OnInit {
  currentProfile = inject(AccountService).trackCurrentProfile();
  currentAccount = inject(AccountService).trackCurrentAccount();
  isSaving = false;
  empleado: IEmpleado | null = null;

  empresasSharedCollection: IEmpresa[] = [];

  protected empleadoService = inject(EmpleadoService);
  protected empleadoFormService = inject(EmpleadoFormService);
  protected empresaService = inject(EmpresaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmpleadoFormGroup = this.empleadoFormService.createEmpleadoFormGroup();

  compareEmpresa = (o1: IEmpresa | null, o2: IEmpresa | null): boolean => this.empresaService.compareEmpresa(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleado }) => {
      this.empleado = empleado;
      if (empleado) {
        this.updateForm(empleado);
      }

      this.loadRelationshipsOptions();
    });
    if (this.currentAccount()?.authorities.includes('ROLE_USER')) {
      this.editForm.patchValue({ empresa: this.currentProfile()?.empresa });
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleado = this.empleadoFormService.getEmpleado(this.editForm);
    if (empleado.id !== null) {
      this.subscribeToSaveResponse(this.empleadoService.update(empleado));
    } else {
      this.subscribeToSaveResponse(this.empleadoService.create(empleado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleado>>): void {
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

  protected updateForm(empleado: IEmpleado): void {
    this.empleado = empleado;
    this.empleadoFormService.resetForm(this.editForm, empleado);

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing<IEmpresa>(
      this.empresasSharedCollection,
      empleado.empresa,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing<IEmpresa>(empresas, this.empleado?.empresa)))
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));
  }
}
