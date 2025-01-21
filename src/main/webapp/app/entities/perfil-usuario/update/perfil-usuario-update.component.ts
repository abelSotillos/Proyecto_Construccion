import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { IPerfilUsuario } from '../perfil-usuario.model';
import { PerfilUsuarioService } from '../service/perfil-usuario.service';
import { PerfilUsuarioFormGroup, PerfilUsuarioFormService } from './perfil-usuario-form.service';

@Component({
  selector: 'jhi-perfil-usuario-update',
  templateUrl: './perfil-usuario-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PerfilUsuarioUpdateComponent implements OnInit {
  isSaving = false;
  perfilUsuario: IPerfilUsuario | null = null;

  empresasSharedCollection: IEmpresa[] = [];

  protected perfilUsuarioService = inject(PerfilUsuarioService);
  protected perfilUsuarioFormService = inject(PerfilUsuarioFormService);
  protected empresaService = inject(EmpresaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PerfilUsuarioFormGroup = this.perfilUsuarioFormService.createPerfilUsuarioFormGroup();

  compareEmpresa = (o1: IEmpresa | null, o2: IEmpresa | null): boolean => this.empresaService.compareEmpresa(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ perfilUsuario }) => {
      this.perfilUsuario = perfilUsuario;
      if (perfilUsuario) {
        this.updateForm(perfilUsuario);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const perfilUsuario = this.perfilUsuarioFormService.getPerfilUsuario(this.editForm);
    if (perfilUsuario.id !== null) {
      this.subscribeToSaveResponse(this.perfilUsuarioService.update(perfilUsuario));
    } else {
      this.subscribeToSaveResponse(this.perfilUsuarioService.create(perfilUsuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerfilUsuario>>): void {
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

  protected updateForm(perfilUsuario: IPerfilUsuario): void {
    this.perfilUsuario = perfilUsuario;
    this.perfilUsuarioFormService.resetForm(this.editForm, perfilUsuario);

    this.empresasSharedCollection = this.empresaService.addEmpresaToCollectionIfMissing<IEmpresa>(
      this.empresasSharedCollection,
      perfilUsuario.empresa,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.empresaService
      .query()
      .pipe(map((res: HttpResponse<IEmpresa[]>) => res.body ?? []))
      .pipe(
        map((empresas: IEmpresa[]) => this.empresaService.addEmpresaToCollectionIfMissing<IEmpresa>(empresas, this.perfilUsuario?.empresa)),
      )
      .subscribe((empresas: IEmpresa[]) => (this.empresasSharedCollection = empresas));
  }
}
