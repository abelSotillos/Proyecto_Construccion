import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmpleado } from '../empleado.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../empleado.test-samples';

import { EmpleadoService, RestEmpleado } from './empleado.service';

const requireRestSample: RestEmpleado = {
  ...sampleWithRequiredData,
  fechaContratacion: sampleWithRequiredData.fechaContratacion?.toJSON(),
};

describe('Empleado Service', () => {
  let service: EmpleadoService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmpleado | IEmpleado[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmpleadoService);
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

    it('should create a Empleado', () => {
      const empleado = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(empleado).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Empleado', () => {
      const empleado = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(empleado).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Empleado', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Empleado', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Empleado', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmpleadoToCollectionIfMissing', () => {
      it('should add a Empleado to an empty array', () => {
        const empleado: IEmpleado = sampleWithRequiredData;
        expectedResult = service.addEmpleadoToCollectionIfMissing([], empleado);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empleado);
      });

      it('should not add a Empleado to an array that contains it', () => {
        const empleado: IEmpleado = sampleWithRequiredData;
        const empleadoCollection: IEmpleado[] = [
          {
            ...empleado,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmpleadoToCollectionIfMissing(empleadoCollection, empleado);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Empleado to an array that doesn't contain it", () => {
        const empleado: IEmpleado = sampleWithRequiredData;
        const empleadoCollection: IEmpleado[] = [sampleWithPartialData];
        expectedResult = service.addEmpleadoToCollectionIfMissing(empleadoCollection, empleado);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empleado);
      });

      it('should add only unique Empleado to an array', () => {
        const empleadoArray: IEmpleado[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const empleadoCollection: IEmpleado[] = [sampleWithRequiredData];
        expectedResult = service.addEmpleadoToCollectionIfMissing(empleadoCollection, ...empleadoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const empleado: IEmpleado = sampleWithRequiredData;
        const empleado2: IEmpleado = sampleWithPartialData;
        expectedResult = service.addEmpleadoToCollectionIfMissing([], empleado, empleado2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empleado);
        expect(expectedResult).toContain(empleado2);
      });

      it('should accept null and undefined values', () => {
        const empleado: IEmpleado = sampleWithRequiredData;
        expectedResult = service.addEmpleadoToCollectionIfMissing([], null, empleado, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empleado);
      });

      it('should return initial array if no Empleado is added', () => {
        const empleadoCollection: IEmpleado[] = [sampleWithRequiredData];
        expectedResult = service.addEmpleadoToCollectionIfMissing(empleadoCollection, undefined, null);
        expect(expectedResult).toEqual(empleadoCollection);
      });
    });

    describe('compareEmpleado', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmpleado(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 11214 };
        const entity2 = null;

        const compareResult1 = service.compareEmpleado(entity1, entity2);
        const compareResult2 = service.compareEmpleado(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 11214 };
        const entity2 = { id: 25035 };

        const compareResult1 = service.compareEmpleado(entity1, entity2);
        const compareResult2 = service.compareEmpleado(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 11214 };
        const entity2 = { id: 11214 };

        const compareResult1 = service.compareEmpleado(entity1, entity2);
        const compareResult2 = service.compareEmpleado(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
