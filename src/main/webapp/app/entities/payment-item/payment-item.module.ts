import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaymentItemComponent } from './list/payment-item.component';
import { PaymentItemDetailComponent } from './detail/payment-item-detail.component';
import { PaymentItemUpdateComponent } from './update/payment-item-update.component';
import { PaymentItemDeleteDialogComponent } from './delete/payment-item-delete-dialog.component';
import { PaymentItemRoutingModule } from './route/payment-item-routing.module';

@NgModule({
  imports: [SharedModule, PaymentItemRoutingModule],
  declarations: [PaymentItemComponent, PaymentItemDetailComponent, PaymentItemUpdateComponent, PaymentItemDeleteDialogComponent],
  entryComponents: [PaymentItemDeleteDialogComponent],
})
export class PaymentItemModule {}
