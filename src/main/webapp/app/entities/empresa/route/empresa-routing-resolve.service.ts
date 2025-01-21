import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmpresa } from '../empresa.model';
import { EmpresaService } from '../service/empresa.service';

const empresaResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmpresa> => {
  const id = route.params.id;
  if (id) {
    return inject(EmpresaService)
      .find(id)
      .pipe(
        mergeMap((empresa: HttpResponse<IEmpresa>) => {
          if (empresa.body) {
            return of(empresa.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default empresaResolve;
