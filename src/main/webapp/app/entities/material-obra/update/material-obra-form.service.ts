import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMaterialObra, NewMaterialObra } from '../material-obra.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaterialObra for edit and NewMaterialObraFormGroupInput for create.
 */
type MaterialObraFormGroupInput = IMaterialObra | PartialWithRequiredKeyOf<NewMaterialObra>;

type MaterialObraFormDefaults = Pick<NewMaterialObra, 'id'>;

type MaterialObraFormGroupContent = {
  id: FormControl<IMaterialObra['id'] | NewMaterialObra['id']>;
  cantidad: FormControl<IMaterialObra['cantidad']>;
  obra: FormControl<IMaterialObra['obra']>;
  material: FormControl<IMaterialObra['material']>;
};

export type MaterialObraFormGroup = FormGroup<MaterialObraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialObraFormService {
  createMaterialObraFormGroup(materialObra: MaterialObraFormGroupInput = { id: null }): MaterialObraFormGroup {
    const materialObraRawValue = {
      ...this.getFormDefaults(),
      ...materialObra,
    };
    return new FormGroup<MaterialObraFormGroupContent>({
      id: new FormControl(
        { value: materialObraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cantidad: new FormControl(materialObraRawValue.cantidad),
      obra: new FormControl(materialObraRawValue.obra),
      material: new FormControl(materialObraRawValue.material),
    });
  }

  getMaterialObra(form: MaterialObraFormGroup): IMaterialObra | NewMaterialObra {
    return form.getRawValue() as IMaterialObra | NewMaterialObra;
  }

  resetForm(form: MaterialObraFormGroup, materialObra: MaterialObraFormGroupInput): void {
    const materialObraRawValue = { ...this.getFormDefaults(), ...materialObra };
    form.reset(
      {
        ...materialObraRawValue,
        id: { value: materialObraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MaterialObraFormDefaults {
    return {
      id: null,
    };
  }
}
