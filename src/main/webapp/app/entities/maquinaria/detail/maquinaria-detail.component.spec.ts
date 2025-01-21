import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaquinariaDetailComponent } from './maquinaria-detail.component';

describe('Maquinaria Management Detail Component', () => {
  let comp: MaquinariaDetailComponent;
  let fixture: ComponentFixture<MaquinariaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaquinariaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./maquinaria-detail.component').then(m => m.MaquinariaDetailComponent),
              resolve: { maquinaria: () => of({ id: 27800 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MaquinariaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MaquinariaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load maquinaria on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MaquinariaDetailComponent);

      // THEN
      expect(instance.maquinaria()).toEqual(expect.objectContaining({ id: 27800 }));
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
