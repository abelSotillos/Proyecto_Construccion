import { IObra } from 'app/entities/obra/obra.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';

export interface IEmpleadoObra {
  id: number;
  horas?: number | null;
  obra?: Pick<IObra, 'id'> | null;
  empleado?: Pick<IEmpleado, 'id'> | null;
}

export type NewEmpleadoObra = Omit<IEmpleadoObra, 'id'> & { id: null };
