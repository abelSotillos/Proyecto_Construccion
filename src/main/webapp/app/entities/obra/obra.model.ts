import dayjs from 'dayjs/esm';
import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { EstadoObra } from 'app/entities/enumerations/estado-obra.model';

export interface IObra {
  id: number;
  nombre?: string | null;
  direccion?: string | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  coste?: number | null;
  estado?: keyof typeof EstadoObra | null;
  costePagado?: number | null;
  empresa?: Pick<IEmpresa, 'id'> | null;
  cliente?: Pick<ICliente, 'id'> | null;
}

export type NewObra = Omit<IObra, 'id'> & { id: null };
