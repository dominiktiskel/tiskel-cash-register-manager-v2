import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILog, Log } from '../log.model';
import { LogService } from '../service/log.service';

@Injectable({ providedIn: 'root' })
export class LogRoutingResolveService implements Resolve<ILog> {
  constructor(protected service: LogService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILog> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((log: HttpResponse<Log>) => {
          if (log.body) {
            return of(log.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Log());
  }
}
