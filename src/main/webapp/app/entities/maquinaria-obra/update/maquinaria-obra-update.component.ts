import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IObra } from 'app/entities/obra/obra.model';
import { ObraService } from 'app/entities/obra/service/obra.service';
import { IMaquinaria } from 'app/entities/maquinaria/maquinaria.model';
import { MaquinariaService } from 'app/entities/maquinaria/service/maquinaria.service';
import { MaquinariaObraService } from '../service/maquinaria-obra.service';
import { IMaquinariaObra } from '../maquinaria-obra.model';
import { MaquinariaObraFormGroup, MaquinariaObraFormService } from './maquinaria-obra-form.service';

@Component({
  selector: 'jhi-maquinaria-obra-update',
  templateUrl: './maquinaria-obra-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaquinariaObraUpdateComponent implements OnInit {
  isSaving = false;
  maquinariaObra: IMaquinariaObra | null = null;

  obrasSharedCollection: IObra[] = [];
  maquinariasSharedCollection: IMaquinaria[] = [];

  protected maquinariaObraService = inject(MaquinariaObraService);
  protected maquinariaObraFormService = inject(MaquinariaObraFormService);
  protected obraService = inject(ObraService);
  protected maquinariaService = inject(MaquinariaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaquinariaObraFormGroup = this.maquinariaObraFormService.createMaquinariaObraFormGroup();

  compareObra = (o1: IObra | null, o2: IObra | null): boolean => this.obraService.compareObra(o1, o2);

  compareMaquinaria = (o1: IMaquinaria | null, o2: IMaquinaria | null): boolean => this.maquinariaService.compareMaquinaria(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maquinariaObra }) => {
      this.maquinariaObra = maquinariaObra;
      if (maquinariaObra) {
        this.updateForm(maquinariaObra);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const maquinariaObra = this.maquinariaObraFormService.getMaquinariaObra(this.editForm);
    if (maquinariaObra.id !== null) {
      this.subscribeToSaveResponse(this.maquinariaObraService.update(maquinariaObra));
    } else {
      this.subscribeToSaveResponse(this.maquinariaObraService.create(maquinariaObra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaquinariaObra>>): void {
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

  protected updateForm(maquinariaObra: IMaquinariaObra): void {
    this.maquinariaObra = maquinariaObra;
    this.maquinariaObraFormService.resetForm(this.editForm, maquinariaObra);

    this.obrasSharedCollection = this.obraService.addObraToCollectionIfMissing<IObra>(this.obrasSharedCollection, maquinariaObra.obra);
    this.maquinariasSharedCollection = this.maquinariaService.addMaquinariaToCollectionIfMissing<IMaquinaria>(
      this.maquinariasSharedCollection,
      maquinariaObra.maquinaria,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.obraService
      .query()
      .pipe(map((res: HttpResponse<IObra[]>) => res.body ?? []))
      .pipe(map((obras: IObra[]) => this.obraService.addObraToCollectionIfMissing<IObra>(obras, this.maquinariaObra?.obra)))
      .subscribe((obras: IObra[]) => (this.obrasSharedCollection = obras));

    this.maquinariaService
      .query()
      .pipe(map((res: HttpResponse<IMaquinaria[]>) => res.body ?? []))
      .pipe(
        map((maquinarias: IMaquinaria[]) =>
          this.maquinariaService.addMaquinariaToCollectionIfMissing<IMaquinaria>(maquinarias, this.maquinariaObra?.maquinaria),
        ),
      )
      .subscribe((maquinarias: IMaquinaria[]) => (this.maquinariasSharedCollection = maquinarias));
  }
}
