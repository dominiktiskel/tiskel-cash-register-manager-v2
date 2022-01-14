import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILog, getLogIdentifier } from '../log.model';

export type EntityResponseType = HttpResponse<ILog>;
export type EntityArrayResponseType = HttpResponse<ILog[]>;

@Injectable({ providedIn: 'root' })
export class LogService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/logs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(log: ILog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(log);
    return this.http
      .post<ILog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(log: ILog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(log);
    return this.http
      .put<ILog>(`${this.resourceUrl}/${getLogIdentifier(log) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(log: ILog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(log);
    return this.http
      .patch<ILog>(`${this.resourceUrl}/${getLogIdentifier(log) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLogToCollectionIfMissing(logCollection: ILog[], ...logsToCheck: (ILog | null | undefined)[]): ILog[] {
    const logs: ILog[] = logsToCheck.filter(isPresent);
    if (logs.length > 0) {
      const logCollectionIdentifiers = logCollection.map(logItem => getLogIdentifier(logItem)!);
      const logsToAdd = logs.filter(logItem => {
        const logIdentifier = getLogIdentifier(logItem);
        if (logIdentifier == null || logCollectionIdentifiers.includes(logIdentifier)) {
          return false;
        }
        logCollectionIdentifiers.push(logIdentifier);
        return true;
      });
      return [...logsToAdd, ...logCollection];
    }
    return logCollection;
  }

  protected convertDateFromClient(log: ILog): ILog {
    return Object.assign({}, log, {
      created: log.created?.isValid() ? log.created.toJSON() : undefined,
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
      res.body.forEach((log: ILog) => {
        log.created = log.created ? dayjs(log.created) : undefined;
      });
    }
    return res;
  }
}
