import dayjs from 'dayjs/esm';
import { IPayment } from 'app/entities/payment/payment.model';
import { ICompany } from 'app/entities/company/company.model';

export interface IInvoice {
  id?: number;
  created?: dayjs.Dayjs;
  payedDate?: dayjs.Dayjs;
  periodBeginDate?: dayjs.Dayjs;
  periodEndDate?: dayjs.Dayjs;
  number?: string;
  jsonData?: string;
  payments?: IPayment[] | null;
  company?: ICompany;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public created?: dayjs.Dayjs,
    public payedDate?: dayjs.Dayjs,
    public periodBeginDate?: dayjs.Dayjs,
    public periodEndDate?: dayjs.Dayjs,
    public number?: string,
    public jsonData?: string,
    public payments?: IPayment[] | null,
    public company?: ICompany
  ) {}
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
