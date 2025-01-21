import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPerfilUsuario } from '../perfil-usuario.model';
import { PerfilUsuarioService } from '../service/perfil-usuario.service';

@Component({
  templateUrl: './perfil-usuario-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PerfilUsuarioDeleteDialogComponent {
  perfilUsuario?: IPerfilUsuario;

  protected perfilUsuarioService = inject(PerfilUsuarioService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.perfilUsuarioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
