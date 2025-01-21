import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEmpleadoObra } from '../empleado-obra.model';

@Component({
  selector: 'jhi-empleado-obra-detail',
  templateUrl: './empleado-obra-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EmpleadoObraDetailComponent {
  empleadoObra = input<IEmpleadoObra | null>(null);

  previousState(): void {
    window.history.back();
  }
}
