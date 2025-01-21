import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaquinaria } from '../maquinaria.model';
import { MaquinariaService } from '../service/maquinaria.service';

const maquinariaResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaquinaria> => {
  const id = route.params.id;
  if (id) {
    return inject(MaquinariaService)
      .find(id)
      .pipe(
        mergeMap((maquinaria: HttpResponse<IMaquinaria>) => {
          if (maquinaria.body) {
            return of(maquinaria.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default maquinariaResolve;
