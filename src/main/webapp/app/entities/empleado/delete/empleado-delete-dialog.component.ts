import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';

@Component({
  templateUrl: './empleado-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmpleadoDeleteDialogComponent {
  empleado?: IEmpleado;

  protected empleadoService = inject(EmpleadoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.empleadoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
