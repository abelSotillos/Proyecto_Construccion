import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';

const empleadoResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmpleado> => {
  const id = route.params.id;
  if (id) {
    return inject(EmpleadoService)
      .find(id)
      .pipe(
        mergeMap((empleado: HttpResponse<IEmpleado>) => {
          if (empleado.body) {
            return of(empleado.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default empleadoResolve;
