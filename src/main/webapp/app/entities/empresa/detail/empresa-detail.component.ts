import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEmpresa } from '../empresa.model';

@Component({
  selector: 'jhi-empresa-detail',
  templateUrl: './empresa-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EmpresaDetailComponent {
  empresa = input<IEmpresa | null>(null);

  previousState(): void {
    window.history.back();
  }
}
