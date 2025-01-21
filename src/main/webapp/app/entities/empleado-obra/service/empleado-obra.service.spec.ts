import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmpleadoObra } from '../empleado-obra.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../empleado-obra.test-samples';

import { EmpleadoObraService } from './empleado-obra.service';

const requireRestSample: IEmpleadoObra = {
  ...sampleWithRequiredData,
};

describe('EmpleadoObra Service', () => {
  let service: EmpleadoObraService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmpleadoObra | IEmpleadoObra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmpleadoObraService);
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

    it('should create a EmpleadoObra', () => {
      const empleadoObra = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(empleadoObra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmpleadoObra', () => {
      const empleadoObra = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(empleadoObra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmpleadoObra', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmpleadoObra', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmpleadoObra', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmpleadoObraToCollectionIfMissing', () => {
      it('should add a EmpleadoObra to an empty array', () => {
        const empleadoObra: IEmpleadoObra = sampleWithRequiredData;
        expectedResult = service.addEmpleadoObraToCollectionIfMissing([], empleadoObra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empleadoObra);
      });

      it('should not add a EmpleadoObra to an array that contains it', () => {
        const empleadoObra: IEmpleadoObra = sampleWithRequiredData;
        const empleadoObraCollection: IEmpleadoObra[] = [
          {
            ...empleadoObra,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmpleadoObraToCollectionIfMissing(empleadoObraCollection, empleadoObra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmpleadoObra to an array that doesn't contain it", () => {
        const empleadoObra: IEmpleadoObra = sampleWithRequiredData;
        const empleadoObraCollection: IEmpleadoObra[] = [sampleWithPartialData];
        expectedResult = service.addEmpleadoObraToCollectionIfMissing(empleadoObraCollection, empleadoObra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empleadoObra);
      });

      it('should add only unique EmpleadoObra to an array', () => {
        const empleadoObraArray: IEmpleadoObra[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const empleadoObraCollection: IEmpleadoObra[] = [sampleWithRequiredData];
        expectedResult = service.addEmpleadoObraToCollectionIfMissing(empleadoObraCollection, ...empleadoObraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const empleadoObra: IEmpleadoObra = sampleWithRequiredData;
        const empleadoObra2: IEmpleadoObra = sampleWithPartialData;
        expectedResult = service.addEmpleadoObraToCollectionIfMissing([], empleadoObra, empleadoObra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empleadoObra);
        expect(expectedResult).toContain(empleadoObra2);
      });

      it('should accept null and undefined values', () => {
        const empleadoObra: IEmpleadoObra = sampleWithRequiredData;
        expectedResult = service.addEmpleadoObraToCollectionIfMissing([], null, empleadoObra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empleadoObra);
      });

      it('should return initial array if no EmpleadoObra is added', () => {
        const empleadoObraCollection: IEmpleadoObra[] = [sampleWithRequiredData];
        expectedResult = service.addEmpleadoObraToCollectionIfMissing(empleadoObraCollection, undefined, null);
        expect(expectedResult).toEqual(empleadoObraCollection);
      });
    });

    describe('compareEmpleadoObra', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmpleadoObra(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 14224 };
        const entity2 = null;

        const compareResult1 = service.compareEmpleadoObra(entity1, entity2);
        const compareResult2 = service.compareEmpleadoObra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 14224 };
        const entity2 = { id: 22362 };

        const compareResult1 = service.compareEmpleadoObra(entity1, entity2);
        const compareResult2 = service.compareEmpleadoObra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 14224 };
        const entity2 = { id: 14224 };

        const compareResult1 = service.compareEmpleadoObra(entity1, entity2);
        const compareResult2 = service.compareEmpleadoObra(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
