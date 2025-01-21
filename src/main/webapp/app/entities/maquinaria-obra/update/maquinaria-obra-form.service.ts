import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMaquinariaObra, NewMaquinariaObra } from '../maquinaria-obra.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaquinariaObra for edit and NewMaquinariaObraFormGroupInput for create.
 */
type MaquinariaObraFormGroupInput = IMaquinariaObra | PartialWithRequiredKeyOf<NewMaquinariaObra>;

type MaquinariaObraFormDefaults = Pick<NewMaquinariaObra, 'id'>;

type MaquinariaObraFormGroupContent = {
  id: FormControl<IMaquinariaObra['id'] | NewMaquinariaObra['id']>;
  horas: FormControl<IMaquinariaObra['horas']>;
  obra: FormControl<IMaquinariaObra['obra']>;
  maquinaria: FormControl<IMaquinariaObra['maquinaria']>;
};

export type MaquinariaObraFormGroup = FormGroup<MaquinariaObraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaquinariaObraFormService {
  createMaquinariaObraFormGroup(maquinariaObra: MaquinariaObraFormGroupInput = { id: null }): MaquinariaObraFormGroup {
    const maquinariaObraRawValue = {
      ...this.getFormDefaults(),
      ...maquinariaObra,
    };
    return new FormGroup<MaquinariaObraFormGroupContent>({
      id: new FormControl(
        { value: maquinariaObraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      horas: new FormControl(maquinariaObraRawValue.horas),
      obra: new FormControl(maquinariaObraRawValue.obra),
      maquinaria: new FormControl(maquinariaObraRawValue.maquinaria),
    });
  }

  getMaquinariaObra(form: MaquinariaObraFormGroup): IMaquinariaObra | NewMaquinariaObra {
    return form.getRawValue() as IMaquinariaObra | NewMaquinariaObra;
  }

  resetForm(form: MaquinariaObraFormGroup, maquinariaObra: MaquinariaObraFormGroupInput): void {
    const maquinariaObraRawValue = { ...this.getFormDefaults(), ...maquinariaObra };
    form.reset(
      {
        ...maquinariaObraRawValue,
        id: { value: maquinariaObraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaquinariaObraFormDefaults {
    return {
      id: null,
    };
  }
}
