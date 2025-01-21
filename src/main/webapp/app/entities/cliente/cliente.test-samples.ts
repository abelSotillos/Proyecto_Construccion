import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 2897,
};

export const sampleWithPartialData: ICliente = {
  id: 15834,
  apellidos: 'recount',
  nif: 'coliseum',
  email: 'Marta.OlmosDiaz27@gmail.com',
};

export const sampleWithFullData: ICliente = {
  id: 17129,
  nombre: 'circumference',
  apellidos: 'categorise council',
  nif: 'blah reluctantly than',
  direccion: 'afore consequently',
  telefono: 'during',
  email: 'Benito71@gmail.com',
};

export const sampleWithNewData: NewCliente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
