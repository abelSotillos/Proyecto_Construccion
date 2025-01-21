import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EmpleadoObraDetailComponent } from './empleado-obra-detail.component';

describe('EmpleadoObra Management Detail Component', () => {
  let comp: EmpleadoObraDetailComponent;
  let fixture: ComponentFixture<EmpleadoObraDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpleadoObraDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./empleado-obra-detail.component').then(m => m.EmpleadoObraDetailComponent),
              resolve: { empleadoObra: () => of({ id: 14224 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EmpleadoObraDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmpleadoObraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load empleadoObra on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EmpleadoObraDetailComponent);

      // THEN
      expect(instance.empleadoObra()).toEqual(expect.objectContaining({ id: 14224 }));
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
