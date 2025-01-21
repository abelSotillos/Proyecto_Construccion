import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPerfilUsuario, NewPerfilUsuario } from '../perfil-usuario.model';

export type PartialUpdatePerfilUsuario = Partial<IPerfilUsuario> & Pick<IPerfilUsuario, 'id'>;

export type EntityResponseType = HttpResponse<IPerfilUsuario>;
export type EntityArrayResponseType = HttpResponse<IPerfilUsuario[]>;

@Injectable({ providedIn: 'root' })
export class PerfilUsuarioService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/perfil-usuarios');

  create(perfilUsuario: NewPerfilUsuario): Observable<EntityResponseType> {
    return this.http.post<IPerfilUsuario>(this.resourceUrl, perfilUsuario, { observe: 'response' });
  }

  update(perfilUsuario: IPerfilUsuario): Observable<EntityResponseType> {
    return this.http.put<IPerfilUsuario>(`${this.resourceUrl}/${this.getPerfilUsuarioIdentifier(perfilUsuario)}`, perfilUsuario, {
      observe: 'response',
    });
  }

  partialUpdate(perfilUsuario: PartialUpdatePerfilUsuario): Observable<EntityResponseType> {
    return this.http.patch<IPerfilUsuario>(`${this.resourceUrl}/${this.getPerfilUsuarioIdentifier(perfilUsuario)}`, perfilUsuario, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPerfilUsuario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPerfilUsuario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPerfilUsuarioIdentifier(perfilUsuario: Pick<IPerfilUsuario, 'id'>): number {
    return perfilUsuario.id;
  }

  comparePerfilUsuario(o1: Pick<IPerfilUsuario, 'id'> | null, o2: Pick<IPerfilUsuario, 'id'> | null): boolean {
    return o1 && o2 ? this.getPerfilUsuarioIdentifier(o1) === this.getPerfilUsuarioIdentifier(o2) : o1 === o2;
  }

  addPerfilUsuarioToCollectionIfMissing<Type extends Pick<IPerfilUsuario, 'id'>>(
    perfilUsuarioCollection: Type[],
    ...perfilUsuariosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const perfilUsuarios: Type[] = perfilUsuariosToCheck.filter(isPresent);
    if (perfilUsuarios.length > 0) {
      const perfilUsuarioCollectionIdentifiers = perfilUsuarioCollection.map(perfilUsuarioItem =>
        this.getPerfilUsuarioIdentifier(perfilUsuarioItem),
      );
      const perfilUsuariosToAdd = perfilUsuarios.filter(perfilUsuarioItem => {
        const perfilUsuarioIdentifier = this.getPerfilUsuarioIdentifier(perfilUsuarioItem);
        if (perfilUsuarioCollectionIdentifiers.includes(perfilUsuarioIdentifier)) {
          return false;
        }
        perfilUsuarioCollectionIdentifiers.push(perfilUsuarioIdentifier);
        return true;
      });
      return [...perfilUsuariosToAdd, ...perfilUsuarioCollection];
    }
    return perfilUsuarioCollection;
  }
}
