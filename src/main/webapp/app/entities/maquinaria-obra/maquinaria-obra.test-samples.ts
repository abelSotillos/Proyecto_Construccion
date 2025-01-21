import { IMaquinariaObra, NewMaquinariaObra } from './maquinaria-obra.model';

export const sampleWithRequiredData: IMaquinariaObra = {
  id: 15256,
};

export const sampleWithPartialData: IMaquinariaObra = {
  id: 22619,
  horas: 23674,
};

export const sampleWithFullData: IMaquinariaObra = {
  id: 32737,
  horas: 27148,
};

export const sampleWithNewData: NewMaquinariaObra = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
