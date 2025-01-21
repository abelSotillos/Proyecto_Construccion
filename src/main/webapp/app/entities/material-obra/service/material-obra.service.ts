import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterialObra, NewMaterialObra } from '../material-obra.model';

export type PartialUpdateMaterialObra = Partial<IMaterialObra> & Pick<IMaterialObra, 'id'>;

export type EntityResponseType = HttpResponse<IMaterialObra>;
export type EntityArrayResponseType = HttpResponse<IMaterialObra[]>;

@Injectable({ providedIn: 'root' })
export class MaterialObraService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/material-obras');

  create(materialObra: NewMaterialObra): Observable<EntityResponseType> {
    return this.http.post<IMaterialObra>(this.resourceUrl, materialObra, { observe: 'response' });
  }

  update(materialObra: IMaterialObra): Observable<EntityResponseType> {
    return this.http.put<IMaterialObra>(`${this.resourceUrl}/${this.getMaterialObraIdentifier(materialObra)}`, materialObra, {
      observe: 'response',
    });
  }

  partialUpdate(materialObra: PartialUpdateMaterialObra): Observable<EntityResponseType> {
    return this.http.patch<IMaterialObra>(`${this.resourceUrl}/${this.getMaterialObraIdentifier(materialObra)}`, materialObra, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterialObra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterialObra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialObraIdentifier(materialObra: Pick<IMaterialObra, 'id'>): number {
    return materialObra.id;
  }

  compareMaterialObra(o1: Pick<IMaterialObra, 'id'> | null, o2: Pick<IMaterialObra, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialObraIdentifier(o1) === this.getMaterialObraIdentifier(o2) : o1 === o2;
  }

  addMaterialObraToCollectionIfMissing<Type extends Pick<IMaterialObra, 'id'>>(
    materialObraCollection: Type[],
    ...materialObrasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materialObras: Type[] = materialObrasToCheck.filter(isPresent);
    if (materialObras.length > 0) {
      const materialObraCollectionIdentifiers = materialObraCollection.map(materialObraItem =>
        this.getMaterialObraIdentifier(materialObraItem),
      );
      const materialObrasToAdd = materialObras.filter(materialObraItem => {
        const materialObraIdentifier = this.getMaterialObraIdentifier(materialObraItem);
        if (materialObraCollectionIdentifiers.includes(materialObraIdentifier)) {
          return false;
        }
        materialObraCollectionIdentifiers.push(materialObraIdentifier);
        return true;
      });
      return [...materialObrasToAdd, ...materialObraCollection];
    }
    return materialObraCollection;
  }
}
