import { IEmpresa } from 'app/entities/empresa/empresa.model';

export interface IPerfilUsuario {
  id: number;
  empresa?: Pick<IEmpresa, 'id'> | null;
}

export type NewPerfilUsuario = Omit<IPerfilUsuario, 'id'> & { id: null };
