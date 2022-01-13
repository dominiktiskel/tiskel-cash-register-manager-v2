import dayjs from 'dayjs/esm';
import { ITerminal } from 'app/entities/terminal/terminal.model';
import { IPayment } from 'app/entities/payment/payment.model';

export interface IPaymentItem {
  id?: number;
  created?: dayjs.Dayjs;
  daysPayed?: number;
  terminal?: ITerminal;
  payment?: IPayment;
}

export class PaymentItem implements IPaymentItem {
  constructor(
    public id?: number,
    public created?: dayjs.Dayjs,
    public daysPayed?: number,
    public terminal?: ITerminal,
    public payment?: IPayment
  ) {}
}

export function getPaymentItemIdentifier(paymentItem: IPaymentItem): number | undefined {
  return paymentItem.id;
}
