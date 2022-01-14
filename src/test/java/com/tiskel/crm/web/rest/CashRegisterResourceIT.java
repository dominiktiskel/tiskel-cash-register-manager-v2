package com.tiskel.crm.web.rest;

import static com.tiskel.crm.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tiskel.crm.IntegrationTest;
import com.tiskel.crm.domain.CashRegister;
import com.tiskel.crm.domain.Company;
import com.tiskel.crm.repository.CashRegisterRepository;
import com.tiskel.crm.service.dto.CashRegisterDTO;
import com.tiskel.crm.service.mapper.CashRegisterMapper;
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
 * Integration tests for the {@link CashRegisterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CashRegisterResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ELZAB_ID = "AAAAAAAAAA";
    private static final String UPDATED_ELZAB_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ELZAB_LICENSE = "AAAAAAAAAA";
    private static final String UPDATED_ELZAB_LICENSE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cash-registers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CashRegisterRepository cashRegisterRepository;

    @Autowired
    private CashRegisterMapper cashRegisterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCashRegisterMockMvc;

    private CashRegister cashRegister;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashRegister createEntity(EntityManager em) {
        CashRegister cashRegister = new CashRegister()
            .created(DEFAULT_CREATED)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .elzabId(DEFAULT_ELZAB_ID)
            .elzabLicense(DEFAULT_ELZAB_LICENSE)
            .active(DEFAULT_ACTIVE)
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
        cashRegister.setCompany(company);
        return cashRegister;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CashRegister createUpdatedEntity(EntityManager em) {
        CashRegister cashRegister = new CashRegister()
            .created(UPDATED_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .elzabId(UPDATED_ELZAB_ID)
            .elzabLicense(UPDATED_ELZAB_LICENSE)
            .active(UPDATED_ACTIVE)
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
        cashRegister.setCompany(company);
        return cashRegister;
    }

    @BeforeEach
    public void initTest() {
        cashRegister = createEntity(em);
    }

    @Test
    @Transactional
    void createCashRegister() throws Exception {
        int databaseSizeBeforeCreate = cashRegisterRepository.findAll().size();
        // Create the CashRegister
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);
        restCashRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeCreate + 1);
        CashRegister testCashRegister = cashRegisterList.get(cashRegisterList.size() - 1);
        assertThat(testCashRegister.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testCashRegister.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testCashRegister.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCashRegister.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCashRegister.getElzabId()).isEqualTo(DEFAULT_ELZAB_ID);
        assertThat(testCashRegister.getElzabLicense()).isEqualTo(DEFAULT_ELZAB_LICENSE);
        assertThat(testCashRegister.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testCashRegister.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void createCashRegisterWithExistingId() throws Exception {
        // Create the CashRegister with an existing ID
        cashRegister.setId(1L);
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);

        int databaseSizeBeforeCreate = cashRegisterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCashRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashRegisterRepository.findAll().size();
        // set the field null
        cashRegister.setCreated(null);

        // Create the CashRegister, which fails.
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);

        restCashRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = cashRegisterRepository.findAll().size();
        // set the field null
        cashRegister.setActive(null);

        // Create the CashRegister, which fails.
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);

        restCashRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCashRegisters() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        // Get all the cashRegisterList
        restCashRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cashRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].elzabId").value(hasItem(DEFAULT_ELZAB_ID)))
            .andExpect(jsonPath("$.[*].elzabLicense").value(hasItem(DEFAULT_ELZAB_LICENSE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)));
    }

    @Test
    @Transactional
    void getCashRegister() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        // Get the cashRegister
        restCashRegisterMockMvc
            .perform(get(ENTITY_API_URL_ID, cashRegister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cashRegister.getId().intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.lastUpdate").value(sameInstant(DEFAULT_LAST_UPDATE)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.elzabId").value(DEFAULT_ELZAB_ID))
            .andExpect(jsonPath("$.elzabLicense").value(DEFAULT_ELZAB_LICENSE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingCashRegister() throws Exception {
        // Get the cashRegister
        restCashRegisterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCashRegister() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();

        // Update the cashRegister
        CashRegister updatedCashRegister = cashRegisterRepository.findById(cashRegister.getId()).get();
        // Disconnect from session so that the updates on updatedCashRegister are not directly saved in db
        em.detach(updatedCashRegister);
        updatedCashRegister
            .created(UPDATED_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .elzabId(UPDATED_ELZAB_ID)
            .elzabLicense(UPDATED_ELZAB_LICENSE)
            .active(UPDATED_ACTIVE)
            .errorMessage(UPDATED_ERROR_MESSAGE);
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(updatedCashRegister);

        restCashRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cashRegisterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isOk());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
        CashRegister testCashRegister = cashRegisterList.get(cashRegisterList.size() - 1);
        assertThat(testCashRegister.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCashRegister.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testCashRegister.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCashRegister.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCashRegister.getElzabId()).isEqualTo(UPDATED_ELZAB_ID);
        assertThat(testCashRegister.getElzabLicense()).isEqualTo(UPDATED_ELZAB_LICENSE);
        assertThat(testCashRegister.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCashRegister.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void putNonExistingCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // Create the CashRegister
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cashRegisterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // Create the CashRegister
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // Create the CashRegister
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCashRegisterWithPatch() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();

        // Update the cashRegister using partial update
        CashRegister partialUpdatedCashRegister = new CashRegister();
        partialUpdatedCashRegister.setId(cashRegister.getId());

        partialUpdatedCashRegister.created(UPDATED_CREATED).elzabId(UPDATED_ELZAB_ID).active(UPDATED_ACTIVE);

        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCashRegister))
            )
            .andExpect(status().isOk());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
        CashRegister testCashRegister = cashRegisterList.get(cashRegisterList.size() - 1);
        assertThat(testCashRegister.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCashRegister.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testCashRegister.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCashRegister.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCashRegister.getElzabId()).isEqualTo(UPDATED_ELZAB_ID);
        assertThat(testCashRegister.getElzabLicense()).isEqualTo(DEFAULT_ELZAB_LICENSE);
        assertThat(testCashRegister.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCashRegister.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void fullUpdateCashRegisterWithPatch() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();

        // Update the cashRegister using partial update
        CashRegister partialUpdatedCashRegister = new CashRegister();
        partialUpdatedCashRegister.setId(cashRegister.getId());

        partialUpdatedCashRegister
            .created(UPDATED_CREATED)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .elzabId(UPDATED_ELZAB_ID)
            .elzabLicense(UPDATED_ELZAB_LICENSE)
            .active(UPDATED_ACTIVE)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCashRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCashRegister))
            )
            .andExpect(status().isOk());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
        CashRegister testCashRegister = cashRegisterList.get(cashRegisterList.size() - 1);
        assertThat(testCashRegister.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testCashRegister.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testCashRegister.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCashRegister.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCashRegister.getElzabId()).isEqualTo(UPDATED_ELZAB_ID);
        assertThat(testCashRegister.getElzabLicense()).isEqualTo(UPDATED_ELZAB_LICENSE);
        assertThat(testCashRegister.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCashRegister.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void patchNonExistingCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // Create the CashRegister
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cashRegisterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // Create the CashRegister
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCashRegister() throws Exception {
        int databaseSizeBeforeUpdate = cashRegisterRepository.findAll().size();
        cashRegister.setId(count.incrementAndGet());

        // Create the CashRegister
        CashRegisterDTO cashRegisterDTO = cashRegisterMapper.toDto(cashRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCashRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cashRegisterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CashRegister in the database
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCashRegister() throws Exception {
        // Initialize the database
        cashRegisterRepository.saveAndFlush(cashRegister);

        int databaseSizeBeforeDelete = cashRegisterRepository.findAll().size();

        // Delete the cashRegister
        restCashRegisterMockMvc
            .perform(delete(ENTITY_API_URL_ID, cashRegister.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CashRegister> cashRegisterList = cashRegisterRepository.findAll();
        assertThat(cashRegisterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
