import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaquinaria, NewMaquinaria } from '../maquinaria.model';

export type PartialUpdateMaquinaria = Partial<IMaquinaria> & Pick<IMaquinaria, 'id'>;

export type EntityResponseType = HttpResponse<IMaquinaria>;
export type EntityArrayResponseType = HttpResponse<IMaquinaria[]>;

@Injectable({ providedIn: 'root' })
export class MaquinariaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/maquinarias');

  create(maquinaria: NewMaquinaria): Observable<EntityResponseType> {
    return this.http.post<IMaquinaria>(this.resourceUrl, maquinaria, { observe: 'response' });
  }

  update(maquinaria: IMaquinaria): Observable<EntityResponseType> {
    return this.http.put<IMaquinaria>(`${this.resourceUrl}/${this.getMaquinariaIdentifier(maquinaria)}`, maquinaria, {
      observe: 'response',
    });
  }

  partialUpdate(maquinaria: PartialUpdateMaquinaria): Observable<EntityResponseType> {
    return this.http.patch<IMaquinaria>(`${this.resourceUrl}/${this.getMaquinariaIdentifier(maquinaria)}`, maquinaria, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaquinaria>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaquinaria[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaquinariaIdentifier(maquinaria: Pick<IMaquinaria, 'id'>): number {
    return maquinaria.id;
  }

  compareMaquinaria(o1: Pick<IMaquinaria, 'id'> | null, o2: Pick<IMaquinaria, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaquinariaIdentifier(o1) === this.getMaquinariaIdentifier(o2) : o1 === o2;
  }

  addMaquinariaToCollectionIfMissing<Type extends Pick<IMaquinaria, 'id'>>(
    maquinariaCollection: Type[],
    ...maquinariasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const maquinarias: Type[] = maquinariasToCheck.filter(isPresent);
    if (maquinarias.length > 0) {
      const maquinariaCollectionIdentifiers = maquinariaCollection.map(maquinariaItem => this.getMaquinariaIdentifier(maquinariaItem));
      const maquinariasToAdd = maquinarias.filter(maquinariaItem => {
        const maquinariaIdentifier = this.getMaquinariaIdentifier(maquinariaItem);
        if (maquinariaCollectionIdentifiers.includes(maquinariaIdentifier)) {
          return false;
        }
        maquinariaCollectionIdentifiers.push(maquinariaIdentifier);
        return true;
      });
      return [...maquinariasToAdd, ...maquinariaCollection];
    }
    return maquinariaCollection;
  }
}
