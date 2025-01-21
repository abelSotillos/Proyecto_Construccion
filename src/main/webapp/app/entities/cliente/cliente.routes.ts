import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ClienteResolve from './route/cliente-routing-resolve.service';

const clienteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cliente.component').then(m => m.ClienteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cliente-detail.component').then(m => m.ClienteDetailComponent),
    resolve: {
      cliente: ClienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cliente-update.component').then(m => m.ClienteUpdateComponent),
    resolve: {
      cliente: ClienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cliente-update.component').then(m => m.ClienteUpdateComponent),
    resolve: {
      cliente: ClienteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default clienteRoute;
