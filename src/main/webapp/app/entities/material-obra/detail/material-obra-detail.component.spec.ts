import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaterialObraDetailComponent } from './material-obra-detail.component';

describe('MaterialObra Management Detail Component', () => {
  let comp: MaterialObraDetailComponent;
  let fixture: ComponentFixture<MaterialObraDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaterialObraDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./material-obra-detail.component').then(m => m.MaterialObraDetailComponent),
              resolve: { materialObra: () => of({ id: 15274 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MaterialObraDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MaterialObraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load materialObra on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MaterialObraDetailComponent);

      // THEN
      expect(instance.materialObra()).toEqual(expect.objectContaining({ id: 15274 }));
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
