import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILog, Log } from '../log.model';
import { LogService } from '../service/log.service';
import { LogType } from 'app/entities/enumerations/log-type.model';

@Component({
  selector: 'jhi-log-update',
  templateUrl: './log-update.component.html',
})
export class LogUpdateComponent implements OnInit {
  isSaving = false;
  logTypeValues = Object.keys(LogType);

  editForm = this.fb.group({
    id: [],
    created: [null, [Validators.required]],
    type: [null, [Validators.required]],
    message: [],
    data: [],
  });

  constructor(protected logService: LogService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ log }) => {
      if (log.id === undefined) {
        const today = dayjs().startOf('day');
        log.created = today;
      }

      this.updateForm(log);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const log = this.createFromForm();
    if (log.id !== undefined) {
      this.subscribeToSaveResponse(this.logService.update(log));
    } else {
      this.subscribeToSaveResponse(this.logService.create(log));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILog>>): void {
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

  protected updateForm(log: ILog): void {
    this.editForm.patchValue({
      id: log.id,
      created: log.created ? log.created.format(DATE_TIME_FORMAT) : null,
      type: log.type,
      message: log.message,
      data: log.data,
    });
  }

  protected createFromForm(): ILog {
    return {
      ...new Log(),
      id: this.editForm.get(['id'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      type: this.editForm.get(['type'])!.value,
      message: this.editForm.get(['message'])!.value,
      data: this.editForm.get(['data'])!.value,
    };
  }
}
