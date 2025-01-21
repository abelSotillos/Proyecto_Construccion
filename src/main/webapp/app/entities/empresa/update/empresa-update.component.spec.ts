import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { EmpresaService } from '../service/empresa.service';
import { IEmpresa } from '../empresa.model';
import { EmpresaFormService } from './empresa-form.service';

import { EmpresaUpdateComponent } from './empresa-update.component';

describe('Empresa Management Update Component', () => {
  let comp: EmpresaUpdateComponent;
  let fixture: ComponentFixture<EmpresaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empresaFormService: EmpresaFormService;
  let empresaService: EmpresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmpresaUpdateComponent],
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
      .overrideTemplate(EmpresaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpresaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empresaFormService = TestBed.inject(EmpresaFormService);
    empresaService = TestBed.inject(EmpresaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const empresa: IEmpresa = { id: 16890 };

      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      expect(comp.empresa).toEqual(empresa);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpresa>>();
      const empresa = { id: 13465 };
      jest.spyOn(empresaFormService, 'getEmpresa').mockReturnValue(empresa);
      jest.spyOn(empresaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empresa }));
      saveSubject.complete();

      // THEN
      expect(empresaFormService.getEmpresa).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(empresaService.update).toHaveBeenCalledWith(expect.objectContaining(empresa));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpresa>>();
      const empresa = { id: 13465 };
      jest.spyOn(empresaFormService, 'getEmpresa').mockReturnValue({ id: null });
      jest.spyOn(empresaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empresa: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empresa }));
      saveSubject.complete();

      // THEN
      expect(empresaFormService.getEmpresa).toHaveBeenCalled();
      expect(empresaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpresa>>();
      const empresa = { id: 13465 };
      jest.spyOn(empresaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empresaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
