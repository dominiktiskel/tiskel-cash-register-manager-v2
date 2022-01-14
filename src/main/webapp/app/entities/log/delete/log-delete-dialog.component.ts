import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILog } from '../log.model';
import { LogService } from '../service/log.service';

@Component({
  templateUrl: './log-delete-dialog.component.html',
})
export class LogDeleteDialogComponent {
  log?: ILog;

  constructor(protected logService: LogService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.logService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
