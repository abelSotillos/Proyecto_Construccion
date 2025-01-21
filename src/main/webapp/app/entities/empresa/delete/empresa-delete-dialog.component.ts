import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmpresa } from '../empresa.model';
import { EmpresaService } from '../service/empresa.service';

@Component({
  templateUrl: './empresa-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmpresaDeleteDialogComponent {
  empresa?: IEmpresa;

  protected empresaService = inject(EmpresaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.empresaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
