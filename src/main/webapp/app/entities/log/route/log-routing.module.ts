import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LogComponent } from '../list/log.component';
import { LogDetailComponent } from '../detail/log-detail.component';
import { LogUpdateComponent } from '../update/log-update.component';
import { LogRoutingResolveService } from './log-routing-resolve.service';

const logRoute: Routes = [
  {
    path: '',
    component: LogComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LogDetailComponent,
    resolve: {
      log: LogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LogUpdateComponent,
    resolve: {
      log: LogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LogUpdateComponent,
    resolve: {
      log: LogRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(logRoute)],
  exports: [RouterModule],
})
export class LogRoutingModule {}
