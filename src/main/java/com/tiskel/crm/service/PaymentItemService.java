package com.tiskel.crm.service;

import com.tiskel.crm.service.dto.PaymentItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.tiskel.crm.domain.PaymentItem}.
 */
public interface PaymentItemService {
    /**
     * Save a paymentItem.
     *
     * @param paymentItemDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentItemDTO save(PaymentItemDTO paymentItemDTO);

    /**
     * Partially updates a paymentItem.
     *
     * @param paymentItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentItemDTO> partialUpdate(PaymentItemDTO paymentItemDTO);

    /**
     * Get all the paymentItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paymentItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentItemDTO> findOne(Long id);

    /**
     * Delete the "id" paymentItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
