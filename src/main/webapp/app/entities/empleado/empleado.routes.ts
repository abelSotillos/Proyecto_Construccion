import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmpleadoResolve from './route/empleado-routing-resolve.service';

const empleadoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/empleado.component').then(m => m.EmpleadoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/empleado-detail.component').then(m => m.EmpleadoDetailComponent),
    resolve: {
      empleado: EmpleadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/empleado-update.component').then(m => m.EmpleadoUpdateComponent),
    resolve: {
      empleado: EmpleadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/empleado-update.component').then(m => m.EmpleadoUpdateComponent),
    resolve: {
      empleado: EmpleadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default empleadoRoute;
