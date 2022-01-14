import dayjs from 'dayjs/esm';
import { IReceipt } from 'app/entities/receipt/receipt.model';
import { IPaymentItem } from 'app/entities/payment-item/payment-item.model';
import { ICompany } from 'app/entities/company/company.model';
import { TerminalPayedByType } from 'app/entities/enumerations/terminal-payed-by-type.model';
import { TerminalSubscriptionPeriod } from 'app/entities/enumerations/terminal-subscription-period.model';

export interface ITerminal {
  id?: number;
  created?: dayjs.Dayjs;
  payedBy?: TerminalPayedByType;
  payedToDate?: dayjs.Dayjs;
  number?: number;
  name?: string | null;
  description?: string | null;
  subscriptionRenewalEnabled?: boolean;
  subscriptionRenewalTrialCount?: number;
  subscriptionPeriod?: TerminalSubscriptionPeriod;
  active?: boolean;
  errorMessage?: string | null;
  receipts?: IReceipt[] | null;
  paymentItems?: IPaymentItem[] | null;
  company?: ICompany;
}

export class Terminal implements ITerminal {
  constructor(
    public id?: number,
    public created?: dayjs.Dayjs,
    public payedBy?: TerminalPayedByType,
    public payedToDate?: dayjs.Dayjs,
    public number?: number,
    public name?: string | null,
    public description?: string | null,
    public subscriptionRenewalEnabled?: boolean,
    public subscriptionRenewalTrialCount?: number,
    public subscriptionPeriod?: TerminalSubscriptionPeriod,
    public active?: boolean,
    public errorMessage?: string | null,
    public receipts?: IReceipt[] | null,
    public paymentItems?: IPaymentItem[] | null,
    public company?: ICompany
  ) {
    this.subscriptionRenewalEnabled = this.subscriptionRenewalEnabled ?? false;
    this.active = this.active ?? false;
  }
}

export function getTerminalIdentifier(terminal: ITerminal): number | undefined {
  return terminal.id;
}
