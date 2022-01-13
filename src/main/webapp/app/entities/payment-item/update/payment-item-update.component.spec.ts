import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaymentItemService } from '../service/payment-item.service';
import { IPaymentItem, PaymentItem } from '../payment-item.model';
import { ITerminal } from 'app/entities/terminal/terminal.model';
import { TerminalService } from 'app/entities/terminal/service/terminal.service';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';

import { PaymentItemUpdateComponent } from './payment-item-update.component';

describe('PaymentItem Management Update Component', () => {
  let comp: PaymentItemUpdateComponent;
  let fixture: ComponentFixture<PaymentItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paymentItemService: PaymentItemService;
  let terminalService: TerminalService;
  let paymentService: PaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PaymentItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PaymentItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentItemService = TestBed.inject(PaymentItemService);
    terminalService = TestBed.inject(TerminalService);
    paymentService = TestBed.inject(PaymentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Terminal query and add missing value', () => {
      const paymentItem: IPaymentItem = { id: 456 };
      const terminal: ITerminal = { id: 86614 };
      paymentItem.terminal = terminal;

      const terminalCollection: ITerminal[] = [{ id: 85035 }];
      jest.spyOn(terminalService, 'query').mockReturnValue(of(new HttpResponse({ body: terminalCollection })));
      const additionalTerminals = [terminal];
      const expectedCollection: ITerminal[] = [...additionalTerminals, ...terminalCollection];
      jest.spyOn(terminalService, 'addTerminalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentItem });
      comp.ngOnInit();

      expect(terminalService.query).toHaveBeenCalled();
      expect(terminalService.addTerminalToCollectionIfMissing).toHaveBeenCalledWith(terminalCollection, ...additionalTerminals);
      expect(comp.terminalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Payment query and add missing value', () => {
      const paymentItem: IPaymentItem = { id: 456 };
      const payment: IPayment = { id: 70965 };
      paymentItem.payment = payment;

      const paymentCollection: IPayment[] = [{ id: 54249 }];
      jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
      const additionalPayments = [payment];
      const expectedCollection: IPayment[] = [...additionalPayments, ...paymentCollection];
      jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paymentItem });
      comp.ngOnInit();

      expect(paymentService.query).toHaveBeenCalled();
      expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(paymentCollection, ...additionalPayments);
      expect(comp.paymentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paymentItem: IPaymentItem = { id: 456 };
      const terminal: ITerminal = { id: 8974 };
      paymentItem.terminal = terminal;
      const payment: IPayment = { id: 76172 };
      paymentItem.payment = payment;

      activatedRoute.data = of({ paymentItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(paymentItem));
      expect(comp.terminalsSharedCollection).toContain(terminal);
      expect(comp.paymentsSharedCollection).toContain(payment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PaymentItem>>();
      const paymentItem = { id: 123 };
      jest.spyOn(paymentItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentItemService.update).toHaveBeenCalledWith(paymentItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PaymentItem>>();
      const paymentItem = new PaymentItem();
      jest.spyOn(paymentItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentItem }));
      saveSubject.complete();

      // THEN
      expect(paymentItemService.create).toHaveBeenCalledWith(paymentItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PaymentItem>>();
      const paymentItem = { id: 123 };
      jest.spyOn(paymentItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentItemService.update).toHaveBeenCalledWith(paymentItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTerminalById', () => {
      it('Should return tracked Terminal primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTerminalById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPaymentById', () => {
      it('Should return tracked Payment primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPaymentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
