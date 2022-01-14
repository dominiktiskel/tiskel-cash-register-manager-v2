import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TerminalPayedByType } from 'app/entities/enumerations/terminal-payed-by-type.model';
import { TerminalSubscriptionPeriod } from 'app/entities/enumerations/terminal-subscription-period.model';
import { ITerminal, Terminal } from '../terminal.model';

import { TerminalService } from './terminal.service';

describe('Terminal Service', () => {
  let service: TerminalService;
  let httpMock: HttpTestingController;
  let elemDefault: ITerminal;
  let expectedResult: ITerminal | ITerminal[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TerminalService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      created: currentDate,
      payedBy: TerminalPayedByType.DRIVER,
      payedToDate: currentDate,
      number: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      subscriptionRenewalEnabled: false,
      subscriptionRenewalTrialCount: 0,
      subscriptionPeriod: TerminalSubscriptionPeriod.WEEK,
      active: false,
      errorMessage: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          created: currentDate.format(DATE_TIME_FORMAT),
          payedToDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Terminal', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          created: currentDate.format(DATE_TIME_FORMAT),
          payedToDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
          payedToDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Terminal()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Terminal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          created: currentDate.format(DATE_TIME_FORMAT),
          payedBy: 'BBBBBB',
          payedToDate: currentDate.format(DATE_TIME_FORMAT),
          number: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          subscriptionRenewalEnabled: true,
          subscriptionRenewalTrialCount: 1,
          subscriptionPeriod: 'BBBBBB',
          active: true,
          errorMessage: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
          payedToDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Terminal', () => {
      const patchObject = Object.assign(
        {
          created: currentDate.format(DATE_TIME_FORMAT),
          payedBy: 'BBBBBB',
          number: 1,
          subscriptionRenewalEnabled: true,
          subscriptionRenewalTrialCount: 1,
          subscriptionPeriod: 'BBBBBB',
          errorMessage: 'BBBBBB',
        },
        new Terminal()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          created: currentDate,
          payedToDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Terminal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          created: currentDate.format(DATE_TIME_FORMAT),
          payedBy: 'BBBBBB',
          payedToDate: currentDate.format(DATE_TIME_FORMAT),
          number: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          subscriptionRenewalEnabled: true,
          subscriptionRenewalTrialCount: 1,
          subscriptionPeriod: 'BBBBBB',
          active: true,
          errorMessage: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          created: currentDate,
          payedToDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Terminal', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTerminalToCollectionIfMissing', () => {
      it('should add a Terminal to an empty array', () => {
        const terminal: ITerminal = { id: 123 };
        expectedResult = service.addTerminalToCollectionIfMissing([], terminal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(terminal);
      });

      it('should not add a Terminal to an array that contains it', () => {
        const terminal: ITerminal = { id: 123 };
        const terminalCollection: ITerminal[] = [
          {
            ...terminal,
          },
          { id: 456 },
        ];
        expectedResult = service.addTerminalToCollectionIfMissing(terminalCollection, terminal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Terminal to an array that doesn't contain it", () => {
        const terminal: ITerminal = { id: 123 };
        const terminalCollection: ITerminal[] = [{ id: 456 }];
        expectedResult = service.addTerminalToCollectionIfMissing(terminalCollection, terminal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(terminal);
      });

      it('should add only unique Terminal to an array', () => {
        const terminalArray: ITerminal[] = [{ id: 123 }, { id: 456 }, { id: 33982 }];
        const terminalCollection: ITerminal[] = [{ id: 123 }];
        expectedResult = service.addTerminalToCollectionIfMissing(terminalCollection, ...terminalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const terminal: ITerminal = { id: 123 };
        const terminal2: ITerminal = { id: 456 };
        expectedResult = service.addTerminalToCollectionIfMissing([], terminal, terminal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(terminal);
        expect(expectedResult).toContain(terminal2);
      });

      it('should accept null and undefined values', () => {
        const terminal: ITerminal = { id: 123 };
        expectedResult = service.addTerminalToCollectionIfMissing([], null, terminal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(terminal);
      });

      it('should return initial array if no Terminal is added', () => {
        const terminalCollection: ITerminal[] = [{ id: 123 }];
        expectedResult = service.addTerminalToCollectionIfMissing(terminalCollection, undefined, null);
        expect(expectedResult).toEqual(terminalCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
