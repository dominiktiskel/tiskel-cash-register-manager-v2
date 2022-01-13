import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentItem } from '../payment-item.model';
import { PaymentItemService } from '../service/payment-item.service';

@Component({
  templateUrl: './payment-item-delete-dialog.component.html',
})
export class PaymentItemDeleteDialogComponent {
  paymentItem?: IPaymentItem;

  constructor(protected paymentItemService: PaymentItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
