import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { LogType } from 'app/entities/enumerations/log-type.model';
import { ILog, Log } from '../log.model';

import { LogService } from './log.service';

describe('Log Service', () => {
  let service: LogService;
  let httpMock: HttpTestingController;
  let elemDefault: ILog;
  let expectedResult: ILog | ILog[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LogService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      created: currentDate,
      type: LogType.DEBUG,
      message: 'AAAAAAA',
      data: 'AAAAAAA',
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

    it('should create a Log', () => {
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

      service.create(new Log()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Log', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          created: currentDate.format(DATE_TIME_FORMAT),
          type: 'BBBBBB',
          message: 'BBBBBB',
          data: 'BBBBBB',
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

    it('should partial update a Log', () => {
      const patchObject = Object.assign(
        {
          created: currentDate.format(DATE_TIME_FORMAT),
          type: 'BBBBBB',
          message: 'BBBBBB',
        },
        new Log()
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

    it('should return a list of Log', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          created: currentDate.format(DATE_TIME_FORMAT),
          type: 'BBBBBB',
          message: 'BBBBBB',
          data: 'BBBBBB',
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

    it('should delete a Log', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLogToCollectionIfMissing', () => {
      it('should add a Log to an empty array', () => {
        const log: ILog = { id: 123 };
        expectedResult = service.addLogToCollectionIfMissing([], log);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(log);
      });

      it('should not add a Log to an array that contains it', () => {
        const log: ILog = { id: 123 };
        const logCollection: ILog[] = [
          {
            ...log,
          },
          { id: 456 },
        ];
        expectedResult = service.addLogToCollectionIfMissing(logCollection, log);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Log to an array that doesn't contain it", () => {
        const log: ILog = { id: 123 };
        const logCollection: ILog[] = [{ id: 456 }];
        expectedResult = service.addLogToCollectionIfMissing(logCollection, log);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(log);
      });

      it('should add only unique Log to an array', () => {
        const logArray: ILog[] = [{ id: 123 }, { id: 456 }, { id: 68191 }];
        const logCollection: ILog[] = [{ id: 123 }];
        expectedResult = service.addLogToCollectionIfMissing(logCollection, ...logArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const log: ILog = { id: 123 };
        const log2: ILog = { id: 456 };
        expectedResult = service.addLogToCollectionIfMissing([], log, log2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(log);
        expect(expectedResult).toContain(log2);
      });

      it('should accept null and undefined values', () => {
        const log: ILog = { id: 123 };
        expectedResult = service.addLogToCollectionIfMissing([], null, log, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(log);
      });

      it('should return initial array if no Log is added', () => {
        const logCollection: ILog[] = [{ id: 123 }];
        expectedResult = service.addLogToCollectionIfMissing(logCollection, undefined, null);
        expect(expectedResult).toEqual(logCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
