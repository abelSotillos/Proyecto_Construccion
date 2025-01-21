import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMaquinaria } from '../maquinaria.model';
import { MaquinariaService } from '../service/maquinaria.service';

@Component({
  templateUrl: './maquinaria-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MaquinariaDeleteDialogComponent {
  maquinaria?: IMaquinaria;

  protected maquinariaService = inject(MaquinariaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.maquinariaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
