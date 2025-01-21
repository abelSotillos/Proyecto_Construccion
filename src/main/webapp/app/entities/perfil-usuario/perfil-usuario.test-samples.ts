import { IPerfilUsuario, NewPerfilUsuario } from './perfil-usuario.model';

export const sampleWithRequiredData: IPerfilUsuario = {
  id: 15931,
};

export const sampleWithPartialData: IPerfilUsuario = {
  id: 16477,
};

export const sampleWithFullData: IPerfilUsuario = {
  id: 1191,
};

export const sampleWithNewData: NewPerfilUsuario = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
