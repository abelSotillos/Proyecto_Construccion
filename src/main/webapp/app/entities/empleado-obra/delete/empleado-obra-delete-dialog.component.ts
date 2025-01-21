import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmpleadoObra } from '../empleado-obra.model';
import { EmpleadoObraService } from '../service/empleado-obra.service';

@Component({
  templateUrl: './empleado-obra-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmpleadoObraDeleteDialogComponent {
  empleadoObra?: IEmpleadoObra;

  protected empleadoObraService = inject(EmpleadoObraService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.empleadoObraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
