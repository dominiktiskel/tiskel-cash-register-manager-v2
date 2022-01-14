package com.tiskel.crm.web.rest;

import com.tiskel.crm.repository.PaymentItemRepository;
import com.tiskel.crm.service.PaymentItemService;
import com.tiskel.crm.service.dto.PaymentItemDTO;
import com.tiskel.crm.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tiskel.crm.domain.PaymentItem}.
 */
@RestController
@RequestMapping("/api")
public class PaymentItemResource {

    private final Logger log = LoggerFactory.getLogger(PaymentItemResource.class);

    private static final String ENTITY_NAME = "paymentItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentItemService paymentItemService;

    private final PaymentItemRepository paymentItemRepository;

    public PaymentItemResource(PaymentItemService paymentItemService, PaymentItemRepository paymentItemRepository) {
        this.paymentItemService = paymentItemService;
        this.paymentItemRepository = paymentItemRepository;
    }

    /**
     * {@code POST  /payment-items} : Create a new paymentItem.
     *
     * @param paymentItemDTO the paymentItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentItemDTO, or with status {@code 400 (Bad Request)} if the paymentItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-items")
    public ResponseEntity<PaymentItemDTO> createPaymentItem(@Valid @RequestBody PaymentItemDTO paymentItemDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentItem : {}", paymentItemDTO);
        if (paymentItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentItemDTO result = paymentItemService.save(paymentItemDTO);
        return ResponseEntity
            .created(new URI("/api/payment-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-items/:id} : Updates an existing paymentItem.
     *
     * @param id the id of the paymentItemDTO to save.
     * @param paymentItemDTO the paymentItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentItemDTO,
     * or with status {@code 400 (Bad Request)} if the paymentItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-items/{id}")
    public ResponseEntity<PaymentItemDTO> updatePaymentItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentItemDTO paymentItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentItem : {}, {}", id, paymentItemDTO);
        if (paymentItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentItemDTO result = paymentItemService.save(paymentItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-items/:id} : Partial updates given fields of an existing paymentItem, field will ignore if it is null
     *
     * @param id the id of the paymentItemDTO to save.
     * @param paymentItemDTO the paymentItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentItemDTO,
     * or with status {@code 400 (Bad Request)} if the paymentItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentItemDTO> partialUpdatePaymentItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentItemDTO paymentItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentItem partially : {}, {}", id, paymentItemDTO);
        if (paymentItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentItemDTO> result = paymentItemService.partialUpdate(paymentItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-items} : get all the paymentItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentItems in body.
     */
    @GetMapping("/payment-items")
    public ResponseEntity<List<PaymentItemDTO>> getAllPaymentItems(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PaymentItems");
        Page<PaymentItemDTO> page = paymentItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-items/:id} : get the "id" paymentItem.
     *
     * @param id the id of the paymentItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-items/{id}")
    public ResponseEntity<PaymentItemDTO> getPaymentItem(@PathVariable Long id) {
        log.debug("REST request to get PaymentItem : {}", id);
        Optional<PaymentItemDTO> paymentItemDTO = paymentItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentItemDTO);
    }

    /**
     * {@code DELETE  /payment-items/:id} : delete the "id" paymentItem.
     *
     * @param id the id of the paymentItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-items/{id}")
    public ResponseEntity<Void> deletePaymentItem(@PathVariable Long id) {
        log.debug("REST request to delete PaymentItem : {}", id);
        paymentItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
