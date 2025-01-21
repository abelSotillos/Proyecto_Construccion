import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMaquinariaObra } from '../maquinaria-obra.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../maquinaria-obra.test-samples';

import { MaquinariaObraService } from './maquinaria-obra.service';

const requireRestSample: IMaquinariaObra = {
  ...sampleWithRequiredData,
};

describe('MaquinariaObra Service', () => {
  let service: MaquinariaObraService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaquinariaObra | IMaquinariaObra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaquinariaObraService);
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

    it('should create a MaquinariaObra', () => {
      const maquinariaObra = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(maquinariaObra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaquinariaObra', () => {
      const maquinariaObra = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(maquinariaObra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaquinariaObra', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaquinariaObra', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaquinariaObra', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaquinariaObraToCollectionIfMissing', () => {
      it('should add a MaquinariaObra to an empty array', () => {
        const maquinariaObra: IMaquinariaObra = sampleWithRequiredData;
        expectedResult = service.addMaquinariaObraToCollectionIfMissing([], maquinariaObra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maquinariaObra);
      });

      it('should not add a MaquinariaObra to an array that contains it', () => {
        const maquinariaObra: IMaquinariaObra = sampleWithRequiredData;
        const maquinariaObraCollection: IMaquinariaObra[] = [
          {
            ...maquinariaObra,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaquinariaObraToCollectionIfMissing(maquinariaObraCollection, maquinariaObra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaquinariaObra to an array that doesn't contain it", () => {
        const maquinariaObra: IMaquinariaObra = sampleWithRequiredData;
        const maquinariaObraCollection: IMaquinariaObra[] = [sampleWithPartialData];
        expectedResult = service.addMaquinariaObraToCollectionIfMissing(maquinariaObraCollection, maquinariaObra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maquinariaObra);
      });

      it('should add only unique MaquinariaObra to an array', () => {
        const maquinariaObraArray: IMaquinariaObra[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const maquinariaObraCollection: IMaquinariaObra[] = [sampleWithRequiredData];
        expectedResult = service.addMaquinariaObraToCollectionIfMissing(maquinariaObraCollection, ...maquinariaObraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const maquinariaObra: IMaquinariaObra = sampleWithRequiredData;
        const maquinariaObra2: IMaquinariaObra = sampleWithPartialData;
        expectedResult = service.addMaquinariaObraToCollectionIfMissing([], maquinariaObra, maquinariaObra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maquinariaObra);
        expect(expectedResult).toContain(maquinariaObra2);
      });

      it('should accept null and undefined values', () => {
        const maquinariaObra: IMaquinariaObra = sampleWithRequiredData;
        expectedResult = service.addMaquinariaObraToCollectionIfMissing([], null, maquinariaObra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maquinariaObra);
      });

      it('should return initial array if no MaquinariaObra is added', () => {
        const maquinariaObraCollection: IMaquinariaObra[] = [sampleWithRequiredData];
        expectedResult = service.addMaquinariaObraToCollectionIfMissing(maquinariaObraCollection, undefined, null);
        expect(expectedResult).toEqual(maquinariaObraCollection);
      });
    });

    describe('compareMaquinariaObra', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaquinariaObra(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 28762 };
        const entity2 = null;

        const compareResult1 = service.compareMaquinariaObra(entity1, entity2);
        const compareResult2 = service.compareMaquinariaObra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 28762 };
        const entity2 = { id: 1536 };

        const compareResult1 = service.compareMaquinariaObra(entity1, entity2);
        const compareResult2 = service.compareMaquinariaObra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 28762 };
        const entity2 = { id: 28762 };

        const compareResult1 = service.compareMaquinariaObra(entity1, entity2);
        const compareResult2 = service.compareMaquinariaObra(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
