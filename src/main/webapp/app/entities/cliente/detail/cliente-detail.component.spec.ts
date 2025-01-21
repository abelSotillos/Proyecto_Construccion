import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClienteDetailComponent } from './cliente-detail.component';

describe('Cliente Management Detail Component', () => {
  let comp: ClienteDetailComponent;
  let fixture: ComponentFixture<ClienteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClienteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cliente-detail.component').then(m => m.ClienteDetailComponent),
              resolve: { cliente: () => of({ id: 13484 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClienteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClienteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cliente on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClienteDetailComponent);

      // THEN
      expect(instance.cliente()).toEqual(expect.objectContaining({ id: 13484 }));
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
