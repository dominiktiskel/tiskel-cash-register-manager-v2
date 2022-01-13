import dayjs from 'dayjs/esm';
import { ICashRegister } from 'app/entities/cash-register/cash-register.model';
import { IReceipt } from 'app/entities/receipt/receipt.model';
import { ITerminal } from 'app/entities/terminal/terminal.model';
import { IPayment } from 'app/entities/payment/payment.model';
import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface ICompany {
  id?: number;
  created?: dayjs.Dayjs;
  nip?: string | null;
  regon?: string | null;
  street?: string | null;
  city?: string | null;
  postCode?: string | null;
  cashRegisters?: ICashRegister[] | null;
  receipts?: IReceipt[] | null;
  terminals?: ITerminal[] | null;
  payments?: IPayment[] | null;
  invoices?: IInvoice[] | null;
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public created?: dayjs.Dayjs,
    public nip?: string | null,
    public regon?: string | null,
    public street?: string | null,
    public city?: string | null,
    public postCode?: string | null,
    public cashRegisters?: ICashRegister[] | null,
    public receipts?: IReceipt[] | null,
    public terminals?: ITerminal[] | null,
    public payments?: IPayment[] | null,
    public invoices?: IInvoice[] | null
  ) {}
}

export function getCompanyIdentifier(company: ICompany): number | undefined {
  return company.id;
}
