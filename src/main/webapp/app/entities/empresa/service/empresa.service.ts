import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmpresa, NewEmpresa } from '../empresa.model';

export type PartialUpdateEmpresa = Partial<IEmpresa> & Pick<IEmpresa, 'id'>;

export type EntityResponseType = HttpResponse<IEmpresa>;
export type EntityArrayResponseType = HttpResponse<IEmpresa[]>;

@Injectable({ providedIn: 'root' })
export class EmpresaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/empresas');

  create(empresa: NewEmpresa): Observable<EntityResponseType> {
    return this.http.post<IEmpresa>(this.resourceUrl, empresa, { observe: 'response' });
  }

  update(empresa: IEmpresa): Observable<EntityResponseType> {
    return this.http.put<IEmpresa>(`${this.resourceUrl}/${this.getEmpresaIdentifier(empresa)}`, empresa, { observe: 'response' });
  }

  partialUpdate(empresa: PartialUpdateEmpresa): Observable<EntityResponseType> {
    return this.http.patch<IEmpresa>(`${this.resourceUrl}/${this.getEmpresaIdentifier(empresa)}`, empresa, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmpresa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmpresa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmpresaIdentifier(empresa: Pick<IEmpresa, 'id'>): number {
    return empresa.id;
  }

  compareEmpresa(o1: Pick<IEmpresa, 'id'> | null, o2: Pick<IEmpresa, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmpresaIdentifier(o1) === this.getEmpresaIdentifier(o2) : o1 === o2;
  }

  addEmpresaToCollectionIfMissing<Type extends Pick<IEmpresa, 'id'>>(
    empresaCollection: Type[],
    ...empresasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const empresas: Type[] = empresasToCheck.filter(isPresent);
    if (empresas.length > 0) {
      const empresaCollectionIdentifiers = empresaCollection.map(empresaItem => this.getEmpresaIdentifier(empresaItem));
      const empresasToAdd = empresas.filter(empresaItem => {
        const empresaIdentifier = this.getEmpresaIdentifier(empresaItem);
        if (empresaCollectionIdentifiers.includes(empresaIdentifier)) {
          return false;
        }
        empresaCollectionIdentifiers.push(empresaIdentifier);
        return true;
      });
      return [...empresasToAdd, ...empresaCollection];
    }
    return empresaCollection;
  }
}
