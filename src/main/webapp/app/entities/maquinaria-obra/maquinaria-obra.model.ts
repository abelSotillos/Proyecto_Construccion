import { IObra } from 'app/entities/obra/obra.model';
import { IMaquinaria } from 'app/entities/maquinaria/maquinaria.model';

export interface IMaquinariaObra {
  id: number;
  horas?: number | null;
  obra?: Pick<IObra, 'id'> | null;
  maquinaria?: Pick<IMaquinaria, 'id'> | null;
}

export type NewMaquinariaObra = Omit<IMaquinariaObra, 'id'> & { id: null };
