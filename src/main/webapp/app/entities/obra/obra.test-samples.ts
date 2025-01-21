import dayjs from 'dayjs/esm';

import { IObra, NewObra } from './obra.model';

export const sampleWithRequiredData: IObra = {
  id: 6585,
};

export const sampleWithPartialData: IObra = {
  id: 5076,
  fechaInicio: dayjs('2025-01-21T10:24'),
  estado: 'INICIADO',
  costePagado: 16705,
};

export const sampleWithFullData: IObra = {
  id: 14293,
  nombre: 'unless radiant',
  direccion: 'amongst train',
  fechaInicio: dayjs('2025-01-21T21:20'),
  fechaFin: dayjs('2025-01-21T19:44'),
  coste: 5799,
  estado: 'INICIADO',
  costePagado: 26565,
};

export const sampleWithNewData: NewObra = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
