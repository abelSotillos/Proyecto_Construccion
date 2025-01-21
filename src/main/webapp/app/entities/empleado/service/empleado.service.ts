import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmpleado, NewEmpleado } from '../empleado.model';

export type PartialUpdateEmpleado = Partial<IEmpleado> & Pick<IEmpleado, 'id'>;

type RestOf<T extends IEmpleado | NewEmpleado> = Omit<T, 'fechaContratacion'> & {
  fechaContratacion?: string | null;
};

export type RestEmpleado = RestOf<IEmpleado>;

export type NewRestEmpleado = RestOf<NewEmpleado>;

export type PartialUpdateRestEmpleado = RestOf<PartialUpdateEmpleado>;

export type EntityResponseType = HttpResponse<IEmpleado>;
export type EntityArrayResponseType = HttpResponse<IEmpleado[]>;

@Injectable({ providedIn: 'root' })
export class EmpleadoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/empleados');

  create(empleado: NewEmpleado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(empleado);
    return this.http
      .post<RestEmpleado>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(empleado: IEmpleado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(empleado);
    return this.http
      .put<RestEmpleado>(`${this.resourceUrl}/${this.getEmpleadoIdentifier(empleado)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(empleado: PartialUpdateEmpleado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(empleado);
    return this.http
      .patch<RestEmpleado>(`${this.resourceUrl}/${this.getEmpleadoIdentifier(empleado)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmpleado>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmpleado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmpleadoIdentifier(empleado: Pick<IEmpleado, 'id'>): number {
    return empleado.id;
  }

  compareEmpleado(o1: Pick<IEmpleado, 'id'> | null, o2: Pick<IEmpleado, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmpleadoIdentifier(o1) === this.getEmpleadoIdentifier(o2) : o1 === o2;
  }

  addEmpleadoToCollectionIfMissing<Type extends Pick<IEmpleado, 'id'>>(
    empleadoCollection: Type[],
    ...empleadosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const empleados: Type[] = empleadosToCheck.filter(isPresent);
    if (empleados.length > 0) {
      const empleadoCollectionIdentifiers = empleadoCollection.map(empleadoItem => this.getEmpleadoIdentifier(empleadoItem));
      const empleadosToAdd = empleados.filter(empleadoItem => {
        const empleadoIdentifier = this.getEmpleadoIdentifier(empleadoItem);
        if (empleadoCollectionIdentifiers.includes(empleadoIdentifier)) {
          return false;
        }
        empleadoCollectionIdentifiers.push(empleadoIdentifier);
        return true;
      });
      return [...empleadosToAdd, ...empleadoCollection];
    }
    return empleadoCollection;
  }

  protected convertDateFromClient<T extends IEmpleado | NewEmpleado | PartialUpdateEmpleado>(empleado: T): RestOf<T> {
    return {
      ...empleado,
      fechaContratacion: empleado.fechaContratacion?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmpleado: RestEmpleado): IEmpleado {
    return {
      ...restEmpleado,
      fechaContratacion: restEmpleado.fechaContratacion ? dayjs(restEmpleado.fechaContratacion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmpleado>): HttpResponse<IEmpleado> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmpleado[]>): HttpResponse<IEmpleado[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
