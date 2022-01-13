import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaymentItemComponent } from '../list/payment-item.component';
import { PaymentItemDetailComponent } from '../detail/payment-item-detail.component';
import { PaymentItemUpdateComponent } from '../update/payment-item-update.component';
import { PaymentItemRoutingResolveService } from './payment-item-routing-resolve.service';

const paymentItemRoute: Routes = [
  {
    path: '',
    component: PaymentItemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentItemDetailComponent,
    resolve: {
      paymentItem: PaymentItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentItemUpdateComponent,
    resolve: {
      paymentItem: PaymentItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentItemUpdateComponent,
    resolve: {
      paymentItem: PaymentItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paymentItemRoute)],
  exports: [RouterModule],
})
export class PaymentItemRoutingModule {}
