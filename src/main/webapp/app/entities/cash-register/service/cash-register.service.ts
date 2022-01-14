import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICashRegister, getCashRegisterIdentifier } from '../cash-register.model';

export type EntityResponseType = HttpResponse<ICashRegister>;
export type EntityArrayResponseType = HttpResponse<ICashRegister[]>;

@Injectable({ providedIn: 'root' })
export class CashRegisterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cash-registers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cashRegister: ICashRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashRegister);
    return this.http
      .post<ICashRegister>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cashRegister: ICashRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashRegister);
    return this.http
      .put<ICashRegister>(`${this.resourceUrl}/${getCashRegisterIdentifier(cashRegister) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cashRegister: ICashRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cashRegister);
    return this.http
      .patch<ICashRegister>(`${this.resourceUrl}/${getCashRegisterIdentifier(cashRegister) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICashRegister>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICashRegister[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCashRegisterToCollectionIfMissing(
    cashRegisterCollection: ICashRegister[],
    ...cashRegistersToCheck: (ICashRegister | null | undefined)[]
  ): ICashRegister[] {
    const cashRegisters: ICashRegister[] = cashRegistersToCheck.filter(isPresent);
    if (cashRegisters.length > 0) {
      const cashRegisterCollectionIdentifiers = cashRegisterCollection.map(
        cashRegisterItem => getCashRegisterIdentifier(cashRegisterItem)!
      );
      const cashRegistersToAdd = cashRegisters.filter(cashRegisterItem => {
        const cashRegisterIdentifier = getCashRegisterIdentifier(cashRegisterItem);
        if (cashRegisterIdentifier == null || cashRegisterCollectionIdentifiers.includes(cashRegisterIdentifier)) {
          return false;
        }
        cashRegisterCollectionIdentifiers.push(cashRegisterIdentifier);
        return true;
      });
      return [...cashRegistersToAdd, ...cashRegisterCollection];
    }
    return cashRegisterCollection;
  }

  protected convertDateFromClient(cashRegister: ICashRegister): ICashRegister {
    return Object.assign({}, cashRegister, {
      created: cashRegister.created?.isValid() ? cashRegister.created.toJSON() : undefined,
      lastUpdate: cashRegister.lastUpdate?.isValid() ? cashRegister.lastUpdate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.lastUpdate = res.body.lastUpdate ? dayjs(res.body.lastUpdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((cashRegister: ICashRegister) => {
        cashRegister.created = cashRegister.created ? dayjs(cashRegister.created) : undefined;
        cashRegister.lastUpdate = cashRegister.lastUpdate ? dayjs(cashRegister.lastUpdate) : undefined;
      });
    }
    return res;
  }
}
