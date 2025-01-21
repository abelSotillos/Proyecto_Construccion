import dayjs from 'dayjs/esm';

import { IEmpleado, NewEmpleado } from './empleado.model';

export const sampleWithRequiredData: IEmpleado = {
  id: 9977,
};

export const sampleWithPartialData: IEmpleado = {
  id: 29372,
  nombre: 'wee zowie',
  dni: 'jubilantly',
};

export const sampleWithFullData: IEmpleado = {
  id: 30067,
  nombre: 'sweetly metallic',
  dni: 'singing tinderbox',
  salario: 13602,
  fechaContratacion: dayjs('2025-01-21T02:00'),
};

export const sampleWithNewData: NewEmpleado = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
