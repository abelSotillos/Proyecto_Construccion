import { IEmpresa } from '../empresa/empresa.model';

export interface ICliente {
  id: number;
  nombre?: string | null;
  apellidos?: string | null;
  nif?: string | null;
  direccion?: string | null;
  telefono?: string | null;
  email?: string | null;
  empresa?: Pick<IEmpresa, 'id'> | null;
}

export type NewCliente = Omit<ICliente, 'id'> & { id: null };
