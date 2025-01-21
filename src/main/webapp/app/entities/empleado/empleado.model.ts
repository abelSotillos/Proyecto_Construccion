import dayjs from 'dayjs/esm';
import { IEmpresa } from 'app/entities/empresa/empresa.model';

export interface IEmpleado {
  id: number;
  nombre?: string | null;
  dni?: string | null;
  salario?: number | null;
  fechaContratacion?: dayjs.Dayjs | null;
  empresa?: Pick<IEmpresa, 'id'> | null;
}

export type NewEmpleado = Omit<IEmpleado, 'id'> & { id: null };
