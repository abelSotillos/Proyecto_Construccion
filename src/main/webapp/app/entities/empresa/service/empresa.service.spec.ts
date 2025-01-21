import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmpresa } from '../empresa.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../empresa.test-samples';

import { EmpresaService } from './empresa.service';

const requireRestSample: IEmpresa = {
  ...sampleWithRequiredData,
};

describe('Empresa Service', () => {
  let service: EmpresaService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmpresa | IEmpresa[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmpresaService);
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

    it('should create a Empresa', () => {
      const empresa = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(empresa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Empresa', () => {
      const empresa = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(empresa).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Empresa', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Empresa', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Empresa', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmpresaToCollectionIfMissing', () => {
      it('should add a Empresa to an empty array', () => {
        const empresa: IEmpresa = sampleWithRequiredData;
        expectedResult = service.addEmpresaToCollectionIfMissing([], empresa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empresa);
      });

      it('should not add a Empresa to an array that contains it', () => {
        const empresa: IEmpresa = sampleWithRequiredData;
        const empresaCollection: IEmpresa[] = [
          {
            ...empresa,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmpresaToCollectionIfMissing(empresaCollection, empresa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Empresa to an array that doesn't contain it", () => {
        const empresa: IEmpresa = sampleWithRequiredData;
        const empresaCollection: IEmpresa[] = [sampleWithPartialData];
        expectedResult = service.addEmpresaToCollectionIfMissing(empresaCollection, empresa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empresa);
      });

      it('should add only unique Empresa to an array', () => {
        const empresaArray: IEmpresa[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const empresaCollection: IEmpresa[] = [sampleWithRequiredData];
        expectedResult = service.addEmpresaToCollectionIfMissing(empresaCollection, ...empresaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const empresa: IEmpresa = sampleWithRequiredData;
        const empresa2: IEmpresa = sampleWithPartialData;
        expectedResult = service.addEmpresaToCollectionIfMissing([], empresa, empresa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empresa);
        expect(expectedResult).toContain(empresa2);
      });

      it('should accept null and undefined values', () => {
        const empresa: IEmpresa = sampleWithRequiredData;
        expectedResult = service.addEmpresaToCollectionIfMissing([], null, empresa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empresa);
      });

      it('should return initial array if no Empresa is added', () => {
        const empresaCollection: IEmpresa[] = [sampleWithRequiredData];
        expectedResult = service.addEmpresaToCollectionIfMissing(empresaCollection, undefined, null);
        expect(expectedResult).toEqual(empresaCollection);
      });
    });

    describe('compareEmpresa', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmpresa(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 13465 };
        const entity2 = null;

        const compareResult1 = service.compareEmpresa(entity1, entity2);
        const compareResult2 = service.compareEmpresa(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 13465 };
        const entity2 = { id: 16890 };

        const compareResult1 = service.compareEmpresa(entity1, entity2);
        const compareResult2 = service.compareEmpresa(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 13465 };
        const entity2 = { id: 13465 };

        const compareResult1 = service.compareEmpresa(entity1, entity2);
        const compareResult2 = service.compareEmpresa(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
