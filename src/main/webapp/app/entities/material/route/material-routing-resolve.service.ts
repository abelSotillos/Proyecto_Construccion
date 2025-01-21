import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaterial } from '../material.model';
import { MaterialService } from '../service/material.service';

const materialResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaterial> => {
  const id = route.params.id;
  if (id) {
    return inject(MaterialService)
      .find(id)
      .pipe(
        mergeMap((material: HttpResponse<IMaterial>) => {
          if (material.body) {
            return of(material.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default materialResolve;
