import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITerminal, Terminal } from '../terminal.model';
import { TerminalService } from '../service/terminal.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { TerminalPayedByType } from 'app/entities/enumerations/terminal-payed-by-type.model';
import { TerminalSubscriptionPeriod } from 'app/entities/enumerations/terminal-subscription-period.model';

@Component({
  selector: 'jhi-terminal-update',
  templateUrl: './terminal-update.component.html',
})
export class TerminalUpdateComponent implements OnInit {
  isSaving = false;
  terminalPayedByTypeValues = Object.keys(TerminalPayedByType);
  terminalSubscriptionPeriodValues = Object.keys(TerminalSubscriptionPeriod);

  companiesSharedCollection: ICompany[] = [];

  editForm = this.fb.group({
    id: [],
    created: [null, [Validators.required]],
    payedBy: [null, [Validators.required]],
    payedToDate: [null, [Validators.required]],
    number: [null, [Validators.required]],
    name: [],
    description: [],
    subscriptionRenewalEnabled: [null, [Validators.required]],
    subscriptionRenewalTrialCount: [null, [Validators.required]],
    subscriptionPeriod: [null, [Validators.required]],
    active: [null, [Validators.required]],
    errorMessage: [],
    company: [null, Validators.required],
  });

  constructor(
    protected terminalService: TerminalService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ terminal }) => {
      if (terminal.id === undefined) {
        const today = dayjs().startOf('day');
        terminal.created = today;
        terminal.payedToDate = today;
      }

      this.updateForm(terminal);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const terminal = this.createFromForm();
    if (terminal.id !== undefined) {
      this.subscribeToSaveResponse(this.terminalService.update(terminal));
    } else {
      this.subscribeToSaveResponse(this.terminalService.create(terminal));
    }
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITerminal>>): void {
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

  protected updateForm(terminal: ITerminal): void {
    this.editForm.patchValue({
      id: terminal.id,
      created: terminal.created ? terminal.created.format(DATE_TIME_FORMAT) : null,
      payedBy: terminal.payedBy,
      payedToDate: terminal.payedToDate ? terminal.payedToDate.format(DATE_TIME_FORMAT) : null,
      number: terminal.number,
      name: terminal.name,
      description: terminal.description,
      subscriptionRenewalEnabled: terminal.subscriptionRenewalEnabled,
      subscriptionRenewalTrialCount: terminal.subscriptionRenewalTrialCount,
      subscriptionPeriod: terminal.subscriptionPeriod,
      active: terminal.active,
      errorMessage: terminal.errorMessage,
      company: terminal.company,
    });

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(this.companiesSharedCollection, terminal.company);
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

  protected createFromForm(): ITerminal {
    return {
      ...new Terminal(),
      id: this.editForm.get(['id'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      payedBy: this.editForm.get(['payedBy'])!.value,
      payedToDate: this.editForm.get(['payedToDate'])!.value
        ? dayjs(this.editForm.get(['payedToDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      number: this.editForm.get(['number'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      subscriptionRenewalEnabled: this.editForm.get(['subscriptionRenewalEnabled'])!.value,
      subscriptionRenewalTrialCount: this.editForm.get(['subscriptionRenewalTrialCount'])!.value,
      subscriptionPeriod: this.editForm.get(['subscriptionPeriod'])!.value,
      active: this.editForm.get(['active'])!.value,
      errorMessage: this.editForm.get(['errorMessage'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }
}
