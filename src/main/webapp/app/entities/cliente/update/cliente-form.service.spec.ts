import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cliente.test-samples';

import { ClienteFormService } from './cliente-form.service';

describe('Cliente Form Service', () => {
  let service: ClienteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClienteFormService);
  });

  describe('Service methods', () => {
    describe('createClienteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClienteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            apellidos: expect.any(Object),
            nif: expect.any(Object),
            direccion: expect.any(Object),
            telefono: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });

      it('passing ICliente should create a new form with FormGroup', () => {
        const formGroup = service.createClienteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            apellidos: expect.any(Object),
            nif: expect.any(Object),
            direccion: expect.any(Object),
            telefono: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });
    });

    describe('getCliente', () => {
      it('should return NewCliente for default Cliente initial value', () => {
        const formGroup = service.createClienteFormGroup(sampleWithNewData);

        const cliente = service.getCliente(formGroup) as any;

        expect(cliente).toMatchObject(sampleWithNewData);
      });

      it('should return NewCliente for empty Cliente initial value', () => {
        const formGroup = service.createClienteFormGroup();

        const cliente = service.getCliente(formGroup) as any;

        expect(cliente).toMatchObject({});
      });

      it('should return ICliente', () => {
        const formGroup = service.createClienteFormGroup(sampleWithRequiredData);

        const cliente = service.getCliente(formGroup) as any;

        expect(cliente).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICliente should not enable id FormControl', () => {
        const formGroup = service.createClienteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCliente should disable id FormControl', () => {
        const formGroup = service.createClienteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
