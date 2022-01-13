import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICompany, Company } from '../company.model';
import { CompanyService } from '../service/company.service';

@Component({
  selector: 'jhi-company-update',
  templateUrl: './company-update.component.html',
})
export class CompanyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    created: [null, [Validators.required]],
    nip: [],
    regon: [],
    street: [],
    city: [],
    postCode: [],
  });

  constructor(protected companyService: CompanyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ company }) => {
      if (company.id === undefined) {
        const today = dayjs().startOf('day');
        company.created = today;
      }

      this.updateForm(company);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const company = this.createFromForm();
    if (company.id !== undefined) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>): void {
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

  protected updateForm(company: ICompany): void {
    this.editForm.patchValue({
      id: company.id,
      created: company.created ? company.created.format(DATE_TIME_FORMAT) : null,
      nip: company.nip,
      regon: company.regon,
      street: company.street,
      city: company.city,
      postCode: company.postCode,
    });
  }

  protected createFromForm(): ICompany {
    return {
      ...new Company(),
      id: this.editForm.get(['id'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      nip: this.editForm.get(['nip'])!.value,
      regon: this.editForm.get(['regon'])!.value,
      street: this.editForm.get(['street'])!.value,
      city: this.editForm.get(['city'])!.value,
      postCode: this.editForm.get(['postCode'])!.value,
    };
  }
}
