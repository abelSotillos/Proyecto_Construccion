import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IObra } from 'app/entities/obra/obra.model';
import { ObraService } from 'app/entities/obra/service/obra.service';
import { IMaquinaria } from 'app/entities/maquinaria/maquinaria.model';
import { MaquinariaService } from 'app/entities/maquinaria/service/maquinaria.service';
import { IMaquinariaObra } from '../maquinaria-obra.model';
import { MaquinariaObraService } from '../service/maquinaria-obra.service';
import { MaquinariaObraFormService } from './maquinaria-obra-form.service';

import { MaquinariaObraUpdateComponent } from './maquinaria-obra-update.component';

describe('MaquinariaObra Management Update Component', () => {
  let comp: MaquinariaObraUpdateComponent;
  let fixture: ComponentFixture<MaquinariaObraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let maquinariaObraFormService: MaquinariaObraFormService;
  let maquinariaObraService: MaquinariaObraService;
  let obraService: ObraService;
  let maquinariaService: MaquinariaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaquinariaObraUpdateComponent],
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
      .overrideTemplate(MaquinariaObraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaquinariaObraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    maquinariaObraFormService = TestBed.inject(MaquinariaObraFormService);
    maquinariaObraService = TestBed.inject(MaquinariaObraService);
    obraService = TestBed.inject(ObraService);
    maquinariaService = TestBed.inject(MaquinariaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Obra query and add missing value', () => {
      const maquinariaObra: IMaquinariaObra = { id: 1536 };
      const obra: IObra = { id: 28688 };
      maquinariaObra.obra = obra;

      const obraCollection: IObra[] = [{ id: 28688 }];
      jest.spyOn(obraService, 'query').mockReturnValue(of(new HttpResponse({ body: obraCollection })));
      const additionalObras = [obra];
      const expectedCollection: IObra[] = [...additionalObras, ...obraCollection];
      jest.spyOn(obraService, 'addObraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ maquinariaObra });
      comp.ngOnInit();

      expect(obraService.query).toHaveBeenCalled();
      expect(obraService.addObraToCollectionIfMissing).toHaveBeenCalledWith(
        obraCollection,
        ...additionalObras.map(expect.objectContaining),
      );
      expect(comp.obrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Maquinaria query and add missing value', () => {
      const maquinariaObra: IMaquinariaObra = { id: 1536 };
      const maquinaria: IMaquinaria = { id: 27800 };
      maquinariaObra.maquinaria = maquinaria;

      const maquinariaCollection: IMaquinaria[] = [{ id: 27800 }];
      jest.spyOn(maquinariaService, 'query').mockReturnValue(of(new HttpResponse({ body: maquinariaCollection })));
      const additionalMaquinarias = [maquinaria];
      const expectedCollection: IMaquinaria[] = [...additionalMaquinarias, ...maquinariaCollection];
      jest.spyOn(maquinariaService, 'addMaquinariaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ maquinariaObra });
      comp.ngOnInit();

      expect(maquinariaService.query).toHaveBeenCalled();
      expect(maquinariaService.addMaquinariaToCollectionIfMissing).toHaveBeenCalledWith(
        maquinariaCollection,
        ...additionalMaquinarias.map(expect.objectContaining),
      );
      expect(comp.maquinariasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const maquinariaObra: IMaquinariaObra = { id: 1536 };
      const obra: IObra = { id: 28688 };
      maquinariaObra.obra = obra;
      const maquinaria: IMaquinaria = { id: 27800 };
      maquinariaObra.maquinaria = maquinaria;

      activatedRoute.data = of({ maquinariaObra });
      comp.ngOnInit();

      expect(comp.obrasSharedCollection).toContainEqual(obra);
      expect(comp.maquinariasSharedCollection).toContainEqual(maquinaria);
      expect(comp.maquinariaObra).toEqual(maquinariaObra);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaquinariaObra>>();
      const maquinariaObra = { id: 28762 };
      jest.spyOn(maquinariaObraFormService, 'getMaquinariaObra').mockReturnValue(maquinariaObra);
      jest.spyOn(maquinariaObraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maquinariaObra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maquinariaObra }));
      saveSubject.complete();

      // THEN
      expect(maquinariaObraFormService.getMaquinariaObra).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(maquinariaObraService.update).toHaveBeenCalledWith(expect.objectContaining(maquinariaObra));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaquinariaObra>>();
      const maquinariaObra = { id: 28762 };
      jest.spyOn(maquinariaObraFormService, 'getMaquinariaObra').mockReturnValue({ id: null });
      jest.spyOn(maquinariaObraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maquinariaObra: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maquinariaObra }));
      saveSubject.complete();

      // THEN
      expect(maquinariaObraFormService.getMaquinariaObra).toHaveBeenCalled();
      expect(maquinariaObraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaquinariaObra>>();
      const maquinariaObra = { id: 28762 };
      jest.spyOn(maquinariaObraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maquinariaObra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(maquinariaObraService.update).toHaveBeenCalled();
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

    describe('compareMaquinaria', () => {
      it('Should forward to maquinariaService', () => {
        const entity = { id: 27800 };
        const entity2 = { id: 18639 };
        jest.spyOn(maquinariaService, 'compareMaquinaria');
        comp.compareMaquinaria(entity, entity2);
        expect(maquinariaService.compareMaquinaria).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
