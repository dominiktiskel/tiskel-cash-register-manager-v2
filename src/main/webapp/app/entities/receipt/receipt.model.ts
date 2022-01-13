import dayjs from 'dayjs/esm';
import { ICompany } from 'app/entities/company/company.model';
import { ICashRegister } from 'app/entities/cash-register/cash-register.model';
import { ITerminal } from 'app/entities/terminal/terminal.model';
import { ReceiptStatus } from 'app/entities/enumerations/receipt-status.model';
import { DeliveryType } from 'app/entities/enumerations/delivery-type.model';

export interface IReceipt {
  id?: number;
  status?: ReceiptStatus | null;
  created?: dayjs.Dayjs;
  orderId?: number | null;
  price?: number;
  tiskelCorporateId?: number | null;
  tiskelLicenseId?: number | null;
  taxiNumber?: number | null;
  driverId?: number | null;
  customerId?: number | null;
  customerPhoneNumber?: string | null;
  customerEmail?: string | null;
  emailSentStatus?: string | null;
  deliveryType?: DeliveryType | null;
  printerType?: string | null;
  jsonData?: string | null;
  printData?: string | null;
  jwtData?: string | null;
  errorMessage?: string | null;
  company?: ICompany;
  cashRegister?: ICashRegister | null;
  terminal?: ITerminal | null;
}

export class Receipt implements IReceipt {
  constructor(
    public id?: number,
    public status?: ReceiptStatus | null,
    public created?: dayjs.Dayjs,
    public orderId?: number | null,
    public price?: number,
    public tiskelCorporateId?: number | null,
    public tiskelLicenseId?: number | null,
    public taxiNumber?: number | null,
    public driverId?: number | null,
    public customerId?: number | null,
    public customerPhoneNumber?: string | null,
    public customerEmail?: string | null,
    public emailSentStatus?: string | null,
    public deliveryType?: DeliveryType | null,
    public printerType?: string | null,
    public jsonData?: string | null,
    public printData?: string | null,
    public jwtData?: string | null,
    public errorMessage?: string | null,
    public company?: ICompany,
    public cashRegister?: ICashRegister | null,
    public terminal?: ITerminal | null
  ) {}
}

export function getReceiptIdentifier(receipt: IReceipt): number | undefined {
  return receipt.id;
}
