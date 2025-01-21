import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaquinariaObra, NewMaquinariaObra } from '../maquinaria-obra.model';

export type PartialUpdateMaquinariaObra = Partial<IMaquinariaObra> & Pick<IMaquinariaObra, 'id'>;

export type EntityResponseType = HttpResponse<IMaquinariaObra>;
export type EntityArrayResponseType = HttpResponse<IMaquinariaObra[]>;

@Injectable({ providedIn: 'root' })
export class MaquinariaObraService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/maquinaria-obras');

  create(maquinariaObra: NewMaquinariaObra): Observable<EntityResponseType> {
    return this.http.post<IMaquinariaObra>(this.resourceUrl, maquinariaObra, { observe: 'response' });
  }

  update(maquinariaObra: IMaquinariaObra): Observable<EntityResponseType> {
    return this.http.put<IMaquinariaObra>(`${this.resourceUrl}/${this.getMaquinariaObraIdentifier(maquinariaObra)}`, maquinariaObra, {
      observe: 'response',
    });
  }

  partialUpdate(maquinariaObra: PartialUpdateMaquinariaObra): Observable<EntityResponseType> {
    return this.http.patch<IMaquinariaObra>(`${this.resourceUrl}/${this.getMaquinariaObraIdentifier(maquinariaObra)}`, maquinariaObra, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaquinariaObra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaquinariaObra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaquinariaObraIdentifier(maquinariaObra: Pick<IMaquinariaObra, 'id'>): number {
    return maquinariaObra.id;
  }

  compareMaquinariaObra(o1: Pick<IMaquinariaObra, 'id'> | null, o2: Pick<IMaquinariaObra, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaquinariaObraIdentifier(o1) === this.getMaquinariaObraIdentifier(o2) : o1 === o2;
  }

  addMaquinariaObraToCollectionIfMissing<Type extends Pick<IMaquinariaObra, 'id'>>(
    maquinariaObraCollection: Type[],
    ...maquinariaObrasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const maquinariaObras: Type[] = maquinariaObrasToCheck.filter(isPresent);
    if (maquinariaObras.length > 0) {
      const maquinariaObraCollectionIdentifiers = maquinariaObraCollection.map(maquinariaObraItem =>
        this.getMaquinariaObraIdentifier(maquinariaObraItem),
      );
      const maquinariaObrasToAdd = maquinariaObras.filter(maquinariaObraItem => {
        const maquinariaObraIdentifier = this.getMaquinariaObraIdentifier(maquinariaObraItem);
        if (maquinariaObraCollectionIdentifiers.includes(maquinariaObraIdentifier)) {
          return false;
        }
        maquinariaObraCollectionIdentifiers.push(maquinariaObraIdentifier);
        return true;
      });
      return [...maquinariaObrasToAdd, ...maquinariaObraCollection];
    }
    return maquinariaObraCollection;
  }
}
