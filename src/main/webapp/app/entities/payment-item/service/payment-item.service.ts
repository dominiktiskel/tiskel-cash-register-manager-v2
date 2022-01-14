import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaymentItem, getPaymentItemIdentifier } from '../payment-item.model';

export type EntityResponseType = HttpResponse<IPaymentItem>;
export type EntityArrayResponseType = HttpResponse<IPaymentItem[]>;

@Injectable({ providedIn: 'root' })
export class PaymentItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentItem: IPaymentItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentItem);
    return this.http
      .post<IPaymentItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paymentItem: IPaymentItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentItem);
    return this.http
      .put<IPaymentItem>(`${this.resourceUrl}/${getPaymentItemIdentifier(paymentItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(paymentItem: IPaymentItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentItem);
    return this.http
      .patch<IPaymentItem>(`${this.resourceUrl}/${getPaymentItemIdentifier(paymentItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPaymentItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPaymentItemToCollectionIfMissing(
    paymentItemCollection: IPaymentItem[],
    ...paymentItemsToCheck: (IPaymentItem | null | undefined)[]
  ): IPaymentItem[] {
    const paymentItems: IPaymentItem[] = paymentItemsToCheck.filter(isPresent);
    if (paymentItems.length > 0) {
      const paymentItemCollectionIdentifiers = paymentItemCollection.map(paymentItemItem => getPaymentItemIdentifier(paymentItemItem)!);
      const paymentItemsToAdd = paymentItems.filter(paymentItemItem => {
        const paymentItemIdentifier = getPaymentItemIdentifier(paymentItemItem);
        if (paymentItemIdentifier == null || paymentItemCollectionIdentifiers.includes(paymentItemIdentifier)) {
          return false;
        }
        paymentItemCollectionIdentifiers.push(paymentItemIdentifier);
        return true;
      });
      return [...paymentItemsToAdd, ...paymentItemCollection];
    }
    return paymentItemCollection;
  }

  protected convertDateFromClient(paymentItem: IPaymentItem): IPaymentItem {
    return Object.assign({}, paymentItem, {
      created: paymentItem.created?.isValid() ? paymentItem.created.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((paymentItem: IPaymentItem) => {
        paymentItem.created = paymentItem.created ? dayjs(paymentItem.created) : undefined;
      });
    }
    return res;
  }
}
