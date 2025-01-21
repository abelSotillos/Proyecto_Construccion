import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpresa } from '../empresa.model';
import { EmpresaService } from '../service/empresa.service';
import { EmpresaFormGroup, EmpresaFormService } from './empresa-form.service';

@Component({
  selector: 'jhi-empresa-update',
  templateUrl: './empresa-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmpresaUpdateComponent implements OnInit {
  isSaving = false;
  empresa: IEmpresa | null = null;

  protected empresaService = inject(EmpresaService);
  protected empresaFormService = inject(EmpresaFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmpresaFormGroup = this.empresaFormService.createEmpresaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empresa }) => {
      this.empresa = empresa;
      if (empresa) {
        this.updateForm(empresa);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empresa = this.empresaFormService.getEmpresa(this.editForm);
    if (empresa.id !== null) {
      this.subscribeToSaveResponse(this.empresaService.update(empresa));
    } else {
      this.subscribeToSaveResponse(this.empresaService.create(empresa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpresa>>): void {
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

  protected updateForm(empresa: IEmpresa): void {
    this.empresa = empresa;
    this.empresaFormService.resetForm(this.editForm, empresa);
  }
}
