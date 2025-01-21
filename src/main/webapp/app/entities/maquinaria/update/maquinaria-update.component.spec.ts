import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { MaquinariaService } from '../service/maquinaria.service';
import { IMaquinaria } from '../maquinaria.model';
import { MaquinariaFormService } from './maquinaria-form.service';

import { MaquinariaUpdateComponent } from './maquinaria-update.component';

describe('Maquinaria Management Update Component', () => {
  let comp: MaquinariaUpdateComponent;
  let fixture: ComponentFixture<MaquinariaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let maquinariaFormService: MaquinariaFormService;
  let maquinariaService: MaquinariaService;
  let empresaService: EmpresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaquinariaUpdateComponent],
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
      .overrideTemplate(MaquinariaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaquinariaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    maquinariaFormService = TestBed.inject(MaquinariaFormService);
    maquinariaService = TestBed.inject(MaquinariaService);
    empresaService = TestBed.inject(EmpresaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empresa query and add missing value', () => {
      const maquinaria: IMaquinaria = { id: 18639 };
      const empresa: IEmpresa = { id: 13465 };
      maquinaria.empresa = empresa;

      const empresaCollection: IEmpresa[] = [{ id: 13465 }];
      jest.spyOn(empresaService, 'query').mockReturnValue(of(new HttpResponse({ body: empresaCollection })));
      const additionalEmpresas = [empresa];
      const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
      jest.spyOn(empresaService, 'addEmpresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ maquinaria });
      comp.ngOnInit();

      expect(empresaService.query).toHaveBeenCalled();
      expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(
        empresaCollection,
        ...additionalEmpresas.map(expect.objectContaining),
      );
      expect(comp.empresasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const maquinaria: IMaquinaria = { id: 18639 };
      const empresa: IEmpresa = { id: 13465 };
      maquinaria.empresa = empresa;

      activatedRoute.data = of({ maquinaria });
      comp.ngOnInit();

      expect(comp.empresasSharedCollection).toContainEqual(empresa);
      expect(comp.maquinaria).toEqual(maquinaria);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaquinaria>>();
      const maquinaria = { id: 27800 };
      jest.spyOn(maquinariaFormService, 'getMaquinaria').mockReturnValue(maquinaria);
      jest.spyOn(maquinariaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maquinaria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maquinaria }));
      saveSubject.complete();

      // THEN
      expect(maquinariaFormService.getMaquinaria).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(maquinariaService.update).toHaveBeenCalledWith(expect.objectContaining(maquinaria));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaquinaria>>();
      const maquinaria = { id: 27800 };
      jest.spyOn(maquinariaFormService, 'getMaquinaria').mockReturnValue({ id: null });
      jest.spyOn(maquinariaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maquinaria: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maquinaria }));
      saveSubject.complete();

      // THEN
      expect(maquinariaFormService.getMaquinaria).toHaveBeenCalled();
      expect(maquinariaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaquinaria>>();
      const maquinaria = { id: 27800 };
      jest.spyOn(maquinariaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maquinaria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(maquinariaService.update).toHaveBeenCalled();
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
