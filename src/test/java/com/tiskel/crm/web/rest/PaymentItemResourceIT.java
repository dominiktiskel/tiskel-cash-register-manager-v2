package com.tiskel.crm.web.rest;

import static com.tiskel.crm.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tiskel.crm.IntegrationTest;
import com.tiskel.crm.domain.Payment;
import com.tiskel.crm.domain.PaymentItem;
import com.tiskel.crm.domain.Terminal;
import com.tiskel.crm.repository.PaymentItemRepository;
import com.tiskel.crm.service.dto.PaymentItemDTO;
import com.tiskel.crm.service.mapper.PaymentItemMapper;
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
 * Integration tests for the {@link PaymentItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentItemResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_DAYS_PAYED = 1;
    private static final Integer UPDATED_DAYS_PAYED = 2;

    private static final String ENTITY_API_URL = "/api/payment-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentItemRepository paymentItemRepository;

    @Autowired
    private PaymentItemMapper paymentItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentItemMockMvc;

    private PaymentItem paymentItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentItem createEntity(EntityManager em) {
        PaymentItem paymentItem = new PaymentItem().created(DEFAULT_CREATED).daysPayed(DEFAULT_DAYS_PAYED);
        // Add required entity
        Terminal terminal;
        if (TestUtil.findAll(em, Terminal.class).isEmpty()) {
            terminal = TerminalResourceIT.createEntity(em);
            em.persist(terminal);
            em.flush();
        } else {
            terminal = TestUtil.findAll(em, Terminal.class).get(0);
        }
        paymentItem.setTerminal(terminal);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        paymentItem.setPayment(payment);
        return paymentItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentItem createUpdatedEntity(EntityManager em) {
        PaymentItem paymentItem = new PaymentItem().created(UPDATED_CREATED).daysPayed(UPDATED_DAYS_PAYED);
        // Add required entity
        Terminal terminal;
        if (TestUtil.findAll(em, Terminal.class).isEmpty()) {
            terminal = TerminalResourceIT.createUpdatedEntity(em);
            em.persist(terminal);
            em.flush();
        } else {
            terminal = TestUtil.findAll(em, Terminal.class).get(0);
        }
        paymentItem.setTerminal(terminal);
        // Add required entity
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createUpdatedEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        paymentItem.setPayment(payment);
        return paymentItem;
    }

    @BeforeEach
    public void initTest() {
        paymentItem = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentItem() throws Exception {
        int databaseSizeBeforeCreate = paymentItemRepository.findAll().size();
        // Create the PaymentItem
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);
        restPaymentItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentItem testPaymentItem = paymentItemList.get(paymentItemList.size() - 1);
        assertThat(testPaymentItem.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testPaymentItem.getDaysPayed()).isEqualTo(DEFAULT_DAYS_PAYED);
    }

    @Test
    @Transactional
    void createPaymentItemWithExistingId() throws Exception {
        // Create the PaymentItem with an existing ID
        paymentItem.setId(1L);
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);

        int databaseSizeBeforeCreate = paymentItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentItemRepository.findAll().size();
        // set the field null
        paymentItem.setCreated(null);

        // Create the PaymentItem, which fails.
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);

        restPaymentItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDaysPayedIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentItemRepository.findAll().size();
        // set the field null
        paymentItem.setDaysPayed(null);

        // Create the PaymentItem, which fails.
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);

        restPaymentItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentItems() throws Exception {
        // Initialize the database
        paymentItemRepository.saveAndFlush(paymentItem);

        // Get all the paymentItemList
        restPaymentItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].daysPayed").value(hasItem(DEFAULT_DAYS_PAYED)));
    }

    @Test
    @Transactional
    void getPaymentItem() throws Exception {
        // Initialize the database
        paymentItemRepository.saveAndFlush(paymentItem);

        // Get the paymentItem
        restPaymentItemMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentItem.getId().intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.daysPayed").value(DEFAULT_DAYS_PAYED));
    }

    @Test
    @Transactional
    void getNonExistingPaymentItem() throws Exception {
        // Get the paymentItem
        restPaymentItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaymentItem() throws Exception {
        // Initialize the database
        paymentItemRepository.saveAndFlush(paymentItem);

        int databaseSizeBeforeUpdate = paymentItemRepository.findAll().size();

        // Update the paymentItem
        PaymentItem updatedPaymentItem = paymentItemRepository.findById(paymentItem.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentItem are not directly saved in db
        em.detach(updatedPaymentItem);
        updatedPaymentItem.created(UPDATED_CREATED).daysPayed(UPDATED_DAYS_PAYED);
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(updatedPaymentItem);

        restPaymentItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeUpdate);
        PaymentItem testPaymentItem = paymentItemList.get(paymentItemList.size() - 1);
        assertThat(testPaymentItem.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPaymentItem.getDaysPayed()).isEqualTo(UPDATED_DAYS_PAYED);
    }

    @Test
    @Transactional
    void putNonExistingPaymentItem() throws Exception {
        int databaseSizeBeforeUpdate = paymentItemRepository.findAll().size();
        paymentItem.setId(count.incrementAndGet());

        // Create the PaymentItem
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentItem() throws Exception {
        int databaseSizeBeforeUpdate = paymentItemRepository.findAll().size();
        paymentItem.setId(count.incrementAndGet());

        // Create the PaymentItem
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentItem() throws Exception {
        int databaseSizeBeforeUpdate = paymentItemRepository.findAll().size();
        paymentItem.setId(count.incrementAndGet());

        // Create the PaymentItem
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentItemWithPatch() throws Exception {
        // Initialize the database
        paymentItemRepository.saveAndFlush(paymentItem);

        int databaseSizeBeforeUpdate = paymentItemRepository.findAll().size();

        // Update the paymentItem using partial update
        PaymentItem partialUpdatedPaymentItem = new PaymentItem();
        partialUpdatedPaymentItem.setId(paymentItem.getId());

        partialUpdatedPaymentItem.created(UPDATED_CREATED);

        restPaymentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentItem))
            )
            .andExpect(status().isOk());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeUpdate);
        PaymentItem testPaymentItem = paymentItemList.get(paymentItemList.size() - 1);
        assertThat(testPaymentItem.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPaymentItem.getDaysPayed()).isEqualTo(DEFAULT_DAYS_PAYED);
    }

    @Test
    @Transactional
    void fullUpdatePaymentItemWithPatch() throws Exception {
        // Initialize the database
        paymentItemRepository.saveAndFlush(paymentItem);

        int databaseSizeBeforeUpdate = paymentItemRepository.findAll().size();

        // Update the paymentItem using partial update
        PaymentItem partialUpdatedPaymentItem = new PaymentItem();
        partialUpdatedPaymentItem.setId(paymentItem.getId());

        partialUpdatedPaymentItem.created(UPDATED_CREATED).daysPayed(UPDATED_DAYS_PAYED);

        restPaymentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentItem))
            )
            .andExpect(status().isOk());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeUpdate);
        PaymentItem testPaymentItem = paymentItemList.get(paymentItemList.size() - 1);
        assertThat(testPaymentItem.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testPaymentItem.getDaysPayed()).isEqualTo(UPDATED_DAYS_PAYED);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentItem() throws Exception {
        int databaseSizeBeforeUpdate = paymentItemRepository.findAll().size();
        paymentItem.setId(count.incrementAndGet());

        // Create the PaymentItem
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentItem() throws Exception {
        int databaseSizeBeforeUpdate = paymentItemRepository.findAll().size();
        paymentItem.setId(count.incrementAndGet());

        // Create the PaymentItem
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentItem() throws Exception {
        int databaseSizeBeforeUpdate = paymentItemRepository.findAll().size();
        paymentItem.setId(count.incrementAndGet());

        // Create the PaymentItem
        PaymentItemDTO paymentItemDTO = paymentItemMapper.toDto(paymentItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paymentItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentItem in the database
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentItem() throws Exception {
        // Initialize the database
        paymentItemRepository.saveAndFlush(paymentItem);

        int databaseSizeBeforeDelete = paymentItemRepository.findAll().size();

        // Delete the paymentItem
        restPaymentItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentItem> paymentItemList = paymentItemRepository.findAll();
        assertThat(paymentItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
