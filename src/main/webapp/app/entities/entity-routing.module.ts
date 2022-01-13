import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'receipt',
        data: { pageTitle: 'tiskelCashRegisterManagerApp.receipt.home.title' },
        loadChildren: () => import('./receipt/receipt.module').then(m => m.ReceiptModule),
      },
      {
        path: 'company',
        data: { pageTitle: 'tiskelCashRegisterManagerApp.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'cash-register',
        data: { pageTitle: 'tiskelCashRegisterManagerApp.cashRegister.home.title' },
        loadChildren: () => import('./cash-register/cash-register.module').then(m => m.CashRegisterModule),
      },
      {
        path: 'terminal',
        data: { pageTitle: 'tiskelCashRegisterManagerApp.terminal.home.title' },
        loadChildren: () => import('./terminal/terminal.module').then(m => m.TerminalModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'tiskelCashRegisterManagerApp.payment.home.title' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'payment-item',
        data: { pageTitle: 'tiskelCashRegisterManagerApp.paymentItem.home.title' },
        loadChildren: () => import('./payment-item/payment-item.module').then(m => m.PaymentItemModule),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'tiskelCashRegisterManagerApp.invoice.home.title' },
        loadChildren: () => import('./invoice/invoice.module').then(m => m.InvoiceModule),
      },
      {
        path: 'setting',
        data: { pageTitle: 'tiskelCashRegisterManagerApp.setting.home.title' },
        loadChildren: () => import('./setting/setting.module').then(m => m.SettingModule),
      },
      {
        path: 'log',
        data: { pageTitle: 'tiskelCashRegisterManagerApp.log.home.title' },
        loadChildren: () => import('./log/log.module').then(m => m.LogModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
