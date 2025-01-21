import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaquinariaObraDetailComponent } from './maquinaria-obra-detail.component';

describe('MaquinariaObra Management Detail Component', () => {
  let comp: MaquinariaObraDetailComponent;
  let fixture: ComponentFixture<MaquinariaObraDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaquinariaObraDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./maquinaria-obra-detail.component').then(m => m.MaquinariaObraDetailComponent),
              resolve: { maquinariaObra: () => of({ id: 28762 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MaquinariaObraDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MaquinariaObraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load maquinariaObra on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MaquinariaObraDetailComponent);

      // THEN
      expect(instance.maquinariaObra()).toEqual(expect.objectContaining({ id: 28762 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
