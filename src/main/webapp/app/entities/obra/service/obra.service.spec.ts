import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IObra } from '../obra.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../obra.test-samples';

import { ObraService, RestObra } from './obra.service';

const requireRestSample: RestObra = {
  ...sampleWithRequiredData,
  fechaInicio: sampleWithRequiredData.fechaInicio?.toJSON(),
  fechaFin: sampleWithRequiredData.fechaFin?.toJSON(),
};

describe('Obra Service', () => {
  let service: ObraService;
  let httpMock: HttpTestingController;
  let expectedResult: IObra | IObra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ObraService);
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

    it('should create a Obra', () => {
      const obra = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(obra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Obra', () => {
      const obra = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(obra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Obra', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Obra', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Obra', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addObraToCollectionIfMissing', () => {
      it('should add a Obra to an empty array', () => {
        const obra: IObra = sampleWithRequiredData;
        expectedResult = service.addObraToCollectionIfMissing([], obra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(obra);
      });

      it('should not add a Obra to an array that contains it', () => {
        const obra: IObra = sampleWithRequiredData;
        const obraCollection: IObra[] = [
          {
            ...obra,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addObraToCollectionIfMissing(obraCollection, obra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Obra to an array that doesn't contain it", () => {
        const obra: IObra = sampleWithRequiredData;
        const obraCollection: IObra[] = [sampleWithPartialData];
        expectedResult = service.addObraToCollectionIfMissing(obraCollection, obra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(obra);
      });

      it('should add only unique Obra to an array', () => {
        const obraArray: IObra[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const obraCollection: IObra[] = [sampleWithRequiredData];
        expectedResult = service.addObraToCollectionIfMissing(obraCollection, ...obraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const obra: IObra = sampleWithRequiredData;
        const obra2: IObra = sampleWithPartialData;
        expectedResult = service.addObraToCollectionIfMissing([], obra, obra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(obra);
        expect(expectedResult).toContain(obra2);
      });

      it('should accept null and undefined values', () => {
        const obra: IObra = sampleWithRequiredData;
        expectedResult = service.addObraToCollectionIfMissing([], null, obra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(obra);
      });

      it('should return initial array if no Obra is added', () => {
        const obraCollection: IObra[] = [sampleWithRequiredData];
        expectedResult = service.addObraToCollectionIfMissing(obraCollection, undefined, null);
        expect(expectedResult).toEqual(obraCollection);
      });
    });

    describe('compareObra', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareObra(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 28688 };
        const entity2 = null;

        const compareResult1 = service.compareObra(entity1, entity2);
        const compareResult2 = service.compareObra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 28688 };
        const entity2 = { id: 11447 };

        const compareResult1 = service.compareObra(entity1, entity2);
        const compareResult2 = service.compareObra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 28688 };
        const entity2 = { id: 28688 };

        const compareResult1 = service.compareObra(entity1, entity2);
        const compareResult2 = service.compareObra(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
