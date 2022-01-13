import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TerminalComponent } from './list/terminal.component';
import { TerminalDetailComponent } from './detail/terminal-detail.component';
import { TerminalUpdateComponent } from './update/terminal-update.component';
import { TerminalDeleteDialogComponent } from './delete/terminal-delete-dialog.component';
import { TerminalRoutingModule } from './route/terminal-routing.module';

@NgModule({
  imports: [SharedModule, TerminalRoutingModule],
  declarations: [TerminalComponent, TerminalDetailComponent, TerminalUpdateComponent, TerminalDeleteDialogComponent],
  entryComponents: [TerminalDeleteDialogComponent],
})
export class TerminalModule {}
