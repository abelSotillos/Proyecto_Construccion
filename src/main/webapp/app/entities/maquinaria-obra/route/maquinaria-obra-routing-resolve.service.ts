import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaquinariaObra } from '../maquinaria-obra.model';
import { MaquinariaObraService } from '../service/maquinaria-obra.service';

const maquinariaObraResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaquinariaObra> => {
  const id = route.params.id;
  if (id) {
    return inject(MaquinariaObraService)
      .find(id)
      .pipe(
        mergeMap((maquinariaObra: HttpResponse<IMaquinariaObra>) => {
          if (maquinariaObra.body) {
            return of(maquinariaObra.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default maquinariaObraResolve;
