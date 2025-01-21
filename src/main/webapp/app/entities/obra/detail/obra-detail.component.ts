import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IObra } from '../obra.model';

@Component({
  selector: 'jhi-obra-detail',
  templateUrl: './obra-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class ObraDetailComponent {
  obra = input<IObra | null>(null);

  previousState(): void {
    window.history.back();
  }
}
