import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaterialObra } from '../material-obra.model';
import { MaterialObraService } from '../service/material-obra.service';

const materialObraResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaterialObra> => {
  const id = route.params.id;
  if (id) {
    return inject(MaterialObraService)
      .find(id)
      .pipe(
        mergeMap((materialObra: HttpResponse<IMaterialObra>) => {
          if (materialObra.body) {
            return of(materialObra.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default materialObraResolve;
