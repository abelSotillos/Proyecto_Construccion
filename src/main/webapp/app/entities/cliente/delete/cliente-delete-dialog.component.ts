import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';

@Component({
  templateUrl: './cliente-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ClienteDeleteDialogComponent {
  cliente?: ICliente;

  protected clienteService = inject(ClienteService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.clienteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
