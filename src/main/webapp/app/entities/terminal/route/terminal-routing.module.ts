import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TerminalComponent } from '../list/terminal.component';
import { TerminalDetailComponent } from '../detail/terminal-detail.component';
import { TerminalUpdateComponent } from '../update/terminal-update.component';
import { TerminalRoutingResolveService } from './terminal-routing-resolve.service';

const terminalRoute: Routes = [
  {
    path: '',
    component: TerminalComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TerminalDetailComponent,
    resolve: {
      terminal: TerminalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TerminalUpdateComponent,
    resolve: {
      terminal: TerminalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TerminalUpdateComponent,
    resolve: {
      terminal: TerminalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(terminalRoute)],
  exports: [RouterModule],
})
export class TerminalRoutingModule {}
