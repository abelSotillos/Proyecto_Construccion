import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MaquinariaResolve from './route/maquinaria-routing-resolve.service';

const maquinariaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/maquinaria.component').then(m => m.MaquinariaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/maquinaria-detail.component').then(m => m.MaquinariaDetailComponent),
    resolve: {
      maquinaria: MaquinariaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/maquinaria-update.component').then(m => m.MaquinariaUpdateComponent),
    resolve: {
      maquinaria: MaquinariaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/maquinaria-update.component').then(m => m.MaquinariaUpdateComponent),
    resolve: {
      maquinaria: MaquinariaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default maquinariaRoute;
