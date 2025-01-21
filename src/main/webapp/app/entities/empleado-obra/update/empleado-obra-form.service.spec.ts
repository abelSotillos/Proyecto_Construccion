import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../empleado-obra.test-samples';

import { EmpleadoObraFormService } from './empleado-obra-form.service';

describe('EmpleadoObra Form Service', () => {
  let service: EmpleadoObraFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmpleadoObraFormService);
  });

  describe('Service methods', () => {
    describe('createEmpleadoObraFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmpleadoObraFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            horas: expect.any(Object),
            obra: expect.any(Object),
            empleado: expect.any(Object),
          }),
        );
      });

      it('passing IEmpleadoObra should create a new form with FormGroup', () => {
        const formGroup = service.createEmpleadoObraFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            horas: expect.any(Object),
            obra: expect.any(Object),
            empleado: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmpleadoObra', () => {
      it('should return NewEmpleadoObra for default EmpleadoObra initial value', () => {
        const formGroup = service.createEmpleadoObraFormGroup(sampleWithNewData);

        const empleadoObra = service.getEmpleadoObra(formGroup) as any;

        expect(empleadoObra).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmpleadoObra for empty EmpleadoObra initial value', () => {
        const formGroup = service.createEmpleadoObraFormGroup();

        const empleadoObra = service.getEmpleadoObra(formGroup) as any;

        expect(empleadoObra).toMatchObject({});
      });

      it('should return IEmpleadoObra', () => {
        const formGroup = service.createEmpleadoObraFormGroup(sampleWithRequiredData);

        const empleadoObra = service.getEmpleadoObra(formGroup) as any;

        expect(empleadoObra).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmpleadoObra should not enable id FormControl', () => {
        const formGroup = service.createEmpleadoObraFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmpleadoObra should disable id FormControl', () => {
        const formGroup = service.createEmpleadoObraFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
