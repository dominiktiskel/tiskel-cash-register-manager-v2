import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICashRegister, CashRegister } from '../cash-register.model';
import { CashRegisterService } from '../service/cash-register.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

@Component({
  selector: 'jhi-cash-register-update',
  templateUrl: './cash-register-update.component.html',
})
export class CashRegisterUpdateComponent implements OnInit {
  isSaving = false;

  companiesSharedCollection: ICompany[] = [];

  editForm = this.fb.group({
    id: [],
    created: [null, [Validators.required]],
    lastUpdate: [],
    name: [],
    description: [],
    elzabId: [],
    elzabLicense: [],
    active: [null, [Validators.required]],
    errorMessage: [],
    company: [null, Validators.required],
  });

  constructor(
    protected cashRegisterService: CashRegisterService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cashRegister }) => {
      if (cashRegister.id === undefined) {
        const today = dayjs().startOf('day');
        cashRegister.created = today;
        cashRegister.lastUpdate = today;
      }

      this.updateForm(cashRegister);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cashRegister = this.createFromForm();
    if (cashRegister.id !== undefined) {
      this.subscribeToSaveResponse(this.cashRegisterService.update(cashRegister));
    } else {
      this.subscribeToSaveResponse(this.cashRegisterService.create(cashRegister));
    }
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICashRegister>>): void {
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

  protected updateForm(cashRegister: ICashRegister): void {
    this.editForm.patchValue({
      id: cashRegister.id,
      created: cashRegister.created ? cashRegister.created.format(DATE_TIME_FORMAT) : null,
      lastUpdate: cashRegister.lastUpdate ? cashRegister.lastUpdate.format(DATE_TIME_FORMAT) : null,
      name: cashRegister.name,
      description: cashRegister.description,
      elzabId: cashRegister.elzabId,
      elzabLicense: cashRegister.elzabLicense,
      active: cashRegister.active,
      errorMessage: cashRegister.errorMessage,
      company: cashRegister.company,
    });

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(
      this.companiesSharedCollection,
      cashRegister.company
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
  }

  protected createFromForm(): ICashRegister {
    return {
      ...new CashRegister(),
      id: this.editForm.get(['id'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdate: this.editForm.get(['lastUpdate'])!.value ? dayjs(this.editForm.get(['lastUpdate'])!.value, DATE_TIME_FORMAT) : undefined,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      elzabId: this.editForm.get(['elzabId'])!.value,
      elzabLicense: this.editForm.get(['elzabLicense'])!.value,
      active: this.editForm.get(['active'])!.value,
      errorMessage: this.editForm.get(['errorMessage'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }
}
