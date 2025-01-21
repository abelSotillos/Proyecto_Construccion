import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IObra } from 'app/entities/obra/obra.model';
import { ObraService } from 'app/entities/obra/service/obra.service';
import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { MaterialObraService } from '../service/material-obra.service';
import { IMaterialObra } from '../material-obra.model';
import { MaterialObraFormGroup, MaterialObraFormService } from './material-obra-form.service';

@Component({
  selector: 'jhi-material-obra-update',
  templateUrl: './material-obra-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaterialObraUpdateComponent implements OnInit {
  isSaving = false;
  materialObra: IMaterialObra | null = null;

  obrasSharedCollection: IObra[] = [];
  materialsSharedCollection: IMaterial[] = [];

  protected materialObraService = inject(MaterialObraService);
  protected materialObraFormService = inject(MaterialObraFormService);
  protected obraService = inject(ObraService);
  protected materialService = inject(MaterialService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaterialObraFormGroup = this.materialObraFormService.createMaterialObraFormGroup();

  compareObra = (o1: IObra | null, o2: IObra | null): boolean => this.obraService.compareObra(o1, o2);

  compareMaterial = (o1: IMaterial | null, o2: IMaterial | null): boolean => this.materialService.compareMaterial(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materialObra }) => {
      this.materialObra = materialObra;
      if (materialObra) {
        this.updateForm(materialObra);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materialObra = this.materialObraFormService.getMaterialObra(this.editForm);
    if (materialObra.id !== null) {
      this.subscribeToSaveResponse(this.materialObraService.update(materialObra));
    } else {
      this.subscribeToSaveResponse(this.materialObraService.create(materialObra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialObra>>): void {
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

  protected updateForm(materialObra: IMaterialObra): void {
    this.materialObra = materialObra;
    this.materialObraFormService.resetForm(this.editForm, materialObra);

    this.obrasSharedCollection = this.obraService.addObraToCollectionIfMissing<IObra>(this.obrasSharedCollection, materialObra.obra);
    this.materialsSharedCollection = this.materialService.addMaterialToCollectionIfMissing<IMaterial>(
      this.materialsSharedCollection,
      materialObra.material,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.obraService
      .query()
      .pipe(map((res: HttpResponse<IObra[]>) => res.body ?? []))
      .pipe(map((obras: IObra[]) => this.obraService.addObraToCollectionIfMissing<IObra>(obras, this.materialObra?.obra)))
      .subscribe((obras: IObra[]) => (this.obrasSharedCollection = obras));

    this.materialService
      .query()
      .pipe(map((res: HttpResponse<IMaterial[]>) => res.body ?? []))
      .pipe(
        map((materials: IMaterial[]) =>
          this.materialService.addMaterialToCollectionIfMissing<IMaterial>(materials, this.materialObra?.material),
        ),
      )
      .subscribe((materials: IMaterial[]) => (this.materialsSharedCollection = materials));
  }
}
