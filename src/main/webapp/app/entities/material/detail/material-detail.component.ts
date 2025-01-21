import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMaterial } from '../material.model';

@Component({
  selector: 'jhi-material-detail',
  templateUrl: './material-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MaterialDetailComponent {
  material = input<IMaterial | null>(null);

  previousState(): void {
    window.history.back();
  }
}
