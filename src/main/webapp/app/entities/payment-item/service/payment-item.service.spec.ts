import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPaymentItem, PaymentItem } from '../payment-item.model';

import { PaymentItemService } from './payment-item.service';

describe('PaymentItem Service', () => {
  let service: PaymentItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IPaymentItem;
  let expectedResult: IPaymentItem | IPaymentItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaymentItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      created: currentDate,
      daysPayed: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          created: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PaymentItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          created: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
        },
        returnedFromService
      );

      service.create(new PaymentItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaymentItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          created: currentDate.format(DATE_TIME_FORMAT),
          daysPayed: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaymentItem', () => {
      const patchObject = Object.assign(
        {
          created: currentDate.format(DATE_TIME_FORMAT),
        },
        new PaymentItem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          created: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaymentItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          created: currentDate.format(DATE_TIME_FORMAT),
          daysPayed: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PaymentItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPaymentItemToCollectionIfMissing', () => {
      it('should add a PaymentItem to an empty array', () => {
        const paymentItem: IPaymentItem = { id: 123 };
        expectedResult = service.addPaymentItemToCollectionIfMissing([], paymentItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentItem);
      });

      it('should not add a PaymentItem to an array that contains it', () => {
        const paymentItem: IPaymentItem = { id: 123 };
        const paymentItemCollection: IPaymentItem[] = [
          {
            ...paymentItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addPaymentItemToCollectionIfMissing(paymentItemCollection, paymentItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaymentItem to an array that doesn't contain it", () => {
        const paymentItem: IPaymentItem = { id: 123 };
        const paymentItemCollection: IPaymentItem[] = [{ id: 456 }];
        expectedResult = service.addPaymentItemToCollectionIfMissing(paymentItemCollection, paymentItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentItem);
      });

      it('should add only unique PaymentItem to an array', () => {
        const paymentItemArray: IPaymentItem[] = [{ id: 123 }, { id: 456 }, { id: 20532 }];
        const paymentItemCollection: IPaymentItem[] = [{ id: 123 }];
        expectedResult = service.addPaymentItemToCollectionIfMissing(paymentItemCollection, ...paymentItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paymentItem: IPaymentItem = { id: 123 };
        const paymentItem2: IPaymentItem = { id: 456 };
        expectedResult = service.addPaymentItemToCollectionIfMissing([], paymentItem, paymentItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paymentItem);
        expect(expectedResult).toContain(paymentItem2);
      });

      it('should accept null and undefined values', () => {
        const paymentItem: IPaymentItem = { id: 123 };
        expectedResult = service.addPaymentItemToCollectionIfMissing([], null, paymentItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paymentItem);
      });

      it('should return initial array if no PaymentItem is added', () => {
        const paymentItemCollection: IPaymentItem[] = [{ id: 123 }];
        expectedResult = service.addPaymentItemToCollectionIfMissing(paymentItemCollection, undefined, null);
        expect(expectedResult).toEqual(paymentItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
