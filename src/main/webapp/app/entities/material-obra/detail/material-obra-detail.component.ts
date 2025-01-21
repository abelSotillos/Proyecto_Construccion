import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMaterialObra } from '../material-obra.model';

@Component({
  selector: 'jhi-material-obra-detail',
  templateUrl: './material-obra-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MaterialObraDetailComponent {
  materialObra = input<IMaterialObra | null>(null);

  previousState(): void {
    window.history.back();
  }
}
