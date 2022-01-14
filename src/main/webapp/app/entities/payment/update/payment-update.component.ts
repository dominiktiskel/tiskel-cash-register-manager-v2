import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPayment, Payment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  paymentStatusValues = Object.keys(PaymentStatus);
  paymentTypeValues = Object.keys(PaymentType);

  companiesSharedCollection: ICompany[] = [];
  invoicesSharedCollection: IInvoice[] = [];

  editForm = this.fb.group({
    id: [],
    created: [null, [Validators.required]],
    status: [null, [Validators.required]],
    type: [null, [Validators.required]],
    isSubscriptionRenewal: [null, [Validators.required]],
    errorMessage: [],
    company: [null, Validators.required],
    invoice: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected companyService: CompanyService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      if (payment.id === undefined) {
        const today = dayjs().startOf('day');
        payment.created = today;
      }

      this.updateForm(payment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  trackInvoiceById(index: number, item: IInvoice): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  protected updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      created: payment.created ? payment.created.format(DATE_TIME_FORMAT) : null,
      status: payment.status,
      type: payment.type,
      isSubscriptionRenewal: payment.isSubscriptionRenewal,
      errorMessage: payment.errorMessage,
      company: payment.company,
      invoice: payment.invoice,
    });

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(this.companiesSharedCollection, payment.company);
    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing(this.invoicesSharedCollection, payment.invoice);
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) => this.invoiceService.addInvoiceToCollectionIfMissing(invoices, this.editForm.get('invoice')!.value))
      )
      .subscribe((invoices: IInvoice[]) => (this.invoicesSharedCollection = invoices));
  }

  protected createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status'])!.value,
      type: this.editForm.get(['type'])!.value,
      isSubscriptionRenewal: this.editForm.get(['isSubscriptionRenewal'])!.value,
      errorMessage: this.editForm.get(['errorMessage'])!.value,
      company: this.editForm.get(['company'])!.value,
      invoice: this.editForm.get(['invoice'])!.value,
    };
  }
}
