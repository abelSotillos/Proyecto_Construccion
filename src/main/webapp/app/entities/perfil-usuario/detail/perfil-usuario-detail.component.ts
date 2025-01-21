import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPerfilUsuario } from '../perfil-usuario.model';

@Component({
  selector: 'jhi-perfil-usuario-detail',
  templateUrl: './perfil-usuario-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PerfilUsuarioDetailComponent {
  perfilUsuario = input<IPerfilUsuario | null>(null);

  previousState(): void {
    window.history.back();
  }
}
