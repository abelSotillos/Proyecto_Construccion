import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmpleadoObraResolve from './route/empleado-obra-routing-resolve.service';

const empleadoObraRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/empleado-obra.component').then(m => m.EmpleadoObraComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/empleado-obra-detail.component').then(m => m.EmpleadoObraDetailComponent),
    resolve: {
      empleadoObra: EmpleadoObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/empleado-obra-update.component').then(m => m.EmpleadoObraUpdateComponent),
    resolve: {
      empleadoObra: EmpleadoObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/empleado-obra-update.component').then(m => m.EmpleadoObraUpdateComponent),
    resolve: {
      empleadoObra: EmpleadoObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default empleadoObraRoute;
