import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPerfilUsuario } from '../perfil-usuario.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../perfil-usuario.test-samples';

import { PerfilUsuarioService } from './perfil-usuario.service';

const requireRestSample: IPerfilUsuario = {
  ...sampleWithRequiredData,
};

describe('PerfilUsuario Service', () => {
  let service: PerfilUsuarioService;
  let httpMock: HttpTestingController;
  let expectedResult: IPerfilUsuario | IPerfilUsuario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PerfilUsuarioService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PerfilUsuario', () => {
      const perfilUsuario = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(perfilUsuario).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PerfilUsuario', () => {
      const perfilUsuario = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(perfilUsuario).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PerfilUsuario', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PerfilUsuario', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PerfilUsuario', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPerfilUsuarioToCollectionIfMissing', () => {
      it('should add a PerfilUsuario to an empty array', () => {
        const perfilUsuario: IPerfilUsuario = sampleWithRequiredData;
        expectedResult = service.addPerfilUsuarioToCollectionIfMissing([], perfilUsuario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(perfilUsuario);
      });

      it('should not add a PerfilUsuario to an array that contains it', () => {
        const perfilUsuario: IPerfilUsuario = sampleWithRequiredData;
        const perfilUsuarioCollection: IPerfilUsuario[] = [
          {
            ...perfilUsuario,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPerfilUsuarioToCollectionIfMissing(perfilUsuarioCollection, perfilUsuario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PerfilUsuario to an array that doesn't contain it", () => {
        const perfilUsuario: IPerfilUsuario = sampleWithRequiredData;
        const perfilUsuarioCollection: IPerfilUsuario[] = [sampleWithPartialData];
        expectedResult = service.addPerfilUsuarioToCollectionIfMissing(perfilUsuarioCollection, perfilUsuario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(perfilUsuario);
      });

      it('should add only unique PerfilUsuario to an array', () => {
        const perfilUsuarioArray: IPerfilUsuario[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const perfilUsuarioCollection: IPerfilUsuario[] = [sampleWithRequiredData];
        expectedResult = service.addPerfilUsuarioToCollectionIfMissing(perfilUsuarioCollection, ...perfilUsuarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const perfilUsuario: IPerfilUsuario = sampleWithRequiredData;
        const perfilUsuario2: IPerfilUsuario = sampleWithPartialData;
        expectedResult = service.addPerfilUsuarioToCollectionIfMissing([], perfilUsuario, perfilUsuario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(perfilUsuario);
        expect(expectedResult).toContain(perfilUsuario2);
      });

      it('should accept null and undefined values', () => {
        const perfilUsuario: IPerfilUsuario = sampleWithRequiredData;
        expectedResult = service.addPerfilUsuarioToCollectionIfMissing([], null, perfilUsuario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(perfilUsuario);
      });

      it('should return initial array if no PerfilUsuario is added', () => {
        const perfilUsuarioCollection: IPerfilUsuario[] = [sampleWithRequiredData];
        expectedResult = service.addPerfilUsuarioToCollectionIfMissing(perfilUsuarioCollection, undefined, null);
        expect(expectedResult).toEqual(perfilUsuarioCollection);
      });
    });

    describe('comparePerfilUsuario', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePerfilUsuario(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 7928 };
        const entity2 = null;

        const compareResult1 = service.comparePerfilUsuario(entity1, entity2);
        const compareResult2 = service.comparePerfilUsuario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 7928 };
        const entity2 = { id: 24446 };

        const compareResult1 = service.comparePerfilUsuario(entity1, entity2);
        const compareResult2 = service.comparePerfilUsuario(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 7928 };
        const entity2 = { id: 7928 };

        const compareResult1 = service.comparePerfilUsuario(entity1, entity2);
        const compareResult2 = service.comparePerfilUsuario(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
