import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMaquinaria } from '../maquinaria.model';

@Component({
  selector: 'jhi-maquinaria-detail',
  templateUrl: './maquinaria-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MaquinariaDetailComponent {
  maquinaria = input<IMaquinaria | null>(null);

  previousState(): void {
    window.history.back();
  }
}
