import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMaquinariaObra } from '../maquinaria-obra.model';

@Component({
  selector: 'jhi-maquinaria-obra-detail',
  templateUrl: './maquinaria-obra-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MaquinariaObraDetailComponent {
  maquinariaObra = input<IMaquinariaObra | null>(null);

  previousState(): void {
    window.history.back();
  }
}
