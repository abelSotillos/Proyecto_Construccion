import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IObra } from 'app/entities/obra/obra.model';
import { ObraService } from 'app/entities/obra/service/obra.service';
import { IMaterial } from 'app/entities/material/material.model';
import { MaterialService } from 'app/entities/material/service/material.service';
import { IMaterialObra } from '../material-obra.model';
import { MaterialObraService } from '../service/material-obra.service';
import { MaterialObraFormService } from './material-obra-form.service';

import { MaterialObraUpdateComponent } from './material-obra-update.component';

describe('MaterialObra Management Update Component', () => {
  let comp: MaterialObraUpdateComponent;
  let fixture: ComponentFixture<MaterialObraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialObraFormService: MaterialObraFormService;
  let materialObraService: MaterialObraService;
  let obraService: ObraService;
  let materialService: MaterialService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MaterialObraUpdateComponent],
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
      .overrideTemplate(MaterialObraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialObraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialObraFormService = TestBed.inject(MaterialObraFormService);
    materialObraService = TestBed.inject(MaterialObraService);
    obraService = TestBed.inject(ObraService);
    materialService = TestBed.inject(MaterialService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Obra query and add missing value', () => {
      const materialObra: IMaterialObra = { id: 27281 };
      const obra: IObra = { id: 28688 };
      materialObra.obra = obra;

      const obraCollection: IObra[] = [{ id: 28688 }];
      jest.spyOn(obraService, 'query').mockReturnValue(of(new HttpResponse({ body: obraCollection })));
      const additionalObras = [obra];
      const expectedCollection: IObra[] = [...additionalObras, ...obraCollection];
      jest.spyOn(obraService, 'addObraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialObra });
      comp.ngOnInit();

      expect(obraService.query).toHaveBeenCalled();
      expect(obraService.addObraToCollectionIfMissing).toHaveBeenCalledWith(
        obraCollection,
        ...additionalObras.map(expect.objectContaining),
      );
      expect(comp.obrasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Material query and add missing value', () => {
      const materialObra: IMaterialObra = { id: 27281 };
      const material: IMaterial = { id: 10021 };
      materialObra.material = material;

      const materialCollection: IMaterial[] = [{ id: 10021 }];
      jest.spyOn(materialService, 'query').mockReturnValue(of(new HttpResponse({ body: materialCollection })));
      const additionalMaterials = [material];
      const expectedCollection: IMaterial[] = [...additionalMaterials, ...materialCollection];
      jest.spyOn(materialService, 'addMaterialToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ materialObra });
      comp.ngOnInit();

      expect(materialService.query).toHaveBeenCalled();
      expect(materialService.addMaterialToCollectionIfMissing).toHaveBeenCalledWith(
        materialCollection,
        ...additionalMaterials.map(expect.objectContaining),
      );
      expect(comp.materialsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const materialObra: IMaterialObra = { id: 27281 };
      const obra: IObra = { id: 28688 };
      materialObra.obra = obra;
      const material: IMaterial = { id: 10021 };
      materialObra.material = material;

      activatedRoute.data = of({ materialObra });
      comp.ngOnInit();

      expect(comp.obrasSharedCollection).toContainEqual(obra);
      expect(comp.materialsSharedCollection).toContainEqual(material);
      expect(comp.materialObra).toEqual(materialObra);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialObra>>();
      const materialObra = { id: 15274 };
      jest.spyOn(materialObraFormService, 'getMaterialObra').mockReturnValue(materialObra);
      jest.spyOn(materialObraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialObra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialObra }));
      saveSubject.complete();

      // THEN
      expect(materialObraFormService.getMaterialObra).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialObraService.update).toHaveBeenCalledWith(expect.objectContaining(materialObra));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialObra>>();
      const materialObra = { id: 15274 };
      jest.spyOn(materialObraFormService, 'getMaterialObra').mockReturnValue({ id: null });
      jest.spyOn(materialObraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialObra: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materialObra }));
      saveSubject.complete();

      // THEN
      expect(materialObraFormService.getMaterialObra).toHaveBeenCalled();
      expect(materialObraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaterialObra>>();
      const materialObra = { id: 15274 };
      jest.spyOn(materialObraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materialObra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialObraService.update).toHaveBeenCalled();
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

    describe('compareMaterial', () => {
      it('Should forward to materialService', () => {
        const entity = { id: 10021 };
        const entity2 = { id: 3829 };
        jest.spyOn(materialService, 'compareMaterial');
        comp.compareMaterial(entity, entity2);
        expect(materialService.compareMaterial).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
