export interface ICliente {
  id: number;
  nombre?: string | null;
  apellidos?: string | null;
  nif?: string | null;
  direccion?: string | null;
  telefono?: string | null;
  email?: string | null;
}

export type NewCliente = Omit<ICliente, 'id'> & { id: null };
