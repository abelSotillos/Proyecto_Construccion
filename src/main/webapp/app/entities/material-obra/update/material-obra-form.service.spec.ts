import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../material-obra.test-samples';

import { MaterialObraFormService } from './material-obra-form.service';

describe('MaterialObra Form Service', () => {
  let service: MaterialObraFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialObraFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialObraFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialObraFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidad: expect.any(Object),
            obra: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });

      it('passing IMaterialObra should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialObraFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidad: expect.any(Object),
            obra: expect.any(Object),
            material: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaterialObra', () => {
      it('should return NewMaterialObra for default MaterialObra initial value', () => {
        const formGroup = service.createMaterialObraFormGroup(sampleWithNewData);

        const materialObra = service.getMaterialObra(formGroup) as any;

        expect(materialObra).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaterialObra for empty MaterialObra initial value', () => {
        const formGroup = service.createMaterialObraFormGroup();

        const materialObra = service.getMaterialObra(formGroup) as any;

        expect(materialObra).toMatchObject({});
      });

      it('should return IMaterialObra', () => {
        const formGroup = service.createMaterialObraFormGroup(sampleWithRequiredData);

        const materialObra = service.getMaterialObra(formGroup) as any;

        expect(materialObra).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaterialObra should not enable id FormControl', () => {
        const formGroup = service.createMaterialObraFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaterialObra should disable id FormControl', () => {
        const formGroup = service.createMaterialObraFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
