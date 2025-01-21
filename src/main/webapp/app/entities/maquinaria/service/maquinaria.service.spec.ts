import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMaquinaria } from '../maquinaria.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../maquinaria.test-samples';

import { MaquinariaService } from './maquinaria.service';

const requireRestSample: IMaquinaria = {
  ...sampleWithRequiredData,
};

describe('Maquinaria Service', () => {
  let service: MaquinariaService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaquinaria | IMaquinaria[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaquinariaService);
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

    it('should create a Maquinaria', () => {
      const maquinaria = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(maquinaria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Maquinaria', () => {
      const maquinaria = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(maquinaria).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Maquinaria', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Maquinaria', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Maquinaria', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaquinariaToCollectionIfMissing', () => {
      it('should add a Maquinaria to an empty array', () => {
        const maquinaria: IMaquinaria = sampleWithRequiredData;
        expectedResult = service.addMaquinariaToCollectionIfMissing([], maquinaria);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maquinaria);
      });

      it('should not add a Maquinaria to an array that contains it', () => {
        const maquinaria: IMaquinaria = sampleWithRequiredData;
        const maquinariaCollection: IMaquinaria[] = [
          {
            ...maquinaria,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaquinariaToCollectionIfMissing(maquinariaCollection, maquinaria);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Maquinaria to an array that doesn't contain it", () => {
        const maquinaria: IMaquinaria = sampleWithRequiredData;
        const maquinariaCollection: IMaquinaria[] = [sampleWithPartialData];
        expectedResult = service.addMaquinariaToCollectionIfMissing(maquinariaCollection, maquinaria);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maquinaria);
      });

      it('should add only unique Maquinaria to an array', () => {
        const maquinariaArray: IMaquinaria[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const maquinariaCollection: IMaquinaria[] = [sampleWithRequiredData];
        expectedResult = service.addMaquinariaToCollectionIfMissing(maquinariaCollection, ...maquinariaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const maquinaria: IMaquinaria = sampleWithRequiredData;
        const maquinaria2: IMaquinaria = sampleWithPartialData;
        expectedResult = service.addMaquinariaToCollectionIfMissing([], maquinaria, maquinaria2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maquinaria);
        expect(expectedResult).toContain(maquinaria2);
      });

      it('should accept null and undefined values', () => {
        const maquinaria: IMaquinaria = sampleWithRequiredData;
        expectedResult = service.addMaquinariaToCollectionIfMissing([], null, maquinaria, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maquinaria);
      });

      it('should return initial array if no Maquinaria is added', () => {
        const maquinariaCollection: IMaquinaria[] = [sampleWithRequiredData];
        expectedResult = service.addMaquinariaToCollectionIfMissing(maquinariaCollection, undefined, null);
        expect(expectedResult).toEqual(maquinariaCollection);
      });
    });

    describe('compareMaquinaria', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaquinaria(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 27800 };
        const entity2 = null;

        const compareResult1 = service.compareMaquinaria(entity1, entity2);
        const compareResult2 = service.compareMaquinaria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 27800 };
        const entity2 = { id: 18639 };

        const compareResult1 = service.compareMaquinaria(entity1, entity2);
        const compareResult2 = service.compareMaquinaria(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 27800 };
        const entity2 = { id: 27800 };

        const compareResult1 = service.compareMaquinaria(entity1, entity2);
        const compareResult2 = service.compareMaquinaria(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
