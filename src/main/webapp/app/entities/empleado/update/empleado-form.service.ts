import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmpleado, NewEmpleado } from '../empleado.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmpleado for edit and NewEmpleadoFormGroupInput for create.
 */
type EmpleadoFormGroupInput = IEmpleado | PartialWithRequiredKeyOf<NewEmpleado>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmpleado | NewEmpleado> = Omit<T, 'fechaContratacion'> & {
  fechaContratacion?: string | null;
};

type EmpleadoFormRawValue = FormValueOf<IEmpleado>;

type NewEmpleadoFormRawValue = FormValueOf<NewEmpleado>;

type EmpleadoFormDefaults = Pick<NewEmpleado, 'id' | 'fechaContratacion'>;

type EmpleadoFormGroupContent = {
  id: FormControl<EmpleadoFormRawValue['id'] | NewEmpleado['id']>;
  nombre: FormControl<EmpleadoFormRawValue['nombre']>;
  dni: FormControl<EmpleadoFormRawValue['dni']>;
  salario: FormControl<EmpleadoFormRawValue['salario']>;
  fechaContratacion: FormControl<EmpleadoFormRawValue['fechaContratacion']>;
  empresa: FormControl<EmpleadoFormRawValue['empresa']>;
};

export type EmpleadoFormGroup = FormGroup<EmpleadoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmpleadoFormService {
  createEmpleadoFormGroup(empleado: EmpleadoFormGroupInput = { id: null }): EmpleadoFormGroup {
    const empleadoRawValue = this.convertEmpleadoToEmpleadoRawValue({
      ...this.getFormDefaults(),
      ...empleado,
    });
    return new FormGroup<EmpleadoFormGroupContent>({
      id: new FormControl(
        { value: empleadoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(empleadoRawValue.nombre),
      dni: new FormControl(empleadoRawValue.dni),
      salario: new FormControl(empleadoRawValue.salario),
      fechaContratacion: new FormControl(empleadoRawValue.fechaContratacion),
      empresa: new FormControl(empleadoRawValue.empresa),
    });
  }

  getEmpleado(form: EmpleadoFormGroup): IEmpleado | NewEmpleado {
    return this.convertEmpleadoRawValueToEmpleado(form.getRawValue() as EmpleadoFormRawValue | NewEmpleadoFormRawValue);
  }

  resetForm(form: EmpleadoFormGroup, empleado: EmpleadoFormGroupInput): void {
    const empleadoRawValue = this.convertEmpleadoToEmpleadoRawValue({ ...this.getFormDefaults(), ...empleado });
    form.reset(
      {
        ...empleadoRawValue,
        id: { value: empleadoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmpleadoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaContratacion: currentTime,
    };
  }

  private convertEmpleadoRawValueToEmpleado(rawEmpleado: EmpleadoFormRawValue | NewEmpleadoFormRawValue): IEmpleado | NewEmpleado {
    return {
      ...rawEmpleado,
      fechaContratacion: dayjs(rawEmpleado.fechaContratacion, DATE_TIME_FORMAT),
    };
  }

  private convertEmpleadoToEmpleadoRawValue(
    empleado: IEmpleado | (Partial<NewEmpleado> & EmpleadoFormDefaults),
  ): EmpleadoFormRawValue | PartialWithRequiredKeyOf<NewEmpleadoFormRawValue> {
    return {
      ...empleado,
      fechaContratacion: empleado.fechaContratacion ? empleado.fechaContratacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
