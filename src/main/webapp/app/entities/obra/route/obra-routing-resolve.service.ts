import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IObra } from '../obra.model';
import { ObraService } from '../service/obra.service';

const obraResolve = (route: ActivatedRouteSnapshot): Observable<null | IObra> => {
  const id = route.params.id;
  if (id) {
    return inject(ObraService)
      .find(id)
      .pipe(
        mergeMap((obra: HttpResponse<IObra>) => {
          if (obra.body) {
            return of(obra.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default obraResolve;
