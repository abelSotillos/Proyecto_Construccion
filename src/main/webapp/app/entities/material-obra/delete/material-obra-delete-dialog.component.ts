import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMaterialObra } from '../material-obra.model';
import { MaterialObraService } from '../service/material-obra.service';

@Component({
  templateUrl: './material-obra-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MaterialObraDeleteDialogComponent {
  materialObra?: IMaterialObra;

  protected materialObraService = inject(MaterialObraService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materialObraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
