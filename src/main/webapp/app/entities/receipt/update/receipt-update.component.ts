import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IReceipt, Receipt } from '../receipt.model';
import { ReceiptService } from '../service/receipt.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { ICashRegister } from 'app/entities/cash-register/cash-register.model';
import { CashRegisterService } from 'app/entities/cash-register/service/cash-register.service';
import { ITerminal } from 'app/entities/terminal/terminal.model';
import { TerminalService } from 'app/entities/terminal/service/terminal.service';
import { ReceiptStatus } from 'app/entities/enumerations/receipt-status.model';
import { DeliveryType } from 'app/entities/enumerations/delivery-type.model';

@Component({
  selector: 'jhi-receipt-update',
  templateUrl: './receipt-update.component.html',
})
export class ReceiptUpdateComponent implements OnInit {
  isSaving = false;
  receiptStatusValues = Object.keys(ReceiptStatus);
  deliveryTypeValues = Object.keys(DeliveryType);

  companiesSharedCollection: ICompany[] = [];
  cashRegistersSharedCollection: ICashRegister[] = [];
  terminalsSharedCollection: ITerminal[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    created: [null, [Validators.required]],
    orderId: [],
    price: [null, [Validators.required]],
    tiskelCorporateId: [],
    tiskelLicenseId: [],
    taxiNumber: [],
    driverId: [],
    customerId: [],
    customerPhoneNumber: [],
    customerEmail: [],
    emailSentStatus: [],
    deliveryType: [],
    printerType: [],
    jsonData: [],
    printData: [],
    jwtData: [],
    errorMessage: [],
    company: [null, Validators.required],
    cashRegister: [],
    terminal: [],
  });

  constructor(
    protected receiptService: ReceiptService,
    protected companyService: CompanyService,
    protected cashRegisterService: CashRegisterService,
    protected terminalService: TerminalService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ receipt }) => {
      if (receipt.id === undefined) {
        const today = dayjs().startOf('day');
        receipt.created = today;
      }

      this.updateForm(receipt);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const receipt = this.createFromForm();
    if (receipt.id !== undefined) {
      this.subscribeToSaveResponse(this.receiptService.update(receipt));
    } else {
      this.subscribeToSaveResponse(this.receiptService.create(receipt));
    }
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  trackCashRegisterById(index: number, item: ICashRegister): number {
    return item.id!;
  }

  trackTerminalById(index: number, item: ITerminal): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReceipt>>): void {
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

  protected updateForm(receipt: IReceipt): void {
    this.editForm.patchValue({
      id: receipt.id,
      status: receipt.status,
      created: receipt.created ? receipt.created.format(DATE_TIME_FORMAT) : null,
      orderId: receipt.orderId,
      price: receipt.price,
      tiskelCorporateId: receipt.tiskelCorporateId,
      tiskelLicenseId: receipt.tiskelLicenseId,
      taxiNumber: receipt.taxiNumber,
      driverId: receipt.driverId,
      customerId: receipt.customerId,
      customerPhoneNumber: receipt.customerPhoneNumber,
      customerEmail: receipt.customerEmail,
      emailSentStatus: receipt.emailSentStatus,
      deliveryType: receipt.deliveryType,
      printerType: receipt.printerType,
      jsonData: receipt.jsonData,
      printData: receipt.printData,
      jwtData: receipt.jwtData,
      errorMessage: receipt.errorMessage,
      company: receipt.company,
      cashRegister: receipt.cashRegister,
      terminal: receipt.terminal,
    });

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(this.companiesSharedCollection, receipt.company);
    this.cashRegistersSharedCollection = this.cashRegisterService.addCashRegisterToCollectionIfMissing(
      this.cashRegistersSharedCollection,
      receipt.cashRegister
    );
    this.terminalsSharedCollection = this.terminalService.addTerminalToCollectionIfMissing(
      this.terminalsSharedCollection,
      receipt.terminal
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.cashRegisterService
      .query()
      .pipe(map((res: HttpResponse<ICashRegister[]>) => res.body ?? []))
      .pipe(
        map((cashRegisters: ICashRegister[]) =>
          this.cashRegisterService.addCashRegisterToCollectionIfMissing(cashRegisters, this.editForm.get('cashRegister')!.value)
        )
      )
      .subscribe((cashRegisters: ICashRegister[]) => (this.cashRegistersSharedCollection = cashRegisters));

    this.terminalService
      .query()
      .pipe(map((res: HttpResponse<ITerminal[]>) => res.body ?? []))
      .pipe(
        map((terminals: ITerminal[]) =>
          this.terminalService.addTerminalToCollectionIfMissing(terminals, this.editForm.get('terminal')!.value)
        )
      )
      .subscribe((terminals: ITerminal[]) => (this.terminalsSharedCollection = terminals));
  }

  protected createFromForm(): IReceipt {
    return {
      ...new Receipt(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      orderId: this.editForm.get(['orderId'])!.value,
      price: this.editForm.get(['price'])!.value,
      tiskelCorporateId: this.editForm.get(['tiskelCorporateId'])!.value,
      tiskelLicenseId: this.editForm.get(['tiskelLicenseId'])!.value,
      taxiNumber: this.editForm.get(['taxiNumber'])!.value,
      driverId: this.editForm.get(['driverId'])!.value,
      customerId: this.editForm.get(['customerId'])!.value,
      customerPhoneNumber: this.editForm.get(['customerPhoneNumber'])!.value,
      customerEmail: this.editForm.get(['customerEmail'])!.value,
      emailSentStatus: this.editForm.get(['emailSentStatus'])!.value,
      deliveryType: this.editForm.get(['deliveryType'])!.value,
      printerType: this.editForm.get(['printerType'])!.value,
      jsonData: this.editForm.get(['jsonData'])!.value,
      printData: this.editForm.get(['printData'])!.value,
      jwtData: this.editForm.get(['jwtData'])!.value,
      errorMessage: this.editForm.get(['errorMessage'])!.value,
      company: this.editForm.get(['company'])!.value,
      cashRegister: this.editForm.get(['cashRegister'])!.value,
      terminal: this.editForm.get(['terminal'])!.value,
    };
  }
}
