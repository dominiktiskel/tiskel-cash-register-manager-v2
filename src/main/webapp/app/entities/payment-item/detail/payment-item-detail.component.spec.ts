import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentItemDetailComponent } from './payment-item-detail.component';

describe('PaymentItem Management Detail Component', () => {
  let comp: PaymentItemDetailComponent;
  let fixture: ComponentFixture<PaymentItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ paymentItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaymentItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaymentItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load paymentItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.paymentItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
