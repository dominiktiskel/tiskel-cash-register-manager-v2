import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TerminalDetailComponent } from './terminal-detail.component';

describe('Terminal Management Detail Component', () => {
  let comp: TerminalDetailComponent;
  let fixture: ComponentFixture<TerminalDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TerminalDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ terminal: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TerminalDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TerminalDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load terminal on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.terminal).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
