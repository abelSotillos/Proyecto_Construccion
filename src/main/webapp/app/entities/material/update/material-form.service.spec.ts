import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../material.test-samples';

import { MaterialFormService } from './material-form.service';

describe('Material Form Service', () => {
  let service: MaterialFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            precio: expect.any(Object),
            stock: expect.any(Object),
            unidadMedida: expect.any(Object),
            empresa: expect.any(Object),
          }),
        );
      });

      it('passing IMaterial should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            precio: expect.any(Object),
            stock: expect.any(Object),
            unidadMedida: expect.any(Object),
            empresa: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaterial', () => {
      it('should return NewMaterial for default Material initial value', () => {
        const formGroup = service.createMaterialFormGroup(sampleWithNewData);

        const material = service.getMaterial(formGroup) as any;

        expect(material).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaterial for empty Material initial value', () => {
        const formGroup = service.createMaterialFormGroup();

        const material = service.getMaterial(formGroup) as any;

        expect(material).toMatchObject({});
      });

      it('should return IMaterial', () => {
        const formGroup = service.createMaterialFormGroup(sampleWithRequiredData);

        const material = service.getMaterial(formGroup) as any;

        expect(material).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaterial should not enable id FormControl', () => {
        const formGroup = service.createMaterialFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaterial should disable id FormControl', () => {
        const formGroup = service.createMaterialFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
