import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMaterialObra } from '../material-obra.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../material-obra.test-samples';

import { MaterialObraService } from './material-obra.service';

const requireRestSample: IMaterialObra = {
  ...sampleWithRequiredData,
};

describe('MaterialObra Service', () => {
  let service: MaterialObraService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaterialObra | IMaterialObra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaterialObraService);
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

    it('should create a MaterialObra', () => {
      const materialObra = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(materialObra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaterialObra', () => {
      const materialObra = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(materialObra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaterialObra', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaterialObra', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MaterialObra', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMaterialObraToCollectionIfMissing', () => {
      it('should add a MaterialObra to an empty array', () => {
        const materialObra: IMaterialObra = sampleWithRequiredData;
        expectedResult = service.addMaterialObraToCollectionIfMissing([], materialObra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialObra);
      });

      it('should not add a MaterialObra to an array that contains it', () => {
        const materialObra: IMaterialObra = sampleWithRequiredData;
        const materialObraCollection: IMaterialObra[] = [
          {
            ...materialObra,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaterialObraToCollectionIfMissing(materialObraCollection, materialObra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaterialObra to an array that doesn't contain it", () => {
        const materialObra: IMaterialObra = sampleWithRequiredData;
        const materialObraCollection: IMaterialObra[] = [sampleWithPartialData];
        expectedResult = service.addMaterialObraToCollectionIfMissing(materialObraCollection, materialObra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialObra);
      });

      it('should add only unique MaterialObra to an array', () => {
        const materialObraArray: IMaterialObra[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const materialObraCollection: IMaterialObra[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialObraToCollectionIfMissing(materialObraCollection, ...materialObraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const materialObra: IMaterialObra = sampleWithRequiredData;
        const materialObra2: IMaterialObra = sampleWithPartialData;
        expectedResult = service.addMaterialObraToCollectionIfMissing([], materialObra, materialObra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(materialObra);
        expect(expectedResult).toContain(materialObra2);
      });

      it('should accept null and undefined values', () => {
        const materialObra: IMaterialObra = sampleWithRequiredData;
        expectedResult = service.addMaterialObraToCollectionIfMissing([], null, materialObra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(materialObra);
      });

      it('should return initial array if no MaterialObra is added', () => {
        const materialObraCollection: IMaterialObra[] = [sampleWithRequiredData];
        expectedResult = service.addMaterialObraToCollectionIfMissing(materialObraCollection, undefined, null);
        expect(expectedResult).toEqual(materialObraCollection);
      });
    });

    describe('compareMaterialObra', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaterialObra(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 15274 };
        const entity2 = null;

        const compareResult1 = service.compareMaterialObra(entity1, entity2);
        const compareResult2 = service.compareMaterialObra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 15274 };
        const entity2 = { id: 27281 };

        const compareResult1 = service.compareMaterialObra(entity1, entity2);
        const compareResult2 = service.compareMaterialObra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 15274 };
        const entity2 = { id: 15274 };

        const compareResult1 = service.compareMaterialObra(entity1, entity2);
        const compareResult2 = service.compareMaterialObra(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
