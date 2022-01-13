import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentItem } from '../payment-item.model';

@Component({
  selector: 'jhi-payment-item-detail',
  templateUrl: './payment-item-detail.component.html',
})
export class PaymentItemDetailComponent implements OnInit {
  paymentItem: IPaymentItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentItem }) => {
      this.paymentItem = paymentItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
