import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoice, getInvoiceIdentifier } from '../invoice.model';

export type EntityResponseType = HttpResponse<IInvoice>;
export type EntityArrayResponseType = HttpResponse<IInvoice[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .post<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .put<IInvoice>(`${this.resourceUrl}/${getInvoiceIdentifier(invoice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .patch<IInvoice>(`${this.resourceUrl}/${getInvoiceIdentifier(invoice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvoiceToCollectionIfMissing(invoiceCollection: IInvoice[], ...invoicesToCheck: (IInvoice | null | undefined)[]): IInvoice[] {
    const invoices: IInvoice[] = invoicesToCheck.filter(isPresent);
    if (invoices.length > 0) {
      const invoiceCollectionIdentifiers = invoiceCollection.map(invoiceItem => getInvoiceIdentifier(invoiceItem)!);
      const invoicesToAdd = invoices.filter(invoiceItem => {
        const invoiceIdentifier = getInvoiceIdentifier(invoiceItem);
        if (invoiceIdentifier == null || invoiceCollectionIdentifiers.includes(invoiceIdentifier)) {
          return false;
        }
        invoiceCollectionIdentifiers.push(invoiceIdentifier);
        return true;
      });
      return [...invoicesToAdd, ...invoiceCollection];
    }
    return invoiceCollection;
  }

  protected convertDateFromClient(invoice: IInvoice): IInvoice {
    return Object.assign({}, invoice, {
      created: invoice.created?.isValid() ? invoice.created.toJSON() : undefined,
      payedDate: invoice.payedDate?.isValid() ? invoice.payedDate.toJSON() : undefined,
      periodBeginDate: invoice.periodBeginDate?.isValid() ? invoice.periodBeginDate.toJSON() : undefined,
      periodEndDate: invoice.periodEndDate?.isValid() ? invoice.periodEndDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.payedDate = res.body.payedDate ? dayjs(res.body.payedDate) : undefined;
      res.body.periodBeginDate = res.body.periodBeginDate ? dayjs(res.body.periodBeginDate) : undefined;
      res.body.periodEndDate = res.body.periodEndDate ? dayjs(res.body.periodEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((invoice: IInvoice) => {
        invoice.created = invoice.created ? dayjs(invoice.created) : undefined;
        invoice.payedDate = invoice.payedDate ? dayjs(invoice.payedDate) : undefined;
        invoice.periodBeginDate = invoice.periodBeginDate ? dayjs(invoice.periodBeginDate) : undefined;
        invoice.periodEndDate = invoice.periodEndDate ? dayjs(invoice.periodEndDate) : undefined;
      });
    }
    return res;
  }
}
