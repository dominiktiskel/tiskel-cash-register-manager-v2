import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITerminal } from '../terminal.model';

@Component({
  selector: 'jhi-terminal-detail',
  templateUrl: './terminal-detail.component.html',
})
export class TerminalDetailComponent implements OnInit {
  terminal: ITerminal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ terminal }) => {
      this.terminal = terminal;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
