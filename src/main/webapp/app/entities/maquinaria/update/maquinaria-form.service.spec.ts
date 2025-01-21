import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../maquinaria.test-samples';

import { MaquinariaFormService } from './maquinaria-form.service';

describe('Maquinaria Form Service', () => {
  let service: MaquinariaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaquinariaFormService);
  });

  describe('Service methods', () => {
    describe('createMaquinariaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaquinariaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            modelo: expect.any(Object),
            estado: expect.any(Object),
            precio: expect.any(Object),
            empresa: expect.any(Object),
          }),
        );
      });

      it('passing IMaquinaria should create a new form with FormGroup', () => {
        const formGroup = service.createMaquinariaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            modelo: expect.any(Object),
            estado: expect.any(Object),
            precio: expect.any(Object),
            empresa: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaquinaria', () => {
      it('should return NewMaquinaria for default Maquinaria initial value', () => {
        const formGroup = service.createMaquinariaFormGroup(sampleWithNewData);

        const maquinaria = service.getMaquinaria(formGroup) as any;

        expect(maquinaria).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaquinaria for empty Maquinaria initial value', () => {
        const formGroup = service.createMaquinariaFormGroup();

        const maquinaria = service.getMaquinaria(formGroup) as any;

        expect(maquinaria).toMatchObject({});
      });

      it('should return IMaquinaria', () => {
        const formGroup = service.createMaquinariaFormGroup(sampleWithRequiredData);

        const maquinaria = service.getMaquinaria(formGroup) as any;

        expect(maquinaria).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaquinaria should not enable id FormControl', () => {
        const formGroup = service.createMaquinariaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaquinaria should disable id FormControl', () => {
        const formGroup = service.createMaquinariaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
