import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EstadoMaquinaria } from 'app/entities/enumerations/estado-maquinaria.model';

export interface IMaquinaria {
  id: number;
  modelo?: string | null;
  estado?: keyof typeof EstadoMaquinaria | null;
  precio?: number | null;
  empresa?: Pick<IEmpresa, 'id'> | null;
}

export type NewMaquinaria = Omit<IMaquinaria, 'id'> & { id: null };
