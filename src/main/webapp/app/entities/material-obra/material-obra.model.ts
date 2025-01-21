import { IObra } from 'app/entities/obra/obra.model';
import { IMaterial } from 'app/entities/material/material.model';

export interface IMaterialObra {
  id: number;
  cantidad?: number | null;
  obra?: Pick<IObra, 'id'> | null;
  material?: Pick<IMaterial, 'id'> | null;
}

export type NewMaterialObra = Omit<IMaterialObra, 'id'> & { id: null };
