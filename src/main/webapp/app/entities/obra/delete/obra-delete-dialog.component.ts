import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IObra } from '../obra.model';
import { ObraService } from '../service/obra.service';

@Component({
  templateUrl: './obra-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ObraDeleteDialogComponent {
  obra?: IObra;

  protected obraService = inject(ObraService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.obraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
