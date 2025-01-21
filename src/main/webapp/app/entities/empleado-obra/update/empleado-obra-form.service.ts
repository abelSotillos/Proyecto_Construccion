import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmpleadoObra, NewEmpleadoObra } from '../empleado-obra.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmpleadoObra for edit and NewEmpleadoObraFormGroupInput for create.
 */
type EmpleadoObraFormGroupInput = IEmpleadoObra | PartialWithRequiredKeyOf<NewEmpleadoObra>;

type EmpleadoObraFormDefaults = Pick<NewEmpleadoObra, 'id'>;

type EmpleadoObraFormGroupContent = {
  id: FormControl<IEmpleadoObra['id'] | NewEmpleadoObra['id']>;
  horas: FormControl<IEmpleadoObra['horas']>;
  obra: FormControl<IEmpleadoObra['obra']>;
  empleado: FormControl<IEmpleadoObra['empleado']>;
};

export type EmpleadoObraFormGroup = FormGroup<EmpleadoObraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmpleadoObraFormService {
  createEmpleadoObraFormGroup(empleadoObra: EmpleadoObraFormGroupInput = { id: null }): EmpleadoObraFormGroup {
    const empleadoObraRawValue = {
      ...this.getFormDefaults(),
      ...empleadoObra,
    };
    return new FormGroup<EmpleadoObraFormGroupContent>({
      id: new FormControl(
        { value: empleadoObraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      horas: new FormControl(empleadoObraRawValue.horas),
      obra: new FormControl(empleadoObraRawValue.obra),
      empleado: new FormControl(empleadoObraRawValue.empleado),
    });
  }

  getEmpleadoObra(form: EmpleadoObraFormGroup): IEmpleadoObra | NewEmpleadoObra {
    return form.getRawValue() as IEmpleadoObra | NewEmpleadoObra;
  }

  resetForm(form: EmpleadoObraFormGroup, empleadoObra: EmpleadoObraFormGroupInput): void {
    const empleadoObraRawValue = { ...this.getFormDefaults(), ...empleadoObra };
    form.reset(
      {
        ...empleadoObraRawValue,
        id: { value: empleadoObraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmpleadoObraFormDefaults {
    return {
      id: null,
    };
  }
}
