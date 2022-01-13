import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITerminal, getTerminalIdentifier } from '../terminal.model';

export type EntityResponseType = HttpResponse<ITerminal>;
export type EntityArrayResponseType = HttpResponse<ITerminal[]>;

@Injectable({ providedIn: 'root' })
export class TerminalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/terminals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(terminal: ITerminal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(terminal);
    return this.http
      .post<ITerminal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(terminal: ITerminal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(terminal);
    return this.http
      .put<ITerminal>(`${this.resourceUrl}/${getTerminalIdentifier(terminal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(terminal: ITerminal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(terminal);
    return this.http
      .patch<ITerminal>(`${this.resourceUrl}/${getTerminalIdentifier(terminal) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITerminal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITerminal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTerminalToCollectionIfMissing(terminalCollection: ITerminal[], ...terminalsToCheck: (ITerminal | null | undefined)[]): ITerminal[] {
    const terminals: ITerminal[] = terminalsToCheck.filter(isPresent);
    if (terminals.length > 0) {
      const terminalCollectionIdentifiers = terminalCollection.map(terminalItem => getTerminalIdentifier(terminalItem)!);
      const terminalsToAdd = terminals.filter(terminalItem => {
        const terminalIdentifier = getTerminalIdentifier(terminalItem);
        if (terminalIdentifier == null || terminalCollectionIdentifiers.includes(terminalIdentifier)) {
          return false;
        }
        terminalCollectionIdentifiers.push(terminalIdentifier);
        return true;
      });
      return [...terminalsToAdd, ...terminalCollection];
    }
    return terminalCollection;
  }

  protected convertDateFromClient(terminal: ITerminal): ITerminal {
    return Object.assign({}, terminal, {
      created: terminal.created?.isValid() ? terminal.created.toJSON() : undefined,
      payedToDate: terminal.payedToDate?.isValid() ? terminal.payedToDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.payedToDate = res.body.payedToDate ? dayjs(res.body.payedToDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((terminal: ITerminal) => {
        terminal.created = terminal.created ? dayjs(terminal.created) : undefined;
        terminal.payedToDate = terminal.payedToDate ? dayjs(terminal.payedToDate) : undefined;
      });
    }
    return res;
  }
}
