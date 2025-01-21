export interface IEmpresa {
  id: number;
  nombre?: string | null;
  nif?: string | null;
  calle?: string | null;
  telefono?: string | null;
  provincia?: string | null;
  poblacion?: string | null;
}

export type NewEmpresa = Omit<IEmpresa, 'id'> & { id: null };
