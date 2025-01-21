import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmpresa, NewEmpresa } from '../empresa.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmpresa for edit and NewEmpresaFormGroupInput for create.
 */
type EmpresaFormGroupInput = IEmpresa | PartialWithRequiredKeyOf<NewEmpresa>;

type EmpresaFormDefaults = Pick<NewEmpresa, 'id'>;

type EmpresaFormGroupContent = {
  id: FormControl<IEmpresa['id'] | NewEmpresa['id']>;
  nombre: FormControl<IEmpresa['nombre']>;
  nif: FormControl<IEmpresa['nif']>;
  calle: FormControl<IEmpresa['calle']>;
  telefono: FormControl<IEmpresa['telefono']>;
  provincia: FormControl<IEmpresa['provincia']>;
  poblacion: FormControl<IEmpresa['poblacion']>;
};

export type EmpresaFormGroup = FormGroup<EmpresaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmpresaFormService {
  createEmpresaFormGroup(empresa: EmpresaFormGroupInput = { id: null }): EmpresaFormGroup {
    const empresaRawValue = {
      ...this.getFormDefaults(),
      ...empresa,
    };
    return new FormGroup<EmpresaFormGroupContent>({
      id: new FormControl(
        { value: empresaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(empresaRawValue.nombre),
      nif: new FormControl(empresaRawValue.nif),
      calle: new FormControl(empresaRawValue.calle),
      telefono: new FormControl(empresaRawValue.telefono),
      provincia: new FormControl(empresaRawValue.provincia),
      poblacion: new FormControl(empresaRawValue.poblacion),
    });
  }

  getEmpresa(form: EmpresaFormGroup): IEmpresa | NewEmpresa {
    return form.getRawValue() as IEmpresa | NewEmpresa;
  }

  resetForm(form: EmpresaFormGroup, empresa: EmpresaFormGroupInput): void {
    const empresaRawValue = { ...this.getFormDefaults(), ...empresa };
    form.reset(
      {
        ...empresaRawValue,
        id: { value: empresaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmpresaFormDefaults {
    return {
      id: null,
    };
  }
}
