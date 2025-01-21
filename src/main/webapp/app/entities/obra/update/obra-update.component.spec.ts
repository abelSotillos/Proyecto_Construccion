import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmpresa } from 'app/entities/empresa/empresa.model';
import { EmpresaService } from 'app/entities/empresa/service/empresa.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IObra } from '../obra.model';
import { ObraService } from '../service/obra.service';
import { ObraFormService } from './obra-form.service';

import { ObraUpdateComponent } from './obra-update.component';

describe('Obra Management Update Component', () => {
  let comp: ObraUpdateComponent;
  let fixture: ComponentFixture<ObraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let obraFormService: ObraFormService;
  let obraService: ObraService;
  let empresaService: EmpresaService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ObraUpdateComponent],
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
      .overrideTemplate(ObraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ObraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    obraFormService = TestBed.inject(ObraFormService);
    obraService = TestBed.inject(ObraService);
    empresaService = TestBed.inject(EmpresaService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empresa query and add missing value', () => {
      const obra: IObra = { id: 11447 };
      const empresa: IEmpresa = { id: 13465 };
      obra.empresa = empresa;

      const empresaCollection: IEmpresa[] = [{ id: 13465 }];
      jest.spyOn(empresaService, 'query').mockReturnValue(of(new HttpResponse({ body: empresaCollection })));
      const additionalEmpresas = [empresa];
      const expectedCollection: IEmpresa[] = [...additionalEmpresas, ...empresaCollection];
      jest.spyOn(empresaService, 'addEmpresaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ obra });
      comp.ngOnInit();

      expect(empresaService.query).toHaveBeenCalled();
      expect(empresaService.addEmpresaToCollectionIfMissing).toHaveBeenCalledWith(
        empresaCollection,
        ...additionalEmpresas.map(expect.objectContaining),
      );
      expect(comp.empresasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cliente query and add missing value', () => {
      const obra: IObra = { id: 11447 };
      const cliente: ICliente = { id: 13484 };
      obra.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 13484 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ obra });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const obra: IObra = { id: 11447 };
      const empresa: IEmpresa = { id: 13465 };
      obra.empresa = empresa;
      const cliente: ICliente = { id: 13484 };
      obra.cliente = cliente;

      activatedRoute.data = of({ obra });
      comp.ngOnInit();

      expect(comp.empresasSharedCollection).toContainEqual(empresa);
      expect(comp.clientesSharedCollection).toContainEqual(cliente);
      expect(comp.obra).toEqual(obra);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IObra>>();
      const obra = { id: 28688 };
      jest.spyOn(obraFormService, 'getObra').mockReturnValue(obra);
      jest.spyOn(obraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ obra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: obra }));
      saveSubject.complete();

      // THEN
      expect(obraFormService.getObra).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(obraService.update).toHaveBeenCalledWith(expect.objectContaining(obra));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IObra>>();
      const obra = { id: 28688 };
      jest.spyOn(obraFormService, 'getObra').mockReturnValue({ id: null });
      jest.spyOn(obraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ obra: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: obra }));
      saveSubject.complete();

      // THEN
      expect(obraFormService.getObra).toHaveBeenCalled();
      expect(obraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IObra>>();
      const obra = { id: 28688 };
      jest.spyOn(obraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ obra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(obraService.update).toHaveBeenCalled();
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

    describe('compareCliente', () => {
      it('Should forward to clienteService', () => {
        const entity = { id: 13484 };
        const entity2 = { id: 20795 };
        jest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
