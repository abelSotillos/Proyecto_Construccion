import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';

const clienteResolve = (route: ActivatedRouteSnapshot): Observable<null | ICliente> => {
  const id = route.params.id;
  if (id) {
    return inject(ClienteService)
      .find(id)
      .pipe(
        mergeMap((cliente: HttpResponse<ICliente>) => {
          if (cliente.body) {
            return of(cliente.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default clienteResolve;
