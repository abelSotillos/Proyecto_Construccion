import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'abelScApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'cliente',
    data: { pageTitle: 'abelScApp.cliente.home.title' },
    loadChildren: () => import('./cliente/cliente.routes'),
  },
  {
    path: 'empleado',
    data: { pageTitle: 'abelScApp.empleado.home.title' },
    loadChildren: () => import('./empleado/empleado.routes'),
  },
  {
    path: 'empleado-obra',
    data: { pageTitle: 'abelScApp.empleadoObra.home.title' },
    loadChildren: () => import('./empleado-obra/empleado-obra.routes'),
  },
  {
    path: 'empresa',
    data: { pageTitle: 'abelScApp.empresa.home.title' },
    loadChildren: () => import('./empresa/empresa.routes'),
  },
  {
    path: 'maquinaria',
    data: { pageTitle: 'abelScApp.maquinaria.home.title' },
    loadChildren: () => import('./maquinaria/maquinaria.routes'),
  },
  {
    path: 'maquinaria-obra',
    data: { pageTitle: 'abelScApp.maquinariaObra.home.title' },
    loadChildren: () => import('./maquinaria-obra/maquinaria-obra.routes'),
  },
  {
    path: 'material',
    data: { pageTitle: 'abelScApp.material.home.title' },
    loadChildren: () => import('./material/material.routes'),
  },
  {
    path: 'material-obra',
    data: { pageTitle: 'abelScApp.materialObra.home.title' },
    loadChildren: () => import('./material-obra/material-obra.routes'),
  },
  {
    path: 'obra',
    data: { pageTitle: 'abelScApp.obra.home.title' },
    loadChildren: () => import('./obra/obra.routes'),
  },
  {
    path: 'perfil-usuario',
    data: { pageTitle: 'abelScApp.perfilUsuario.home.title' },
    loadChildren: () => import('./perfil-usuario/perfil-usuario.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
