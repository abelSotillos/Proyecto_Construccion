import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { EmpleadoService } from '../service/empleado.service';
import { IEmpleado } from '../empleado.model';
import { EmpleadoFormService } from './empleado-form.service';

import { EmpleadoUpdateComponent } from './empleado-update.component';

describe('Empleado Management Update Component', () => {
  let comp: EmpleadoUpdateComponent;
  let fixture: ComponentFixture<EmpleadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empleadoFormService: EmpleadoFormService;
  let empleadoService: EmpleadoService;
  let empresaService: EmpresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmpleadoUpdateComponent],
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
      .overrideTemplate(EmpleadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpleadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadoFormService = TestBed.inject(EmpleadoFormService);
    empleadoService = TestBed.inject(EmpleadoService);
    empresaService = TestBed.inject(EmpresaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empresa query and add missing value', () => {
      const empleado: IEmpleado = { id: 25035 };
      const empresa: IEmpresa = { id: 13465 };
      empleado.empresa = empresa;

      const empresaCollection: IEmpresa[] = [{ id: 13465 }];
      jest.spyOn(empresaService, 'query').mockReturnValue(of(new HttpResponse({ body: empresaCollection })));
      const additionalEmpresas = [empresa];
      const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
      jest.spyOn(empresaService, 'addEmpresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(empresaService.query).toHaveBeenCalled();
      expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(
        empresaCollection,
        ...additionalEmpresas.map(expect.objectContaining),
      );
      expect(comp.empresasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const empleado: IEmpleado = { id: 25035 };
      const empresa: IEmpresa = { id: 13465 };
      empleado.empresa = empresa;

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(comp.empresasSharedCollection).toContainEqual(empresa);
      expect(comp.empleado).toEqual(empleado);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleado>>();
      const empleado = { id: 11214 };
      jest.spyOn(empleadoFormService, 'getEmpleado').mockReturnValue(empleado);
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(empleadoFormService.getEmpleado).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadoService.update).toHaveBeenCalledWith(expect.objectContaining(empleado));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleado>>();
      const empleado = { id: 11214 };
      jest.spyOn(empleadoFormService, 'getEmpleado').mockReturnValue({ id: null });
      jest.spyOn(empleadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(empleadoFormService.getEmpleado).toHaveBeenCalled();
      expect(empleadoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleado>>();
      const empleado = { id: 11214 };
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmpresa', () => {
      it('Should forward to empresaService', () => {
        const entity = { id: 13465 };
        const entity2 = { id: 16890 };
        jest.spyOn(empresaService, 'compareEmpresa');
        comp.compareEmpresa(entity, entity2);
        expect(empresaService.compareEmpresa).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
