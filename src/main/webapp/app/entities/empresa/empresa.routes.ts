import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmpresaResolve from './route/empresa-routing-resolve.service';

const empresaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/empresa.component').then(m => m.EmpresaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/empresa-detail.component').then(m => m.EmpresaDetailComponent),
    resolve: {
      empresa: EmpresaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/empresa-update.component').then(m => m.EmpresaUpdateComponent),
    resolve: {
      empresa: EmpresaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/empresa-update.component').then(m => m.EmpresaUpdateComponent),
    resolve: {
      empresa: EmpresaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default empresaRoute;
