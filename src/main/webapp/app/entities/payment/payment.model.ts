import dayjs from 'dayjs/esm';
import { IPaymentItem } from 'app/entities/payment-item/payment-item.model';
import { ICompany } from 'app/entities/company/company.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';

export interface IPayment {
  id?: number;
  created?: dayjs.Dayjs;
  status?: PaymentStatus;
  type?: PaymentType;
  isSubscriptionRenewal?: boolean;
  errorMessage?: string | null;
  paymentItems?: IPaymentItem[] | null;
  company?: ICompany;
  invoice?: IInvoice | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public created?: dayjs.Dayjs,
    public status?: PaymentStatus,
    public type?: PaymentType,
    public isSubscriptionRenewal?: boolean,
    public errorMessage?: string | null,
    public paymentItems?: IPaymentItem[] | null,
    public company?: ICompany,
    public invoice?: IInvoice | null
  ) {
    this.isSubscriptionRenewal = this.isSubscriptionRenewal ?? false;
  }
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
