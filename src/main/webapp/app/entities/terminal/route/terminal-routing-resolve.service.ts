import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITerminal, Terminal } from '../terminal.model';
import { TerminalService } from '../service/terminal.service';

@Injectable({ providedIn: 'root' })
export class TerminalRoutingResolveService implements Resolve<ITerminal> {
  constructor(protected service: TerminalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITerminal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((terminal: HttpResponse<Terminal>) => {
          if (terminal.body) {
            return of(terminal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Terminal());
  }
}
