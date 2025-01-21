import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ObraResolve from './route/obra-routing-resolve.service';

const obraRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/obra.component').then(m => m.ObraComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/obra-detail.component').then(m => m.ObraDetailComponent),
    resolve: {
      obra: ObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/obra-update.component').then(m => m.ObraUpdateComponent),
    resolve: {
      obra: ObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/obra-update.component').then(m => m.ObraUpdateComponent),
    resolve: {
      obra: ObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default obraRoute;
