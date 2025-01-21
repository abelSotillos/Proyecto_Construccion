import { IEmpresa, NewEmpresa } from './empresa.model';

export const sampleWithRequiredData: IEmpresa = {
  id: 28120,
};

export const sampleWithPartialData: IEmpresa = {
  id: 47,
  nombre: 'incidentally bliss unlike',
  calle: 'mantua inasmuch',
  telefono: 'unusual direct concerning',
  provincia: 'delectable hence',
  poblacion: 'shyly',
};

export const sampleWithFullData: IEmpresa = {
  id: 11468,
  nombre: 'and governance',
  nif: 'deeply versus whispered',
  calle: 'glisten',
  telefono: 'gosh warming',
  provincia: 'where cease why',
  poblacion: 'pro brr',
};

export const sampleWithNewData: NewEmpresa = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
