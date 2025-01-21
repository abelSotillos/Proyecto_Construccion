import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ObraDetailComponent } from './obra-detail.component';

describe('Obra Management Detail Component', () => {
  let comp: ObraDetailComponent;
  let fixture: ComponentFixture<ObraDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObraDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./obra-detail.component').then(m => m.ObraDetailComponent),
              resolve: { obra: () => of({ id: 28688 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ObraDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ObraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load obra on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ObraDetailComponent);

      // THEN
      expect(instance.obra()).toEqual(expect.objectContaining({ id: 28688 }));
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
