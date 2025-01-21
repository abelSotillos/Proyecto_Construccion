import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../perfil-usuario.test-samples';

import { PerfilUsuarioFormService } from './perfil-usuario-form.service';

describe('PerfilUsuario Form Service', () => {
  let service: PerfilUsuarioFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PerfilUsuarioFormService);
  });

  describe('Service methods', () => {
    describe('createPerfilUsuarioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPerfilUsuarioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            empresa: expect.any(Object),
          }),
        );
      });

      it('passing IPerfilUsuario should create a new form with FormGroup', () => {
        const formGroup = service.createPerfilUsuarioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            empresa: expect.any(Object),
          }),
        );
      });
    });

    describe('getPerfilUsuario', () => {
      it('should return NewPerfilUsuario for default PerfilUsuario initial value', () => {
        const formGroup = service.createPerfilUsuarioFormGroup(sampleWithNewData);

        const perfilUsuario = service.getPerfilUsuario(formGroup) as any;

        expect(perfilUsuario).toMatchObject(sampleWithNewData);
      });

      it('should return NewPerfilUsuario for empty PerfilUsuario initial value', () => {
        const formGroup = service.createPerfilUsuarioFormGroup();

        const perfilUsuario = service.getPerfilUsuario(formGroup) as any;

        expect(perfilUsuario).toMatchObject({});
      });

      it('should return IPerfilUsuario', () => {
        const formGroup = service.createPerfilUsuarioFormGroup(sampleWithRequiredData);

        const perfilUsuario = service.getPerfilUsuario(formGroup) as any;

        expect(perfilUsuario).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPerfilUsuario should not enable id FormControl', () => {
        const formGroup = service.createPerfilUsuarioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPerfilUsuario should disable id FormControl', () => {
        const formGroup = service.createPerfilUsuarioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
