import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPaymentItem, PaymentItem } from '../payment-item.model';
import { PaymentItemService } from '../service/payment-item.service';
import { ITerminal } from 'app/entities/terminal/terminal.model';
import { TerminalService } from 'app/entities/terminal/service/terminal.service';
import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';

@Component({
  selector: 'jhi-payment-item-update',
  templateUrl: './payment-item-update.component.html',
})
export class PaymentItemUpdateComponent implements OnInit {
  isSaving = false;

  terminalsSharedCollection: ITerminal[] = [];
  paymentsSharedCollection: IPayment[] = [];

  editForm = this.fb.group({
    id: [],
    created: [null, [Validators.required]],
    daysPayed: [null, [Validators.required]],
    terminal: [null, Validators.required],
    payment: [null, Validators.required],
  });

  constructor(
    protected paymentItemService: PaymentItemService,
    protected terminalService: TerminalService,
    protected paymentService: PaymentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentItem }) => {
      if (paymentItem.id === undefined) {
        const today = dayjs().startOf('day');
        paymentItem.created = today;
      }

      this.updateForm(paymentItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentItem = this.createFromForm();
    if (paymentItem.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentItemService.update(paymentItem));
    } else {
      this.subscribeToSaveResponse(this.paymentItemService.create(paymentItem));
    }
  }

  trackTerminalById(index: number, item: ITerminal): number {
    return item.id!;
  }

  trackPaymentById(index: number, item: IPayment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(paymentItem: IPaymentItem): void {
    this.editForm.patchValue({
      id: paymentItem.id,
      created: paymentItem.created ? paymentItem.created.format(DATE_TIME_FORMAT) : null,
      daysPayed: paymentItem.daysPayed,
      terminal: paymentItem.terminal,
      payment: paymentItem.payment,
    });

    this.terminalsSharedCollection = this.terminalService.addTerminalToCollectionIfMissing(
      this.terminalsSharedCollection,
      paymentItem.terminal
    );
    this.paymentsSharedCollection = this.paymentService.addPaymentToCollectionIfMissing(this.paymentsSharedCollection, paymentItem.payment);
  }

  protected loadRelationshipsOptions(): void {
    this.terminalService
      .query()
      .pipe(map((res: HttpResponse<ITerminal[]>) => res.body ?? []))
      .pipe(
        map((terminals: ITerminal[]) =>
          this.terminalService.addTerminalToCollectionIfMissing(terminals, this.editForm.get('terminal')!.value)
        )
      )
      .subscribe((terminals: ITerminal[]) => (this.terminalsSharedCollection = terminals));

    this.paymentService
      .query()
      .pipe(map((res: HttpResponse<IPayment[]>) => res.body ?? []))
      .pipe(
        map((payments: IPayment[]) => this.paymentService.addPaymentToCollectionIfMissing(payments, this.editForm.get('payment')!.value))
      )
      .subscribe((payments: IPayment[]) => (this.paymentsSharedCollection = payments));
  }

  protected createFromForm(): IPaymentItem {
    return {
      ...new PaymentItem(),
      id: this.editForm.get(['id'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      daysPayed: this.editForm.get(['daysPayed'])!.value,
      terminal: this.editForm.get(['terminal'])!.value,
      payment: this.editForm.get(['payment'])!.value,
    };
  }
}
