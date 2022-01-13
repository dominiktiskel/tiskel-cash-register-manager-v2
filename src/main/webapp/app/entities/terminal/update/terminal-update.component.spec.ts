import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TerminalService } from '../service/terminal.service';
import { ITerminal, Terminal } from '../terminal.model';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

import { TerminalUpdateComponent } from './terminal-update.component';

describe('Terminal Management Update Component', () => {
  let comp: TerminalUpdateComponent;
  let fixture: ComponentFixture<TerminalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let terminalService: TerminalService;
  let companyService: CompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TerminalUpdateComponent],
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
      .overrideTemplate(TerminalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TerminalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    terminalService = TestBed.inject(TerminalService);
    companyService = TestBed.inject(CompanyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Company query and add missing value', () => {
      const terminal: ITerminal = { id: 456 };
      const company: ICompany = { id: 15095 };
      terminal.company = company;

      const companyCollection: ICompany[] = [{ id: 70791 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ terminal });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(companyCollection, ...additionalCompanies);
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const terminal: ITerminal = { id: 456 };
      const company: ICompany = { id: 35645 };
      terminal.company = company;

      activatedRoute.data = of({ terminal });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(terminal));
      expect(comp.companiesSharedCollection).toContain(company);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Terminal>>();
      const terminal = { id: 123 };
      jest.spyOn(terminalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: terminal }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(terminalService.update).toHaveBeenCalledWith(terminal);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Terminal>>();
      const terminal = new Terminal();
      jest.spyOn(terminalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: terminal }));
      saveSubject.complete();

      // THEN
      expect(terminalService.create).toHaveBeenCalledWith(terminal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Terminal>>();
      const terminal = { id: 123 };
      jest.spyOn(terminalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ terminal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(terminalService.update).toHaveBeenCalledWith(terminal);
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
  });
});
