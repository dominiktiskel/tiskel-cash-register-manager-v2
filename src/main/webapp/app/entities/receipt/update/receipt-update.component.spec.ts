import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReceiptService } from '../service/receipt.service';
import { IReceipt, Receipt } from '../receipt.model';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { ICashRegister } from 'app/entities/cash-register/cash-register.model';
import { CashRegisterService } from 'app/entities/cash-register/service/cash-register.service';
import { ITerminal } from 'app/entities/terminal/terminal.model';
import { TerminalService } from 'app/entities/terminal/service/terminal.service';

import { ReceiptUpdateComponent } from './receipt-update.component';

describe('Receipt Management Update Component', () => {
  let comp: ReceiptUpdateComponent;
  let fixture: ComponentFixture<ReceiptUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let receiptService: ReceiptService;
  let companyService: CompanyService;
  let cashRegisterService: CashRegisterService;
  let terminalService: TerminalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReceiptUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ReceiptUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReceiptUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    receiptService = TestBed.inject(ReceiptService);
    companyService = TestBed.inject(CompanyService);
    cashRegisterService = TestBed.inject(CashRegisterService);
    terminalService = TestBed.inject(TerminalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Company query and add missing value', () => {
      const receipt: IReceipt = { id: 456 };
      const company: ICompany = { id: 64388 };
      receipt.company = company;

      const companyCollection: ICompany[] = [{ id: 99067 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, ...additionalCompanies);
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CashRegister query and add missing value', () => {
      const receipt: IReceipt = { id: 456 };
      const cashRegister: ICashRegister = { id: 24350 };
      receipt.cashRegister = cashRegister;

      const cashRegisterCollection: ICashRegister[] = [{ id: 89723 }];
      jest.spyOn(cashRegisterService, 'query').mockReturnValue(of(new HttpResponse({ body: cashRegisterCollection })));
      const additionalCashRegisters = [cashRegister];
      const expectedCollection: ICashRegister[] = [...additionalCashRegisters, ...cashRegisterCollection];
      jest.spyOn(cashRegisterService, 'addCashRegisterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      expect(cashRegisterService.query).toHaveBeenCalled();
      expect(cashRegisterService.addCashRegisterToCollectionIfMissing).toHaveBeenCalledWith(
        cashRegisterCollection,
        ...additionalCashRegisters
      );
      expect(comp.cashRegistersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Terminal query and add missing value', () => {
      const receipt: IReceipt = { id: 456 };
      const terminal: ITerminal = { id: 9769 };
      receipt.terminal = terminal;

      const terminalCollection: ITerminal[] = [{ id: 15770 }];
      jest.spyOn(terminalService, 'query').mockReturnValue(of(new HttpResponse({ body: terminalCollection })));
      const additionalTerminals = [terminal];
      const expectedCollection: ITerminal[] = [...additionalTerminals, ...terminalCollection];
      jest.spyOn(terminalService, 'addTerminalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      expect(terminalService.query).toHaveBeenCalled();
      expect(terminalService.addTerminalToCollectionIfMissing).toHaveBeenCalledWith(terminalCollection, ...additionalTerminals);
      expect(comp.terminalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const receipt: IReceipt = { id: 456 };
      const company: ICompany = { id: 65912 };
      receipt.company = company;
      const cashRegister: ICashRegister = { id: 16943 };
      receipt.cashRegister = cashRegister;
      const terminal: ITerminal = { id: 58274 };
      receipt.terminal = terminal;

      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(receipt));
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.cashRegistersSharedCollection).toContain(cashRegister);
      expect(comp.terminalsSharedCollection).toContain(terminal);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Receipt>>();
      const receipt = { id: 123 };
      jest.spyOn(receiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: receipt }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(receiptService.update).toHaveBeenCalledWith(receipt);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Receipt>>();
      const receipt = new Receipt();
      jest.spyOn(receiptService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: receipt }));
      saveSubject.complete();

      // THEN
      expect(receiptService.create).toHaveBeenCalledWith(receipt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Receipt>>();
      const receipt = { id: 123 };
      jest.spyOn(receiptService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ receipt });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(receiptService.update).toHaveBeenCalledWith(receipt);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCompanyById', () => {
      it('Should return tracked Company primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCompanyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCashRegisterById', () => {
      it('Should return tracked CashRegister primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCashRegisterById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTerminalById', () => {
      it('Should return tracked Terminal primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTerminalById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
