import dayjs from 'dayjs/esm';
import { IReceipt } from 'app/entities/receipt/receipt.model';
import { ICompany } from 'app/entities/company/company.model';

export interface ICashRegister {
  id?: number;
  created?: dayjs.Dayjs;
  lastUpdate?: dayjs.Dayjs | null;
  name?: string | null;
  description?: string | null;
  elzabId?: string | null;
  elzabLicense?: string | null;
  active?: boolean;
  errorMessage?: string | null;
  receipts?: IReceipt[] | null;
  company?: ICompany;
}

export class CashRegister implements ICashRegister {
  constructor(
    public id?: number,
    public created?: dayjs.Dayjs,
    public lastUpdate?: dayjs.Dayjs | null,
    public name?: string | null,
    public description?: string | null,
    public elzabId?: string | null,
    public elzabLicense?: string | null,
    public active?: boolean,
    public errorMessage?: string | null,
    public receipts?: IReceipt[] | null,
    public company?: ICompany
  ) {
    this.active = this.active ?? false;
  }
}

export function getCashRegisterIdentifier(cashRegister: ICashRegister): number | undefined {
  return cashRegister.id;
}
