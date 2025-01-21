import { IMaterialObra, NewMaterialObra } from './material-obra.model';

export const sampleWithRequiredData: IMaterialObra = {
  id: 27668,
};

export const sampleWithPartialData: IMaterialObra = {
  id: 22364,
  cantidad: 29802,
};

export const sampleWithFullData: IMaterialObra = {
  id: 23123,
  cantidad: 32620,
};

export const sampleWithNewData: NewMaterialObra = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
