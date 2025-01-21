import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { PerfilUsuarioService } from '../service/perfil-usuario.service';
import { IPerfilUsuario } from '../perfil-usuario.model';
import { PerfilUsuarioFormService } from './perfil-usuario-form.service';

import { PerfilUsuarioUpdateComponent } from './perfil-usuario-update.component';

describe('PerfilUsuario Management Update Component', () => {
  let comp: PerfilUsuarioUpdateComponent;
  let fixture: ComponentFixture<PerfilUsuarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let perfilUsuarioFormService: PerfilUsuarioFormService;
  let perfilUsuarioService: PerfilUsuarioService;
  let empresaService: EmpresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PerfilUsuarioUpdateComponent],
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
      .overrideTemplate(PerfilUsuarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PerfilUsuarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    perfilUsuarioFormService = TestBed.inject(PerfilUsuarioFormService);
    perfilUsuarioService = TestBed.inject(PerfilUsuarioService);
    empresaService = TestBed.inject(EmpresaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empresa query and add missing value', () => {
      const perfilUsuario: IPerfilUsuario = { id: 24446 };
      const empresa: IEmpresa = { id: 13465 };
      perfilUsuario.empresa = empresa;

      const empresaCollection: IEmpresa[] = [{ id: 13465 }];
      jest.spyOn(empresaService, 'query').mockReturnValue(of(new HttpResponse({ body: empresaCollection })));
      const additionalEmpresas = [empresa];
      const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
      jest.spyOn(empresaService, 'addEmpresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ perfilUsuario });
      comp.ngOnInit();

      expect(empresaService.query).toHaveBeenCalled();
      expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(
        empresaCollection,
        ...additionalEmpresas.map(expect.objectContaining),
      );
      expect(comp.empresasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const perfilUsuario: IPerfilUsuario = { id: 24446 };
      const empresa: IEmpresa = { id: 13465 };
      perfilUsuario.empresa = empresa;

      activatedRoute.data = of({ perfilUsuario });
      comp.ngOnInit();

      expect(comp.empresasSharedCollection).toContainEqual(empresa);
      expect(comp.perfilUsuario).toEqual(perfilUsuario);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfilUsuario>>();
      const perfilUsuario = { id: 7928 };
      jest.spyOn(perfilUsuarioFormService, 'getPerfilUsuario').mockReturnValue(perfilUsuario);
      jest.spyOn(perfilUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfilUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfilUsuario }));
      saveSubject.complete();

      // THEN
      expect(perfilUsuarioFormService.getPerfilUsuario).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(perfilUsuarioService.update).toHaveBeenCalledWith(expect.objectContaining(perfilUsuario));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfilUsuario>>();
      const perfilUsuario = { id: 7928 };
      jest.spyOn(perfilUsuarioFormService, 'getPerfilUsuario').mockReturnValue({ id: null });
      jest.spyOn(perfilUsuarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfilUsuario: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: perfilUsuario }));
      saveSubject.complete();

      // THEN
      expect(perfilUsuarioFormService.getPerfilUsuario).toHaveBeenCalled();
      expect(perfilUsuarioService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPerfilUsuario>>();
      const perfilUsuario = { id: 7928 };
      jest.spyOn(perfilUsuarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ perfilUsuario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(perfilUsuarioService.update).toHaveBeenCalled();
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
