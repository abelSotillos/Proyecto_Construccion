import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IObra } from 'app/entities/obra/obra.model';
import { ObraService } from 'app/entities/obra/service/obra.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { EmpleadoObraService } from '../service/empleado-obra.service';
import { IEmpleadoObra } from '../empleado-obra.model';
import { EmpleadoObraFormGroup, EmpleadoObraFormService } from './empleado-obra-form.service';

@Component({
  selector: 'jhi-empleado-obra-update',
  templateUrl: './empleado-obra-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmpleadoObraUpdateComponent implements OnInit {
  isSaving = false;
  empleadoObra: IEmpleadoObra | null = null;

  obrasSharedCollection: IObra[] = [];
  empleadosSharedCollection: IEmpleado[] = [];

  protected empleadoObraService = inject(EmpleadoObraService);
  protected empleadoObraFormService = inject(EmpleadoObraFormService);
  protected obraService = inject(ObraService);
  protected empleadoService = inject(EmpleadoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmpleadoObraFormGroup = this.empleadoObraFormService.createEmpleadoObraFormGroup();

  compareObra = (o1: IObra | null, o2: IObra | null): boolean => this.obraService.compareObra(o1, o2);

  compareEmpleado = (o1: IEmpleado | null, o2: IEmpleado | null): boolean => this.empleadoService.compareEmpleado(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleadoObra }) => {
      this.empleadoObra = empleadoObra;
      if (empleadoObra) {
        this.updateForm(empleadoObra);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleadoObra = this.empleadoObraFormService.getEmpleadoObra(this.editForm);
    if (empleadoObra.id !== null) {
      this.subscribeToSaveResponse(this.empleadoObraService.update(empleadoObra));
    } else {
      this.subscribeToSaveResponse(this.empleadoObraService.create(empleadoObra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleadoObra>>): void {
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

  protected updateForm(empleadoObra: IEmpleadoObra): void {
    this.empleadoObra = empleadoObra;
    this.empleadoObraFormService.resetForm(this.editForm, empleadoObra);

    this.obrasSharedCollection = this.obraService.addObraToCollectionIfMissing<IObra>(this.obrasSharedCollection, empleadoObra.obra);
    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(
      this.empleadosSharedCollection,
      empleadoObra.empleado,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.obraService
      .query()
      .pipe(map((res: HttpResponse<IObra[]>) => res.body ?? []))
      .pipe(map((obras: IObra[]) => this.obraService.addObraToCollectionIfMissing<IObra>(obras, this.empleadoObra?.obra)))
      .subscribe((obras: IObra[]) => (this.obrasSharedCollection = obras));

    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(empleados, this.empleadoObra?.empleado),
        ),
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));
  }
}
