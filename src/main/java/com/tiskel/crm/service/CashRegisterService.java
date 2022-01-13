package com.tiskel.crm.service;

import com.tiskel.crm.service.dto.CashRegisterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.tiskel.crm.domain.CashRegister}.
 */
public interface CashRegisterService {
    /**
     * Save a cashRegister.
     *
     * @param cashRegisterDTO the entity to save.
     * @return the persisted entity.
     */
    CashRegisterDTO save(CashRegisterDTO cashRegisterDTO);

    /**
     * Partially updates a cashRegister.
     *
     * @param cashRegisterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CashRegisterDTO> partialUpdate(CashRegisterDTO cashRegisterDTO);

    /**
     * Get all the cashRegisters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CashRegisterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cashRegister.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CashRegisterDTO> findOne(Long id);

    /**
     * Delete the "id" cashRegister.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
