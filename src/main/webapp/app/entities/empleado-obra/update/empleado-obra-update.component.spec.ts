import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IObra } from 'app/entities/obra/obra.model';
import { ObraService } from 'app/entities/obra/service/obra.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { IEmpleadoObra } from '../empleado-obra.model';
import { EmpleadoObraService } from '../service/empleado-obra.service';
import { EmpleadoObraFormService } from './empleado-obra-form.service';

import { EmpleadoObraUpdateComponent } from './empleado-obra-update.component';

describe('EmpleadoObra Management Update Component', () => {
  let comp: EmpleadoObraUpdateComponent;
  let fixture: ComponentFixture<EmpleadoObraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empleadoObraFormService: EmpleadoObraFormService;
  let empleadoObraService: EmpleadoObraService;
  let obraService: ObraService;
  let empleadoService: EmpleadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmpleadoObraUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmpleadoObraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpleadoObraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadoObraFormService = TestBed.inject(EmpleadoObraFormService);
    empleadoObraService = TestBed.inject(EmpleadoObraService);
    obraService = TestBed.inject(ObraService);
    empleadoService = TestBed.inject(EmpleadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Obra query and add missing value', () => {
      const empleadoObra: IEmpleadoObra = { id: 22362 };
      const obra: IObra = { id: 28688 };
      empleadoObra.obra = obra;

      const obraCollection: IObra[] = [{ id: 28688 }];
      jest.spyOn(obraService, 'query').mockReturnValue(of(new HttpResponse({ body: obraCollection })));
      const additionalObras = [obra];
      const expectedCollection: IObra[] = [...additionalObras, ...obraCollection];
      jest.spyOn(obraService, 'addObraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleadoObra });
      comp.ngOnInit();

      expect(obraService.query).toHaveBeenCalled();
      expect(obraService.addObraToCollectionIfMissing).toHaveBeenCalledWith(
        obraCollection,
        ...additionalObras.map(expect.objectContaining),
      );
      expect(comp.obrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Empleado query and add missing value', () => {
      const empleadoObra: IEmpleadoObra = { id: 22362 };
      const empleado: IEmpleado = { id: 11214 };
      empleadoObra.empleado = empleado;

      const empleadoCollection: IEmpleado[] = [{ id: 11214 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [empleado];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleadoObra });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(
        empleadoCollection,
        ...additionalEmpleados.map(expect.objectContaining),
      );
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const empleadoObra: IEmpleadoObra = { id: 22362 };
      const obra: IObra = { id: 28688 };
      empleadoObra.obra = obra;
      const empleado: IEmpleado = { id: 11214 };
      empleadoObra.empleado = empleado;

      activatedRoute.data = of({ empleadoObra });
      comp.ngOnInit();

      expect(comp.obrasSharedCollection).toContainEqual(obra);
      expect(comp.empleadosSharedCollection).toContainEqual(empleado);
      expect(comp.empleadoObra).toEqual(empleadoObra);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleadoObra>>();
      const empleadoObra = { id: 14224 };
      jest.spyOn(empleadoObraFormService, 'getEmpleadoObra').mockReturnValue(empleadoObra);
      jest.spyOn(empleadoObraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleadoObra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleadoObra }));
      saveSubject.complete();

      // THEN
      expect(empleadoObraFormService.getEmpleadoObra).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadoObraService.update).toHaveBeenCalledWith(expect.objectContaining(empleadoObra));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleadoObra>>();
      const empleadoObra = { id: 14224 };
      jest.spyOn(empleadoObraFormService, 'getEmpleadoObra').mockReturnValue({ id: null });
      jest.spyOn(empleadoObraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleadoObra: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleadoObra }));
      saveSubject.complete();

      // THEN
      expect(empleadoObraFormService.getEmpleadoObra).toHaveBeenCalled();
      expect(empleadoObraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleadoObra>>();
      const empleadoObra = { id: 14224 };
      jest.spyOn(empleadoObraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleadoObra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadoObraService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareObra', () => {
      it('Should forward to obraService', () => {
        const entity = { id: 28688 };
        const entity2 = { id: 11447 };
        jest.spyOn(obraService, 'compareObra');
        comp.compareObra(entity, entity2);
        expect(obraService.compareObra).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmpleado', () => {
      it('Should forward to empleadoService', () => {
        const entity = { id: 11214 };
        const entity2 = { id: 25035 };
        jest.spyOn(empleadoService, 'compareEmpleado');
        comp.compareEmpleado(entity, entity2);
        expect(empleadoService.compareEmpleado).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
