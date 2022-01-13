import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInvoice, Invoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;

  companiesSharedCollection: ICompany[] = [];

  editForm = this.fb.group({
    id: [],
    created: [null, [Validators.required]],
    payedDate: [null, [Validators.required]],
    periodBeginDate: [null, [Validators.required]],
    periodEndDate: [null, [Validators.required]],
    number: [null, [Validators.required]],
    jsonData: [null, [Validators.required]],
    company: [null, Validators.required],
  });

  constructor(
    protected invoiceService: InvoiceService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      if (invoice.id === undefined) {
        const today = dayjs().startOf('day');
        invoice.created = today;
        invoice.payedDate = today;
        invoice.periodBeginDate = today;
        invoice.periodEndDate = today;
      }

      this.updateForm(invoice);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.editForm.patchValue({
      id: invoice.id,
      created: invoice.created ? invoice.created.format(DATE_TIME_FORMAT) : null,
      payedDate: invoice.payedDate ? invoice.payedDate.format(DATE_TIME_FORMAT) : null,
      periodBeginDate: invoice.periodBeginDate ? invoice.periodBeginDate.format(DATE_TIME_FORMAT) : null,
      periodEndDate: invoice.periodEndDate ? invoice.periodEndDate.format(DATE_TIME_FORMAT) : null,
      number: invoice.number,
      jsonData: invoice.jsonData,
      company: invoice.company,
    });

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(this.companiesSharedCollection, invoice.company);
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }

  protected createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      payedDate: this.editForm.get(['payedDate'])!.value ? dayjs(this.editForm.get(['payedDate'])!.value, DATE_TIME_FORMAT) : undefined,
      periodBeginDate: this.editForm.get(['periodBeginDate'])!.value
        ? dayjs(this.editForm.get(['periodBeginDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      periodEndDate: this.editForm.get(['periodEndDate'])!.value
        ? dayjs(this.editForm.get(['periodEndDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      number: this.editForm.get(['number'])!.value,
      jsonData: this.editForm.get(['jsonData'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }
}
