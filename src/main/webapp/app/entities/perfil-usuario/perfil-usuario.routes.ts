import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PerfilUsuarioResolve from './route/perfil-usuario-routing-resolve.service';

const perfilUsuarioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/perfil-usuario.component').then(m => m.PerfilUsuarioComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/perfil-usuario-detail.component').then(m => m.PerfilUsuarioDetailComponent),
    resolve: {
      perfilUsuario: PerfilUsuarioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/perfil-usuario-update.component').then(m => m.PerfilUsuarioUpdateComponent),
    resolve: {
      perfilUsuario: PerfilUsuarioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/perfil-usuario-update.component').then(m => m.PerfilUsuarioUpdateComponent),
    resolve: {
      perfilUsuario: PerfilUsuarioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default perfilUsuarioRoute;
