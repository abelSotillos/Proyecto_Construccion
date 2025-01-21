import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MaterialResolve from './route/material-routing-resolve.service';

const materialRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/material.component').then(m => m.MaterialComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/material-detail.component').then(m => m.MaterialDetailComponent),
    resolve: {
      material: MaterialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/material-update.component').then(m => m.MaterialUpdateComponent),
    resolve: {
      material: MaterialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/material-update.component').then(m => m.MaterialUpdateComponent),
    resolve: {
      material: MaterialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materialRoute;
