import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMaquinariaObra } from '../maquinaria-obra.model';
import { MaquinariaObraService } from '../service/maquinaria-obra.service';

@Component({
  templateUrl: './maquinaria-obra-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MaquinariaObraDeleteDialogComponent {
  maquinariaObra?: IMaquinariaObra;

  protected maquinariaObraService = inject(MaquinariaObraService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.maquinariaObraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
