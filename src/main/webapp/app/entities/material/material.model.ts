import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { UnidadMedida } from 'app/entities/enumerations/unidad-medida.model';

export interface IMaterial {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  precio?: number | null;
  stock?: number | null;
  unidadMedida?: keyof typeof UnidadMedida | null;
  empresa?: Pick<IEmpresa, 'id'> | null;
}

export type NewMaterial = Omit<IMaterial, 'id'> & { id: null };
