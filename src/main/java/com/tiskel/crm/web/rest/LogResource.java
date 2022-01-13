package com.tiskel.crm.web.rest;

import com.tiskel.crm.repository.LogRepository;
import com.tiskel.crm.service.LogService;
import com.tiskel.crm.service.dto.LogDTO;
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
 * REST controller for managing {@link com.tiskel.crm.domain.Log}.
 */
@RestController
@RequestMapping("/api")
public class LogResource {

    private final Logger log = LoggerFactory.getLogger(LogResource.class);

    private static final String ENTITY_NAME = "log";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LogService logService;

    private final LogRepository logRepository;

    public LogResource(LogService logService, LogRepository logRepository) {
        this.logService = logService;
        this.logRepository = logRepository;
    }

    /**
     * {@code POST  /logs} : Create a new log.
     *
     * @param logDTO the logDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new logDTO, or with status {@code 400 (Bad Request)} if the log has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/logs")
    public ResponseEntity<LogDTO> createLog(@Valid @RequestBody LogDTO logDTO) throws URISyntaxException {
        log.debug("REST request to save Log : {}", logDTO);
        if (logDTO.getId() != null) {
            throw new BadRequestAlertException("A new log cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LogDTO result = logService.save(logDTO);
        return ResponseEntity
            .created(new URI("/api/logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /logs/:id} : Updates an existing log.
     *
     * @param id the id of the logDTO to save.
     * @param logDTO the logDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated logDTO,
     * or with status {@code 400 (Bad Request)} if the logDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the logDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/logs/{id}")
    public ResponseEntity<LogDTO> updateLog(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody LogDTO logDTO)
        throws URISyntaxException {
        log.debug("REST request to update Log : {}, {}", id, logDTO);
        if (logDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, logDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!logRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LogDTO result = logService.save(logDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, logDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /logs/:id} : Partial updates given fields of an existing log, field will ignore if it is null
     *
     * @param id the id of the logDTO to save.
     * @param logDTO the logDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated logDTO,
     * or with status {@code 400 (Bad Request)} if the logDTO is not valid,
     * or with status {@code 404 (Not Found)} if the logDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the logDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/logs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LogDTO> partialUpdateLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LogDTO logDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Log partially : {}, {}", id, logDTO);
        if (logDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, logDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!logRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LogDTO> result = logService.partialUpdate(logDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, logDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /logs} : get all the logs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of logs in body.
     */
    @GetMapping("/logs")
    public ResponseEntity<List<LogDTO>> getAllLogs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Logs");
        Page<LogDTO> page = logService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /logs/:id} : get the "id" log.
     *
     * @param id the id of the logDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the logDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logs/{id}")
    public ResponseEntity<LogDTO> getLog(@PathVariable Long id) {
        log.debug("REST request to get Log : {}", id);
        Optional<LogDTO> logDTO = logService.findOne(id);
        return ResponseUtil.wrapOrNotFound(logDTO);
    }

    /**
     * {@code DELETE  /logs/:id} : delete the "id" log.
     *
     * @param id the id of the logDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/logs/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        log.debug("REST request to delete Log : {}", id);
        logService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
