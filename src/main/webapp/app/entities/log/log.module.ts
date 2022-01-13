import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LogComponent } from './list/log.component';
import { LogDetailComponent } from './detail/log-detail.component';
import { LogUpdateComponent } from './update/log-update.component';
import { LogDeleteDialogComponent } from './delete/log-delete-dialog.component';
import { LogRoutingModule } from './route/log-routing.module';

@NgModule({
  imports: [SharedModule, LogRoutingModule],
  declarations: [LogComponent, LogDetailComponent, LogUpdateComponent, LogDeleteDialogComponent],
  entryComponents: [LogDeleteDialogComponent],
})
export class LogModule {}
