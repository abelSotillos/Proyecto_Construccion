import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMaquinaria, NewMaquinaria } from '../maquinaria.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaquinaria for edit and NewMaquinariaFormGroupInput for create.
 */
type MaquinariaFormGroupInput = IMaquinaria | PartialWithRequiredKeyOf<NewMaquinaria>;

type MaquinariaFormDefaults = Pick<NewMaquinaria, 'id'>;

type MaquinariaFormGroupContent = {
  id: FormControl<IMaquinaria['id'] | NewMaquinaria['id']>;
  modelo: FormControl<IMaquinaria['modelo']>;
  estado: FormControl<IMaquinaria['estado']>;
  precio: FormControl<IMaquinaria['precio']>;
  empresa: FormControl<IMaquinaria['empresa']>;
};

export type MaquinariaFormGroup = FormGroup<MaquinariaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaquinariaFormService {
  createMaquinariaFormGroup(maquinaria: MaquinariaFormGroupInput = { id: null }): MaquinariaFormGroup {
    const maquinariaRawValue = {
      ...this.getFormDefaults(),
      ...maquinaria,
    };
    return new FormGroup<MaquinariaFormGroupContent>({
      id: new FormControl(
        { value: maquinariaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      modelo: new FormControl(maquinariaRawValue.modelo),
      estado: new FormControl(maquinariaRawValue.estado),
      precio: new FormControl(maquinariaRawValue.precio),
      empresa: new FormControl(maquinariaRawValue.empresa),
    });
  }

  getMaquinaria(form: MaquinariaFormGroup): IMaquinaria | NewMaquinaria {
    return form.getRawValue() as IMaquinaria | NewMaquinaria;
  }

  resetForm(form: MaquinariaFormGroup, maquinaria: MaquinariaFormGroupInput): void {
    const maquinariaRawValue = { ...this.getFormDefaults(), ...maquinaria };
    form.reset(
      {
        ...maquinariaRawValue,
        id: { value: maquinariaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaquinariaFormDefaults {
    return {
      id: null,
    };
  }
}
