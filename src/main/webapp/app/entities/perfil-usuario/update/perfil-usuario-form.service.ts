import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPerfilUsuario, NewPerfilUsuario } from '../perfil-usuario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPerfilUsuario for edit and NewPerfilUsuarioFormGroupInput for create.
 */
type PerfilUsuarioFormGroupInput = IPerfilUsuario | PartialWithRequiredKeyOf<NewPerfilUsuario>;

type PerfilUsuarioFormDefaults = Pick<NewPerfilUsuario, 'id'>;

type PerfilUsuarioFormGroupContent = {
  id: FormControl<IPerfilUsuario['id'] | NewPerfilUsuario['id']>;
  empresa: FormControl<IPerfilUsuario['empresa']>;
};

export type PerfilUsuarioFormGroup = FormGroup<PerfilUsuarioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PerfilUsuarioFormService {
  createPerfilUsuarioFormGroup(perfilUsuario: PerfilUsuarioFormGroupInput = { id: null }): PerfilUsuarioFormGroup {
    const perfilUsuarioRawValue = {
      ...this.getFormDefaults(),
      ...perfilUsuario,
    };
    return new FormGroup<PerfilUsuarioFormGroupContent>({
      id: new FormControl(
        { value: perfilUsuarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      empresa: new FormControl(perfilUsuarioRawValue.empresa),
    });
  }

  getPerfilUsuario(form: PerfilUsuarioFormGroup): IPerfilUsuario | NewPerfilUsuario {
    return form.getRawValue() as IPerfilUsuario | NewPerfilUsuario;
  }

  resetForm(form: PerfilUsuarioFormGroup, perfilUsuario: PerfilUsuarioFormGroupInput): void {
    const perfilUsuarioRawValue = { ...this.getFormDefaults(), ...perfilUsuario };
    form.reset(
      {
        ...perfilUsuarioRawValue,
        id: { value: perfilUsuarioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PerfilUsuarioFormDefaults {
    return {
      id: null,
    };
  }
}
