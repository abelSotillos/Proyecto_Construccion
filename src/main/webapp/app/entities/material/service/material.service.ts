import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaterial, NewMaterial } from '../material.model';

export type PartialUpdateMaterial = Partial<IMaterial> & Pick<IMaterial, 'id'>;

export type EntityResponseType = HttpResponse<IMaterial>;
export type EntityArrayResponseType = HttpResponse<IMaterial[]>;

@Injectable({ providedIn: 'root' })
export class MaterialService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/materials');

  create(material: NewMaterial): Observable<EntityResponseType> {
    return this.http.post<IMaterial>(this.resourceUrl, material, { observe: 'response' });
  }

  update(material: IMaterial): Observable<EntityResponseType> {
    return this.http.put<IMaterial>(`${this.resourceUrl}/${this.getMaterialIdentifier(material)}`, material, { observe: 'response' });
  }

  partialUpdate(material: PartialUpdateMaterial): Observable<EntityResponseType> {
    return this.http.patch<IMaterial>(`${this.resourceUrl}/${this.getMaterialIdentifier(material)}`, material, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaterial>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaterial[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialIdentifier(material: Pick<IMaterial, 'id'>): number {
    return material.id;
  }

  compareMaterial(o1: Pick<IMaterial, 'id'> | null, o2: Pick<IMaterial, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialIdentifier(o1) === this.getMaterialIdentifier(o2) : o1 === o2;
  }

  addMaterialToCollectionIfMissing<Type extends Pick<IMaterial, 'id'>>(
    materialCollection: Type[],
    ...materialsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materials: Type[] = materialsToCheck.filter(isPresent);
    if (materials.length > 0) {
      const materialCollectionIdentifiers = materialCollection.map(materialItem => this.getMaterialIdentifier(materialItem));
      const materialsToAdd = materials.filter(materialItem => {
        const materialIdentifier = this.getMaterialIdentifier(materialItem);
        if (materialCollectionIdentifiers.includes(materialIdentifier)) {
          return false;
        }
        materialCollectionIdentifiers.push(materialIdentifier);
        return true;
      });
      return [...materialsToAdd, ...materialCollection];
    }
    return materialCollection;
  }
}
