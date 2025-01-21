import { IMaterial, NewMaterial } from './material.model';

export const sampleWithRequiredData: IMaterial = {
  id: 28810,
};

export const sampleWithPartialData: IMaterial = {
  id: 3517,
  descripcion: 'than',
};

export const sampleWithFullData: IMaterial = {
  id: 24846,
  nombre: 'ready whether',
  descripcion: 'sequester before neat',
  precio: 27331,
  stock: 1618,
  unidadMedida: 'PIEZA',
};

export const sampleWithNewData: NewMaterial = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
