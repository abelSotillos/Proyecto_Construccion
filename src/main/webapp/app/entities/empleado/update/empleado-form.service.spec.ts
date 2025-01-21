import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../empleado.test-samples';

import { EmpleadoFormService } from './empleado-form.service';

describe('Empleado Form Service', () => {
  let service: EmpleadoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmpleadoFormService);
  });

  describe('Service methods', () => {
    describe('createEmpleadoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmpleadoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            dni: expect.any(Object),
            salario: expect.any(Object),
            fechaContratacion: expect.any(Object),
            empresa: expect.any(Object),
          }),
        );
      });

      it('passing IEmpleado should create a new form with FormGroup', () => {
        const formGroup = service.createEmpleadoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            dni: expect.any(Object),
            salario: expect.any(Object),
            fechaContratacion: expect.any(Object),
            empresa: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmpleado', () => {
      it('should return NewEmpleado for default Empleado initial value', () => {
        const formGroup = service.createEmpleadoFormGroup(sampleWithNewData);

        const empleado = service.getEmpleado(formGroup) as any;

        expect(empleado).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmpleado for empty Empleado initial value', () => {
        const formGroup = service.createEmpleadoFormGroup();

        const empleado = service.getEmpleado(formGroup) as any;

        expect(empleado).toMatchObject({});
      });

      it('should return IEmpleado', () => {
        const formGroup = service.createEmpleadoFormGroup(sampleWithRequiredData);

        const empleado = service.getEmpleado(formGroup) as any;

        expect(empleado).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmpleado should not enable id FormControl', () => {
        const formGroup = service.createEmpleadoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmpleado should disable id FormControl', () => {
        const formGroup = service.createEmpleadoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
