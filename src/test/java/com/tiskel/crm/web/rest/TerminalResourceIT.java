package com.tiskel.crm.web.rest;

import static com.tiskel.crm.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tiskel.crm.IntegrationTest;
import com.tiskel.crm.domain.Company;
import com.tiskel.crm.domain.Terminal;
import com.tiskel.crm.domain.enumeration.TerminalPayedByType;
import com.tiskel.crm.domain.enumeration.TerminalSubscriptionPeriod;
import com.tiskel.crm.repository.TerminalRepository;
import com.tiskel.crm.service.dto.TerminalDTO;
import com.tiskel.crm.service.mapper.TerminalMapper;
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
 * Integration tests for the {@link TerminalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TerminalResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final TerminalPayedByType DEFAULT_PAYED_BY = TerminalPayedByType.DRIVER;
    private static final TerminalPayedByType UPDATED_PAYED_BY = TerminalPayedByType.TAXI;

    private static final ZonedDateTime DEFAULT_PAYED_TO_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAYED_TO_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SUBSCRIPTION_RENEWAL_ENABLED = false;
    private static final Boolean UPDATED_SUBSCRIPTION_RENEWAL_ENABLED = true;

    private static final Integer DEFAULT_SUBSCRIPTION_RENEWAL_TRIAL_COUNT = 1;
    private static final Integer UPDATED_SUBSCRIPTION_RENEWAL_TRIAL_COUNT = 2;

    private static final TerminalSubscriptionPeriod DEFAULT_SUBSCRIPTION_PERIOD = TerminalSubscriptionPeriod.WEEK;
    private static final TerminalSubscriptionPeriod UPDATED_SUBSCRIPTION_PERIOD = TerminalSubscriptionPeriod.MONTH;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/terminals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private TerminalMapper terminalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTerminalMockMvc;

    private Terminal terminal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terminal createEntity(EntityManager em) {
        Terminal terminal = new Terminal()
            .created(DEFAULT_CREATED)
            .payedBy(DEFAULT_PAYED_BY)
            .payedToDate(DEFAULT_PAYED_TO_DATE)
            .number(DEFAULT_NUMBER)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .subscriptionRenewalEnabled(DEFAULT_SUBSCRIPTION_RENEWAL_ENABLED)
            .subscriptionRenewalTrialCount(DEFAULT_SUBSCRIPTION_RENEWAL_TRIAL_COUNT)
            .subscriptionPeriod(DEFAULT_SUBSCRIPTION_PERIOD)
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
        terminal.setCompany(company);
        return terminal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terminal createUpdatedEntity(EntityManager em) {
        Terminal terminal = new Terminal()
            .created(UPDATED_CREATED)
            .payedBy(UPDATED_PAYED_BY)
            .payedToDate(UPDATED_PAYED_TO_DATE)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .subscriptionRenewalEnabled(UPDATED_SUBSCRIPTION_RENEWAL_ENABLED)
            .subscriptionRenewalTrialCount(UPDATED_SUBSCRIPTION_RENEWAL_TRIAL_COUNT)
            .subscriptionPeriod(UPDATED_SUBSCRIPTION_PERIOD)
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
        terminal.setCompany(company);
        return terminal;
    }

    @BeforeEach
    public void initTest() {
        terminal = createEntity(em);
    }

    @Test
    @Transactional
    void createTerminal() throws Exception {
        int databaseSizeBeforeCreate = terminalRepository.findAll().size();
        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);
        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isCreated());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate + 1);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTerminal.getPayedBy()).isEqualTo(DEFAULT_PAYED_BY);
        assertThat(testTerminal.getPayedToDate()).isEqualTo(DEFAULT_PAYED_TO_DATE);
        assertThat(testTerminal.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testTerminal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTerminal.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTerminal.getSubscriptionRenewalEnabled()).isEqualTo(DEFAULT_SUBSCRIPTION_RENEWAL_ENABLED);
        assertThat(testTerminal.getSubscriptionRenewalTrialCount()).isEqualTo(DEFAULT_SUBSCRIPTION_RENEWAL_TRIAL_COUNT);
        assertThat(testTerminal.getSubscriptionPeriod()).isEqualTo(DEFAULT_SUBSCRIPTION_PERIOD);
        assertThat(testTerminal.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testTerminal.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void createTerminalWithExistingId() throws Exception {
        // Create the Terminal with an existing ID
        terminal.setId(1L);
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        int databaseSizeBeforeCreate = terminalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setCreated(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPayedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setPayedBy(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPayedToDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setPayedToDate(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setNumber(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionRenewalEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setSubscriptionRenewalEnabled(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionRenewalTrialCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setSubscriptionRenewalTrialCount(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubscriptionPeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setSubscriptionPeriod(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalRepository.findAll().size();
        // set the field null
        terminal.setActive(null);

        // Create the Terminal, which fails.
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        restTerminalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isBadRequest());

        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTerminals() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminal.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].payedBy").value(hasItem(DEFAULT_PAYED_BY.toString())))
            .andExpect(jsonPath("$.[*].payedToDate").value(hasItem(sameInstant(DEFAULT_PAYED_TO_DATE))))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].subscriptionRenewalEnabled").value(hasItem(DEFAULT_SUBSCRIPTION_RENEWAL_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].subscriptionRenewalTrialCount").value(hasItem(DEFAULT_SUBSCRIPTION_RENEWAL_TRIAL_COUNT)))
            .andExpect(jsonPath("$.[*].subscriptionPeriod").value(hasItem(DEFAULT_SUBSCRIPTION_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)));
    }

    @Test
    @Transactional
    void getTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get the terminal
        restTerminalMockMvc
            .perform(get(ENTITY_API_URL_ID, terminal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(terminal.getId().intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.payedBy").value(DEFAULT_PAYED_BY.toString()))
            .andExpect(jsonPath("$.payedToDate").value(sameInstant(DEFAULT_PAYED_TO_DATE)))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.subscriptionRenewalEnabled").value(DEFAULT_SUBSCRIPTION_RENEWAL_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.subscriptionRenewalTrialCount").value(DEFAULT_SUBSCRIPTION_RENEWAL_TRIAL_COUNT))
            .andExpect(jsonPath("$.subscriptionPeriod").value(DEFAULT_SUBSCRIPTION_PERIOD.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingTerminal() throws Exception {
        // Get the terminal
        restTerminalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal
        Terminal updatedTerminal = terminalRepository.findById(terminal.getId()).get();
        // Disconnect from session so that the updates on updatedTerminal are not directly saved in db
        em.detach(updatedTerminal);
        updatedTerminal
            .created(UPDATED_CREATED)
            .payedBy(UPDATED_PAYED_BY)
            .payedToDate(UPDATED_PAYED_TO_DATE)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .subscriptionRenewalEnabled(UPDATED_SUBSCRIPTION_RENEWAL_ENABLED)
            .subscriptionRenewalTrialCount(UPDATED_SUBSCRIPTION_RENEWAL_TRIAL_COUNT)
            .subscriptionPeriod(UPDATED_SUBSCRIPTION_PERIOD)
            .active(UPDATED_ACTIVE)
            .errorMessage(UPDATED_ERROR_MESSAGE);
        TerminalDTO terminalDTO = terminalMapper.toDto(updatedTerminal);

        restTerminalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTerminal.getPayedBy()).isEqualTo(UPDATED_PAYED_BY);
        assertThat(testTerminal.getPayedToDate()).isEqualTo(UPDATED_PAYED_TO_DATE);
        assertThat(testTerminal.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testTerminal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTerminal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTerminal.getSubscriptionRenewalEnabled()).isEqualTo(UPDATED_SUBSCRIPTION_RENEWAL_ENABLED);
        assertThat(testTerminal.getSubscriptionRenewalTrialCount()).isEqualTo(UPDATED_SUBSCRIPTION_RENEWAL_TRIAL_COUNT);
        assertThat(testTerminal.getSubscriptionPeriod()).isEqualTo(UPDATED_SUBSCRIPTION_PERIOD);
        assertThat(testTerminal.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testTerminal.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void putNonExistingTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTerminalWithPatch() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal using partial update
        Terminal partialUpdatedTerminal = new Terminal();
        partialUpdatedTerminal.setId(terminal.getId());

        partialUpdatedTerminal
            .payedToDate(UPDATED_PAYED_TO_DATE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .subscriptionRenewalTrialCount(UPDATED_SUBSCRIPTION_RENEWAL_TRIAL_COUNT)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminal))
            )
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTerminal.getPayedBy()).isEqualTo(DEFAULT_PAYED_BY);
        assertThat(testTerminal.getPayedToDate()).isEqualTo(UPDATED_PAYED_TO_DATE);
        assertThat(testTerminal.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testTerminal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTerminal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTerminal.getSubscriptionRenewalEnabled()).isEqualTo(DEFAULT_SUBSCRIPTION_RENEWAL_ENABLED);
        assertThat(testTerminal.getSubscriptionRenewalTrialCount()).isEqualTo(UPDATED_SUBSCRIPTION_RENEWAL_TRIAL_COUNT);
        assertThat(testTerminal.getSubscriptionPeriod()).isEqualTo(DEFAULT_SUBSCRIPTION_PERIOD);
        assertThat(testTerminal.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testTerminal.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void fullUpdateTerminalWithPatch() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal using partial update
        Terminal partialUpdatedTerminal = new Terminal();
        partialUpdatedTerminal.setId(terminal.getId());

        partialUpdatedTerminal
            .created(UPDATED_CREATED)
            .payedBy(UPDATED_PAYED_BY)
            .payedToDate(UPDATED_PAYED_TO_DATE)
            .number(UPDATED_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .subscriptionRenewalEnabled(UPDATED_SUBSCRIPTION_RENEWAL_ENABLED)
            .subscriptionRenewalTrialCount(UPDATED_SUBSCRIPTION_RENEWAL_TRIAL_COUNT)
            .subscriptionPeriod(UPDATED_SUBSCRIPTION_PERIOD)
            .active(UPDATED_ACTIVE)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminal))
            )
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTerminal.getPayedBy()).isEqualTo(UPDATED_PAYED_BY);
        assertThat(testTerminal.getPayedToDate()).isEqualTo(UPDATED_PAYED_TO_DATE);
        assertThat(testTerminal.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testTerminal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTerminal.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTerminal.getSubscriptionRenewalEnabled()).isEqualTo(UPDATED_SUBSCRIPTION_RENEWAL_ENABLED);
        assertThat(testTerminal.getSubscriptionRenewalTrialCount()).isEqualTo(UPDATED_SUBSCRIPTION_RENEWAL_TRIAL_COUNT);
        assertThat(testTerminal.getSubscriptionPeriod()).isEqualTo(UPDATED_SUBSCRIPTION_PERIOD);
        assertThat(testTerminal.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testTerminal.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
    }

    @Test
    @Transactional
    void patchNonExistingTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, terminalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();
        terminal.setId(count.incrementAndGet());

        // Create the Terminal
        TerminalDTO terminalDTO = terminalMapper.toDto(terminal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(terminalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeDelete = terminalRepository.findAll().size();

        // Delete the terminal
        restTerminalMockMvc
            .perform(delete(ENTITY_API_URL_ID, terminal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
