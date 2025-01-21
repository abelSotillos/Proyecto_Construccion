import { IEmpleadoObra, NewEmpleadoObra } from './empleado-obra.model';

export const sampleWithRequiredData: IEmpleadoObra = {
  id: 15046,
};

export const sampleWithPartialData: IEmpleadoObra = {
  id: 13812,
};

export const sampleWithFullData: IEmpleadoObra = {
  id: 5637,
  horas: 20368,
};

export const sampleWithNewData: NewEmpleadoObra = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
