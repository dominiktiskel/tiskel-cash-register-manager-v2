import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILog } from '../log.model';

@Component({
  selector: 'jhi-log-detail',
  templateUrl: './log-detail.component.html',
})
export class LogDetailComponent implements OnInit {
  log: ILog | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ log }) => {
      this.log = log;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
