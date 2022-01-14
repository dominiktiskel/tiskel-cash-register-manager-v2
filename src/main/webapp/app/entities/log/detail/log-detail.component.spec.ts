import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LogDetailComponent } from './log-detail.component';

describe('Log Management Detail Component', () => {
  let comp: LogDetailComponent;
  let fixture: ComponentFixture<LogDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LogDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ log: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LogDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load log on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.log).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
