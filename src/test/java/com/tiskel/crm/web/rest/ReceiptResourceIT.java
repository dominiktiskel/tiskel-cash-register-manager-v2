package com.tiskel.crm.web.rest;

import static com.tiskel.crm.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tiskel.crm.IntegrationTest;
import com.tiskel.crm.domain.Company;
import com.tiskel.crm.domain.Receipt;
import com.tiskel.crm.domain.enumeration.DeliveryType;
import com.tiskel.crm.domain.enumeration.ReceiptStatus;
import com.tiskel.crm.repository.ReceiptRepository;
import com.tiskel.crm.service.dto.ReceiptDTO;
import com.tiskel.crm.service.mapper.ReceiptMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReceiptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReceiptResourceIT {

    private static final ReceiptStatus DEFAULT_STATUS = ReceiptStatus.NEW;
    private static final ReceiptStatus UPDATED_STATUS = ReceiptStatus.IN_PROGRESS;

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_ORDER_ID = 1;
    private static final Integer UPDATED_ORDER_ID = 2;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final Integer DEFAULT_TISKEL_CORPORATE_ID = 1;
    private static final Integer UPDATED_TISKEL_CORPORATE_ID = 2;

    private static final Integer DEFAULT_TISKEL_LICENSE_ID = 1;
    private static final Integer UPDATED_TISKEL_LICENSE_ID = 2;

    private static final Integer DEFAULT_TAXI_NUMBER = 1;
    private static final Integer UPDATED_TAXI_NUMBER = 2;

    private static final Integer DEFAULT_DRIVER_ID = 1;
    private static final Integer UPDATED_DRIVER_ID = 2;

    private static final Integer DEFAULT_CUSTOMER_ID = 1;
    private static final Integer UPDATED_CUSTOMER_ID = 2;

    private static final String DEFAULT_CUSTOMER_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_SENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_SENT_STATUS = "BBBBBBBBBB";

    private static final DeliveryType DEFAULT_DELIVERY_TYPE = DeliveryType.PAPER;
    private static final DeliveryType UPDATED_DELIVERY_TYPE = DeliveryType.EMAIL;

    private static final String DEFAULT_PRINTER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PRINTER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_JSON_DATA = "AAAAAAAAAA";
    private static final String UPDATED_JSON_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_PRINT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_PRINT_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_JWT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_JWT_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ReceiptMapper receiptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceiptMockMvc;

    private Receipt receipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createEntity(EntityManager em) {
        Receipt receipt = new Receipt()
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED)
            .orderId(DEFAULT_ORDER_ID)
            .price(DEFAULT_PRICE)
            .tiskelCorporateId(DEFAULT_TISKEL_CORPORATE_ID)
            .tiskelLicenseId(DEFAULT_TISKEL_LICENSE_ID)
            .taxiNumber(DEFAULT_TAXI_NUMBER)
            .driverId(DEFAULT_DRIVER_ID)
            .customerId(DEFAULT_CUSTOMER_ID)
            .customerPhoneNumber(DEFAULT_CUSTOMER_PHONE_NUMBER)
            .customerEmail(DEFAULT_CUSTOMER_EMAIL)
            .emailSentStatus(DEFAULT_EMAIL_SENT_STATUS)
            .deliveryType(DEFAULT_DELIVERY_TYPE)
            .printerType(DEFAULT_PRINTER_TYPE)
            .jsonData(DEFAULT_JSON_DATA)
            .printData(DEFAULT_PRINT_DATA)
            .jwtData(DEFAULT_JWT_DATA)
            .errorMessage(DEFAULT_ERROR_MESSAGE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        receipt.setCompany(company);
        return receipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createUpdatedEntity(EntityManager em) {
        Receipt receipt = new Receipt()
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .orderId(UPDATED_ORDER_ID)
            .price(UPDATED_PRICE)
            .tiskelCorporateId(UPDATED_TISKEL_CORPORATE_ID)
            .tiskelLicenseId(UPDATED_TISKEL_LICENSE_ID)
            .taxiNumber(UPDATED_TAXI_NUMBER)
            .driverId(UPDATED_DRIVER_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerPhoneNumber(UPDATED_CUSTOMER_PHONE_NUMBER)
            .customerEmail(UPDATED_CUSTOMER_EMAIL)
            .emailSentStatus(UPDATED_EMAIL_SENT_STATUS)
            .deliveryType(UPDATED_DELIVERY_TYPE)
            .printerType(UPDATED_PRINTER_TYPE)
            .jsonData(UPDATED_JSON_DATA)
            .printData(UPDATED_PRINT_DATA)
            .jwtData(UPDATED_JWT_DATA)
            .errorMessage(UPDATED_ERROR_MESSAGE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        receipt.setCompany(company);
        return receipt;
    }

    @BeforeEach
    public void initTest() {
        receipt = createEntity(em);
    }

    @Test
    @Transactional
    void createReceipt() throws Exception {
        int databaseSizeBeforeCreate = receiptRepository.findAll().size();
        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);
        restReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isCreated());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeCreate + 1);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReceipt.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testReceipt.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testReceipt.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testReceipt.getTiskelCorporateId()).isEqualTo(DEFAULT_TISKEL_CORPORATE_ID);
        assertThat(testReceipt.getTiskelLicenseId()).isEqualTo(DEFAULT_TISKEL_LICENSE_ID);
        assertThat(testReceipt.getTaxiNumber()).isEqualTo(DEFAULT_TAXI_NUMBER);
        assertThat(testReceipt.getDriverId()).isEqualTo(DEFAULT_DRIVER_ID);
        assertThat(testReceipt.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testReceipt.getCustomerPhoneNumber()).isEqualTo(DEFAULT_CUSTOMER_PHONE_NUMBER);
        assertThat(testReceipt.getCustomerEmail()).isEqualTo(DEFAULT_CUSTOMER_EMAIL);
        assertThat(testReceipt.getEmailSentStatus()).isEqualTo(DEFAULT_EMAIL_SENT_STATUS);
        assertThat(testReceipt.getDeliveryType()).isEqualTo(DEFAULT_DELIVERY_TYPE);
        assertThat(testReceipt.getPrinterType()).isEqualTo(DEFAULT_PRINTER_TYPE);
        assertThat(testReceipt.getJsonData()).isEqualTo(DEFAULT_JSON_DATA);
        assertThat(testReceipt.getPrintData()).isEqualTo(DEFAULT_PRINT_DATA);
        assertThat(testReceipt.getJwtData()).isEqualTo(DEFAULT_JWT_DATA);
        assertThat(testReceipt.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void createReceiptWithExistingId() throws Exception {
        // Create the Receipt with an existing ID
        receipt.setId(1L);
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        int databaseSizeBeforeCreate = receiptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = receiptRepository.findAll().size();
        // set the field null
        receipt.setCreated(null);

        // Create the Receipt, which fails.
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        restReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isBadRequest());

        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = receiptRepository.findAll().size();
        // set the field null
        receipt.setPrice(null);

        // Create the Receipt, which fails.
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        restReceiptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isBadRequest());

        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReceipts() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        // Get all the receiptList
        restReceiptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receipt.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].tiskelCorporateId").value(hasItem(DEFAULT_TISKEL_CORPORATE_ID)))
            .andExpect(jsonPath("$.[*].tiskelLicenseId").value(hasItem(DEFAULT_TISKEL_LICENSE_ID)))
            .andExpect(jsonPath("$.[*].taxiNumber").value(hasItem(DEFAULT_TAXI_NUMBER)))
            .andExpect(jsonPath("$.[*].driverId").value(hasItem(DEFAULT_DRIVER_ID)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].customerPhoneNumber").value(hasItem(DEFAULT_CUSTOMER_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].customerEmail").value(hasItem(DEFAULT_CUSTOMER_EMAIL)))
            .andExpect(jsonPath("$.[*].emailSentStatus").value(hasItem(DEFAULT_EMAIL_SENT_STATUS)))
            .andExpect(jsonPath("$.[*].deliveryType").value(hasItem(DEFAULT_DELIVERY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].printerType").value(hasItem(DEFAULT_PRINTER_TYPE)))
            .andExpect(jsonPath("$.[*].jsonData").value(hasItem(DEFAULT_JSON_DATA)))
            .andExpect(jsonPath("$.[*].printData").value(hasItem(DEFAULT_PRINT_DATA)))
            .andExpect(jsonPath("$.[*].jwtData").value(hasItem(DEFAULT_JWT_DATA)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)));
    }

    @Test
    @Transactional
    void getReceipt() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        // Get the receipt
        restReceiptMockMvc
            .perform(get(ENTITY_API_URL_ID, receipt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receipt.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.tiskelCorporateId").value(DEFAULT_TISKEL_CORPORATE_ID))
            .andExpect(jsonPath("$.tiskelLicenseId").value(DEFAULT_TISKEL_LICENSE_ID))
            .andExpect(jsonPath("$.taxiNumber").value(DEFAULT_TAXI_NUMBER))
            .andExpect(jsonPath("$.driverId").value(DEFAULT_DRIVER_ID))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.customerPhoneNumber").value(DEFAULT_CUSTOMER_PHONE_NUMBER))
            .andExpect(jsonPath("$.customerEmail").value(DEFAULT_CUSTOMER_EMAIL))
            .andExpect(jsonPath("$.emailSentStatus").value(DEFAULT_EMAIL_SENT_STATUS))
            .andExpect(jsonPath("$.deliveryType").value(DEFAULT_DELIVERY_TYPE.toString()))
            .andExpect(jsonPath("$.printerType").value(DEFAULT_PRINTER_TYPE))
            .andExpect(jsonPath("$.jsonData").value(DEFAULT_JSON_DATA))
            .andExpect(jsonPath("$.printData").value(DEFAULT_PRINT_DATA))
            .andExpect(jsonPath("$.jwtData").value(DEFAULT_JWT_DATA))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingReceipt() throws Exception {
        // Get the receipt
        restReceiptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReceipt() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();

        // Update the receipt
        Receipt updatedReceipt = receiptRepository.findById(receipt.getId()).get();
        // Disconnect from session so that the updates on updatedReceipt are not directly saved in db
        em.detach(updatedReceipt);
        updatedReceipt
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .orderId(UPDATED_ORDER_ID)
            .price(UPDATED_PRICE)
            .tiskelCorporateId(UPDATED_TISKEL_CORPORATE_ID)
            .tiskelLicenseId(UPDATED_TISKEL_LICENSE_ID)
            .taxiNumber(UPDATED_TAXI_NUMBER)
            .driverId(UPDATED_DRIVER_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerPhoneNumber(UPDATED_CUSTOMER_PHONE_NUMBER)
            .customerEmail(UPDATED_CUSTOMER_EMAIL)
            .emailSentStatus(UPDATED_EMAIL_SENT_STATUS)
            .deliveryType(UPDATED_DELIVERY_TYPE)
            .printerType(UPDATED_PRINTER_TYPE)
            .jsonData(UPDATED_JSON_DATA)
            .printData(UPDATED_PRINT_DATA)
            .jwtData(UPDATED_JWT_DATA)
            .errorMessage(UPDATED_ERROR_MESSAGE);
        ReceiptDTO receiptDTO = receiptMapper.toDto(updatedReceipt);

        restReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isOk());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReceipt.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testReceipt.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testReceipt.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testReceipt.getTiskelCorporateId()).isEqualTo(UPDATED_TISKEL_CORPORATE_ID);
        assertThat(testReceipt.getTiskelLicenseId()).isEqualTo(UPDATED_TISKEL_LICENSE_ID);
        assertThat(testReceipt.getTaxiNumber()).isEqualTo(UPDATED_TAXI_NUMBER);
        assertThat(testReceipt.getDriverId()).isEqualTo(UPDATED_DRIVER_ID);
        assertThat(testReceipt.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testReceipt.getCustomerPhoneNumber()).isEqualTo(UPDATED_CUSTOMER_PHONE_NUMBER);
        assertThat(testReceipt.getCustomerEmail()).isEqualTo(UPDATED_CUSTOMER_EMAIL);
        assertThat(testReceipt.getEmailSentStatus()).isEqualTo(UPDATED_EMAIL_SENT_STATUS);
        assertThat(testReceipt.getDeliveryType()).isEqualTo(UPDATED_DELIVERY_TYPE);
        assertThat(testReceipt.getPrinterType()).isEqualTo(UPDATED_PRINTER_TYPE);
        assertThat(testReceipt.getJsonData()).isEqualTo(UPDATED_JSON_DATA);
        assertThat(testReceipt.getPrintData()).isEqualTo(UPDATED_PRINT_DATA);
        assertThat(testReceipt.getJwtData()).isEqualTo(UPDATED_JWT_DATA);
        assertThat(testReceipt.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void putNonExistingReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receiptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receiptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt
            .status(UPDATED_STATUS)
            .orderId(UPDATED_ORDER_ID)
            .price(UPDATED_PRICE)
            .tiskelCorporateId(UPDATED_TISKEL_CORPORATE_ID)
            .tiskelLicenseId(UPDATED_TISKEL_LICENSE_ID)
            .taxiNumber(UPDATED_TAXI_NUMBER)
            .driverId(UPDATED_DRIVER_ID)
            .customerPhoneNumber(UPDATED_CUSTOMER_PHONE_NUMBER)
            .deliveryType(UPDATED_DELIVERY_TYPE)
            .printerType(UPDATED_PRINTER_TYPE)
            .jsonData(UPDATED_JSON_DATA)
            .printData(UPDATED_PRINT_DATA)
            .jwtData(UPDATED_JWT_DATA);

        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceipt))
            )
            .andExpect(status().isOk());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReceipt.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testReceipt.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testReceipt.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testReceipt.getTiskelCorporateId()).isEqualTo(UPDATED_TISKEL_CORPORATE_ID);
        assertThat(testReceipt.getTiskelLicenseId()).isEqualTo(UPDATED_TISKEL_LICENSE_ID);
        assertThat(testReceipt.getTaxiNumber()).isEqualTo(UPDATED_TAXI_NUMBER);
        assertThat(testReceipt.getDriverId()).isEqualTo(UPDATED_DRIVER_ID);
        assertThat(testReceipt.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testReceipt.getCustomerPhoneNumber()).isEqualTo(UPDATED_CUSTOMER_PHONE_NUMBER);
        assertThat(testReceipt.getCustomerEmail()).isEqualTo(DEFAULT_CUSTOMER_EMAIL);
        assertThat(testReceipt.getEmailSentStatus()).isEqualTo(DEFAULT_EMAIL_SENT_STATUS);
        assertThat(testReceipt.getDeliveryType()).isEqualTo(UPDATED_DELIVERY_TYPE);
        assertThat(testReceipt.getPrinterType()).isEqualTo(UPDATED_PRINTER_TYPE);
        assertThat(testReceipt.getJsonData()).isEqualTo(UPDATED_JSON_DATA);
        assertThat(testReceipt.getPrintData()).isEqualTo(UPDATED_PRINT_DATA);
        assertThat(testReceipt.getJwtData()).isEqualTo(UPDATED_JWT_DATA);
        assertThat(testReceipt.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void fullUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .orderId(UPDATED_ORDER_ID)
            .price(UPDATED_PRICE)
            .tiskelCorporateId(UPDATED_TISKEL_CORPORATE_ID)
            .tiskelLicenseId(UPDATED_TISKEL_LICENSE_ID)
            .taxiNumber(UPDATED_TAXI_NUMBER)
            .driverId(UPDATED_DRIVER_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerPhoneNumber(UPDATED_CUSTOMER_PHONE_NUMBER)
            .customerEmail(UPDATED_CUSTOMER_EMAIL)
            .emailSentStatus(UPDATED_EMAIL_SENT_STATUS)
            .deliveryType(UPDATED_DELIVERY_TYPE)
            .printerType(UPDATED_PRINTER_TYPE)
            .jsonData(UPDATED_JSON_DATA)
            .printData(UPDATED_PRINT_DATA)
            .jwtData(UPDATED_JWT_DATA)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceipt))
            )
            .andExpect(status().isOk());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
        Receipt testReceipt = receiptList.get(receiptList.size() - 1);
        assertThat(testReceipt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReceipt.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testReceipt.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testReceipt.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testReceipt.getTiskelCorporateId()).isEqualTo(UPDATED_TISKEL_CORPORATE_ID);
        assertThat(testReceipt.getTiskelLicenseId()).isEqualTo(UPDATED_TISKEL_LICENSE_ID);
        assertThat(testReceipt.getTaxiNumber()).isEqualTo(UPDATED_TAXI_NUMBER);
        assertThat(testReceipt.getDriverId()).isEqualTo(UPDATED_DRIVER_ID);
        assertThat(testReceipt.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testReceipt.getCustomerPhoneNumber()).isEqualTo(UPDATED_CUSTOMER_PHONE_NUMBER);
        assertThat(testReceipt.getCustomerEmail()).isEqualTo(UPDATED_CUSTOMER_EMAIL);
        assertThat(testReceipt.getEmailSentStatus()).isEqualTo(UPDATED_EMAIL_SENT_STATUS);
        assertThat(testReceipt.getDeliveryType()).isEqualTo(UPDATED_DELIVERY_TYPE);
        assertThat(testReceipt.getPrinterType()).isEqualTo(UPDATED_PRINTER_TYPE);
        assertThat(testReceipt.getJsonData()).isEqualTo(UPDATED_JSON_DATA);
        assertThat(testReceipt.getPrintData()).isEqualTo(UPDATED_PRINT_DATA);
        assertThat(testReceipt.getJwtData()).isEqualTo(UPDATED_JWT_DATA);
        assertThat(testReceipt.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void patchNonExistingReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, receiptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceipt() throws Exception {
        int databaseSizeBeforeUpdate = receiptRepository.findAll().size();
        receipt.setId(count.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceiptMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(receiptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Receipt in the database
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceipt() throws Exception {
        // Initialize the database
        receiptRepository.saveAndFlush(receipt);

        int databaseSizeBeforeDelete = receiptRepository.findAll().size();

        // Delete the receipt
        restReceiptMockMvc
            .perform(delete(ENTITY_API_URL_ID, receipt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Receipt> receiptList = receiptRepository.findAll();
        assertThat(receiptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
