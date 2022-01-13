import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITerminal } from '../terminal.model';
import { TerminalService } from '../service/terminal.service';

@Component({
  templateUrl: './terminal-delete-dialog.component.html',
})
export class TerminalDeleteDialogComponent {
  terminal?: ITerminal;

  constructor(protected terminalService: TerminalService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.terminalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
