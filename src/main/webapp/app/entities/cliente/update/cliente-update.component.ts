import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { ClienteFormGroup, ClienteFormService } from './cliente-form.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClienteUpdateComponent implements OnInit {
  currentProfile = inject(AccountService).trackCurrentProfile();
  currentAccount = inject(AccountService).trackCurrentAccount();
  isSaving = false;
  cliente: ICliente | null = null;

  protected clienteService = inject(ClienteService);
  protected clienteFormService = inject(ClienteFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClienteFormGroup = this.clienteFormService.createClienteFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.cliente = cliente;
      if (cliente) {
        this.updateForm(cliente);
      }
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
    const cliente = this.clienteFormService.getCliente(this.editForm);
    if (cliente.id !== null) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.cliente = cliente;
    this.clienteFormService.resetForm(this.editForm, cliente);
  }
}
