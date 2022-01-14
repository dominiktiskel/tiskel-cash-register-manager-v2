package com.tiskel.crm.service;

import com.tiskel.crm.service.dto.TerminalDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.tiskel.crm.domain.Terminal}.
 */
public interface TerminalService {
    /**
     * Save a terminal.
     *
     * @param terminalDTO the entity to save.
     * @return the persisted entity.
     */
    TerminalDTO save(TerminalDTO terminalDTO);

    /**
     * Partially updates a terminal.
     *
     * @param terminalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TerminalDTO> partialUpdate(TerminalDTO terminalDTO);

    /**
     * Get all the terminals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TerminalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" terminal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TerminalDTO> findOne(Long id);

    /**
     * Delete the "id" terminal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
