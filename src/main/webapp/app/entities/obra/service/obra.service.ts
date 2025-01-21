import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IObra, NewObra } from '../obra.model';

export type PartialUpdateObra = Partial<IObra> & Pick<IObra, 'id'>;

type RestOf<T extends IObra | NewObra> = Omit<T, 'fechaInicio' | 'fechaFin'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

export type RestObra = RestOf<IObra>;

export type NewRestObra = RestOf<NewObra>;

export type PartialUpdateRestObra = RestOf<PartialUpdateObra>;

export type EntityResponseType = HttpResponse<IObra>;
export type EntityArrayResponseType = HttpResponse<IObra[]>;

@Injectable({ providedIn: 'root' })
export class ObraService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/obras');

  create(obra: NewObra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(obra);
    return this.http.post<RestObra>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(obra: IObra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(obra);
    return this.http
      .put<RestObra>(`${this.resourceUrl}/${this.getObraIdentifier(obra)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(obra: PartialUpdateObra): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(obra);
    return this.http
      .patch<RestObra>(`${this.resourceUrl}/${this.getObraIdentifier(obra)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestObra>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestObra[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getObraIdentifier(obra: Pick<IObra, 'id'>): number {
    return obra.id;
  }

  compareObra(o1: Pick<IObra, 'id'> | null, o2: Pick<IObra, 'id'> | null): boolean {
    return o1 && o2 ? this.getObraIdentifier(o1) === this.getObraIdentifier(o2) : o1 === o2;
  }

  addObraToCollectionIfMissing<Type extends Pick<IObra, 'id'>>(
    obraCollection: Type[],
    ...obrasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const obras: Type[] = obrasToCheck.filter(isPresent);
    if (obras.length > 0) {
      const obraCollectionIdentifiers = obraCollection.map(obraItem => this.getObraIdentifier(obraItem));
      const obrasToAdd = obras.filter(obraItem => {
        const obraIdentifier = this.getObraIdentifier(obraItem);
        if (obraCollectionIdentifiers.includes(obraIdentifier)) {
          return false;
        }
        obraCollectionIdentifiers.push(obraIdentifier);
        return true;
      });
      return [...obrasToAdd, ...obraCollection];
    }
    return obraCollection;
  }

  protected convertDateFromClient<T extends IObra | NewObra | PartialUpdateObra>(obra: T): RestOf<T> {
    return {
      ...obra,
      fechaInicio: obra.fechaInicio?.toJSON() ?? null,
      fechaFin: obra.fechaFin?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restObra: RestObra): IObra {
    return {
      ...restObra,
      fechaInicio: restObra.fechaInicio ? dayjs(restObra.fechaInicio) : undefined,
      fechaFin: restObra.fechaFin ? dayjs(restObra.fechaFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestObra>): HttpResponse<IObra> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestObra[]>): HttpResponse<IObra[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
