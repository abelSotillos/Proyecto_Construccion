import { IMaquinaria, NewMaquinaria } from './maquinaria.model';

export const sampleWithRequiredData: IMaquinaria = {
  id: 11302,
};

export const sampleWithPartialData: IMaquinaria = {
  id: 27365,
};

export const sampleWithFullData: IMaquinaria = {
  id: 2974,
  modelo: 'because describe',
  estado: 'TALLER',
  precio: 3520,
};

export const sampleWithNewData: NewMaquinaria = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
