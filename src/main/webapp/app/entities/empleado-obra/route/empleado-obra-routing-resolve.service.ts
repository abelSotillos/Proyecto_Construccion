import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmpleadoObra } from '../empleado-obra.model';
import { EmpleadoObraService } from '../service/empleado-obra.service';

const empleadoObraResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmpleadoObra> => {
  const id = route.params.id;
  if (id) {
    return inject(EmpleadoObraService)
      .find(id)
      .pipe(
        mergeMap((empleadoObra: HttpResponse<IEmpleadoObra>) => {
          if (empleadoObra.body) {
            return of(empleadoObra.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default empleadoObraResolve;
