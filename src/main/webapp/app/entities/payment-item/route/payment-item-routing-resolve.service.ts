import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaymentItem, PaymentItem } from '../payment-item.model';
import { PaymentItemService } from '../service/payment-item.service';

@Injectable({ providedIn: 'root' })
export class PaymentItemRoutingResolveService implements Resolve<IPaymentItem> {
  constructor(protected service: PaymentItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((paymentItem: HttpResponse<PaymentItem>) => {
          if (paymentItem.body) {
            return of(paymentItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentItem());
  }
}
