import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICliente, NewCliente } from '../cliente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICliente for edit and NewClienteFormGroupInput for create.
 */
type ClienteFormGroupInput = ICliente | PartialWithRequiredKeyOf<NewCliente>;

type ClienteFormDefaults = Pick<NewCliente, 'id'>;

type ClienteFormGroupContent = {
  id: FormControl<ICliente['id'] | NewCliente['id']>;
  nombre: FormControl<ICliente['nombre']>;
  apellidos: FormControl<ICliente['apellidos']>;
  nif: FormControl<ICliente['nif']>;
  direccion: FormControl<ICliente['direccion']>;
  telefono: FormControl<ICliente['telefono']>;
  email: FormControl<ICliente['email']>;
  empresa: FormControl<ICliente['empresa']>;
};

export type ClienteFormGroup = FormGroup<ClienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClienteFormService {
  createClienteFormGroup(cliente: ClienteFormGroupInput = { id: null }): ClienteFormGroup {
    const clienteRawValue = {
      ...this.getFormDefaults(),
      ...cliente,
    };
    return new FormGroup<ClienteFormGroupContent>({
      id: new FormControl(
        { value: clienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(clienteRawValue.nombre),
      apellidos: new FormControl(clienteRawValue.apellidos),
      nif: new FormControl(clienteRawValue.nif),
      direccion: new FormControl(clienteRawValue.direccion),
      telefono: new FormControl(clienteRawValue.telefono),
      email: new FormControl(clienteRawValue.email),
      empresa: new FormControl(clienteRawValue.empresa),
    });
  }

  getCliente(form: ClienteFormGroup): ICliente | NewCliente {
    return form.getRawValue() as ICliente | NewCliente;
  }

  resetForm(form: ClienteFormGroup, cliente: ClienteFormGroupInput): void {
    const clienteRawValue = { ...this.getFormDefaults(), ...cliente };
    form.reset(
      {
        ...clienteRawValue,
        id: { value: clienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClienteFormDefaults {
    return {
      id: null,
    };
  }
}
