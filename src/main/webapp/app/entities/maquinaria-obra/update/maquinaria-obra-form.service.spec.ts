import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../maquinaria-obra.test-samples';

import { MaquinariaObraFormService } from './maquinaria-obra-form.service';

describe('MaquinariaObra Form Service', () => {
  let service: MaquinariaObraFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaquinariaObraFormService);
  });

  describe('Service methods', () => {
    describe('createMaquinariaObraFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaquinariaObraFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            horas: expect.any(Object),
            obra: expect.any(Object),
            maquinaria: expect.any(Object),
          }),
        );
      });

      it('passing IMaquinariaObra should create a new form with FormGroup', () => {
        const formGroup = service.createMaquinariaObraFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            horas: expect.any(Object),
            obra: expect.any(Object),
            maquinaria: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaquinariaObra', () => {
      it('should return NewMaquinariaObra for default MaquinariaObra initial value', () => {
        const formGroup = service.createMaquinariaObraFormGroup(sampleWithNewData);

        const maquinariaObra = service.getMaquinariaObra(formGroup) as any;

        expect(maquinariaObra).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaquinariaObra for empty MaquinariaObra initial value', () => {
        const formGroup = service.createMaquinariaObraFormGroup();

        const maquinariaObra = service.getMaquinariaObra(formGroup) as any;

        expect(maquinariaObra).toMatchObject({});
      });

      it('should return IMaquinariaObra', () => {
        const formGroup = service.createMaquinariaObraFormGroup(sampleWithRequiredData);

        const maquinariaObra = service.getMaquinariaObra(formGroup) as any;

        expect(maquinariaObra).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaquinariaObra should not enable id FormControl', () => {
        const formGroup = service.createMaquinariaObraFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaquinariaObra should disable id FormControl', () => {
        const formGroup = service.createMaquinariaObraFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
