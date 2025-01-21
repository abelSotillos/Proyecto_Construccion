import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmpleadoObra, NewEmpleadoObra } from '../empleado-obra.model';

export type PartialUpdateEmpleadoObra = Partial<IEmpleadoObra> & Pick<IEmpleadoObra, 'id'>;

export type EntityResponseType = HttpResponse<IEmpleadoObra>;
export type EntityArrayResponseType = HttpResponse<IEmpleadoObra[]>;

@Injectable({ providedIn: 'root' })
export class EmpleadoObraService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/empleado-obras');

  create(empleadoObra: NewEmpleadoObra): Observable<EntityResponseType> {
    return this.http.post<IEmpleadoObra>(this.resourceUrl, empleadoObra, { observe: 'response' });
  }

  update(empleadoObra: IEmpleadoObra): Observable<EntityResponseType> {
    return this.http.put<IEmpleadoObra>(`${this.resourceUrl}/${this.getEmpleadoObraIdentifier(empleadoObra)}`, empleadoObra, {
      observe: 'response',
    });
  }

  partialUpdate(empleadoObra: PartialUpdateEmpleadoObra): Observable<EntityResponseType> {
    return this.http.patch<IEmpleadoObra>(`${this.resourceUrl}/${this.getEmpleadoObraIdentifier(empleadoObra)}`, empleadoObra, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmpleadoObra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmpleadoObra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmpleadoObraIdentifier(empleadoObra: Pick<IEmpleadoObra, 'id'>): number {
    return empleadoObra.id;
  }

  compareEmpleadoObra(o1: Pick<IEmpleadoObra, 'id'> | null, o2: Pick<IEmpleadoObra, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmpleadoObraIdentifier(o1) === this.getEmpleadoObraIdentifier(o2) : o1 === o2;
  }

  addEmpleadoObraToCollectionIfMissing<Type extends Pick<IEmpleadoObra, 'id'>>(
    empleadoObraCollection: Type[],
    ...empleadoObrasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const empleadoObras: Type[] = empleadoObrasToCheck.filter(isPresent);
    if (empleadoObras.length > 0) {
      const empleadoObraCollectionIdentifiers = empleadoObraCollection.map(empleadoObraItem =>
        this.getEmpleadoObraIdentifier(empleadoObraItem),
      );
      const empleadoObrasToAdd = empleadoObras.filter(empleadoObraItem => {
        const empleadoObraIdentifier = this.getEmpleadoObraIdentifier(empleadoObraItem);
        if (empleadoObraCollectionIdentifiers.includes(empleadoObraIdentifier)) {
          return false;
        }
        empleadoObraCollectionIdentifiers.push(empleadoObraIdentifier);
        return true;
      });
      return [...empleadoObrasToAdd, ...empleadoObraCollection];
    }
    return empleadoObraCollection;
  }
}
