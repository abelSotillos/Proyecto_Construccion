import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICliente } from '../cliente.model';

@Component({
  selector: 'jhi-cliente-detail',
  templateUrl: './cliente-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ClienteDetailComponent {
  cliente = input<ICliente | null>(null);

  previousState(): void {
    window.history.back();
  }
}
