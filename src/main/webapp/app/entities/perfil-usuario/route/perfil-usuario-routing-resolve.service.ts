import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPerfilUsuario } from '../perfil-usuario.model';
import { PerfilUsuarioService } from '../service/perfil-usuario.service';

const perfilUsuarioResolve = (route: ActivatedRouteSnapshot): Observable<null | IPerfilUsuario> => {
  const id = route.params.id;
  if (id) {
    return inject(PerfilUsuarioService)
      .find(id)
      .pipe(
        mergeMap((perfilUsuario: HttpResponse<IPerfilUsuario>) => {
          if (perfilUsuario.body) {
            return of(perfilUsuario.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default perfilUsuarioResolve;
