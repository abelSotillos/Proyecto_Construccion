import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MaquinariaObraResolve from './route/maquinaria-obra-routing-resolve.service';

const maquinariaObraRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/maquinaria-obra.component').then(m => m.MaquinariaObraComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/maquinaria-obra-detail.component').then(m => m.MaquinariaObraDetailComponent),
    resolve: {
      maquinariaObra: MaquinariaObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/maquinaria-obra-update.component').then(m => m.MaquinariaObraUpdateComponent),
    resolve: {
      maquinariaObra: MaquinariaObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/maquinaria-obra-update.component').then(m => m.MaquinariaObraUpdateComponent),
    resolve: {
      maquinariaObra: MaquinariaObraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default maquinariaObraRoute;
