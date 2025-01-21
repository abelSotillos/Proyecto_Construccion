import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MaterialObraResolve from './route/material-obra-routing-resolve.service';

const materialObraRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/material-obra.component').then(m => m.MaterialObraComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/material-obra-detail.component').then(m => m.MaterialObraDetailComponent),
    resolve: {
      materialObra: MaterialObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/material-obra-update.component').then(m => m.MaterialObraUpdateComponent),
    resolve: {
      materialObra: MaterialObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/material-obra-update.component').then(m => m.MaterialObraUpdateComponent),
    resolve: {
      materialObra: MaterialObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default materialObraRoute;
