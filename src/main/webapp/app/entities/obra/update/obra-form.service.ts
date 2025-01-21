import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IObra, NewObra } from '../obra.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IObra for edit and NewObraFormGroupInput for create.
 */
type ObraFormGroupInput = IObra | PartialWithRequiredKeyOf<NewObra>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IObra | NewObra> = Omit<T, 'fechaInicio' | 'fechaFin'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

type ObraFormRawValue = FormValueOf<IObra>;

type NewObraFormRawValue = FormValueOf<NewObra>;

type ObraFormDefaults = Pick<NewObra, 'id' | 'fechaInicio' | 'fechaFin'>;

type ObraFormGroupContent = {
  id: FormControl<ObraFormRawValue['id'] | NewObra['id']>;
  nombre: FormControl<ObraFormRawValue['nombre']>;
  direccion: FormControl<ObraFormRawValue['direccion']>;
  fechaInicio: FormControl<ObraFormRawValue['fechaInicio']>;
  fechaFin: FormControl<ObraFormRawValue['fechaFin']>;
  coste: FormControl<ObraFormRawValue['coste']>;
  estado: FormControl<ObraFormRawValue['estado']>;
  costePagado: FormControl<ObraFormRawValue['costePagado']>;
  empresa: FormControl<ObraFormRawValue['empresa']>;
  cliente: FormControl<ObraFormRawValue['cliente']>;
};

export type ObraFormGroup = FormGroup<ObraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ObraFormService {
  createObraFormGroup(obra: ObraFormGroupInput = { id: null }): ObraFormGroup {
    const obraRawValue = this.convertObraToObraRawValue({
      ...this.getFormDefaults(),
      ...obra,
    });
    return new FormGroup<ObraFormGroupContent>({
      id: new FormControl(
        { value: obraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(obraRawValue.nombre),
      direccion: new FormControl(obraRawValue.direccion),
      fechaInicio: new FormControl(obraRawValue.fechaInicio),
      fechaFin: new FormControl(obraRawValue.fechaFin),
      coste: new FormControl(obraRawValue.coste),
      estado: new FormControl(obraRawValue.estado),
      costePagado: new FormControl(obraRawValue.costePagado),
      empresa: new FormControl(obraRawValue.empresa),
      cliente: new FormControl(obraRawValue.cliente),
    });
  }

  getObra(form: ObraFormGroup): IObra | NewObra {
    return this.convertObraRawValueToObra(form.getRawValue() as ObraFormRawValue | NewObraFormRawValue);
  }

  resetForm(form: ObraFormGroup, obra: ObraFormGroupInput): void {
    const obraRawValue = this.convertObraToObraRawValue({ ...this.getFormDefaults(), ...obra });
    form.reset(
      {
        ...obraRawValue,
        id: { value: obraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ObraFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaInicio: currentTime,
      fechaFin: currentTime,
    };
  }

  private convertObraRawValueToObra(rawObra: ObraFormRawValue | NewObraFormRawValue): IObra | NewObra {
    return {
      ...rawObra,
      fechaInicio: dayjs(rawObra.fechaInicio, DATE_TIME_FORMAT),
      fechaFin: dayjs(rawObra.fechaFin, DATE_TIME_FORMAT),
    };
  }

  private convertObraToObraRawValue(
    obra: IObra | (Partial<NewObra> & ObraFormDefaults),
  ): ObraFormRawValue | PartialWithRequiredKeyOf<NewObraFormRawValue> {
    return {
      ...obra,
      fechaInicio: obra.fechaInicio ? obra.fechaInicio.format(DATE_TIME_FORMAT) : undefined,
      fechaFin: obra.fechaFin ? obra.fechaFin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
