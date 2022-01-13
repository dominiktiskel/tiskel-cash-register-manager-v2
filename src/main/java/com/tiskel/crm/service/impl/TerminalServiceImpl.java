package com.tiskel.crm.service.impl;

import com.tiskel.crm.domain.Terminal;
import com.tiskel.crm.repository.TerminalRepository;
import com.tiskel.crm.service.TerminalService;
import com.tiskel.crm.service.dto.TerminalDTO;
import com.tiskel.crm.service.mapper.TerminalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Terminal}.
 */
@Service
@Transactional
public class TerminalServiceImpl implements TerminalService {

    private final Logger log = LoggerFactory.getLogger(TerminalServiceImpl.class);

    private final TerminalRepository terminalRepository;

    private final TerminalMapper terminalMapper;

    public TerminalServiceImpl(TerminalRepository terminalRepository, TerminalMapper terminalMapper) {
        this.terminalRepository = terminalRepository;
        this.terminalMapper = terminalMapper;
    }

    @Override
    public TerminalDTO save(TerminalDTO terminalDTO) {
        log.debug("Request to save Terminal : {}", terminalDTO);
        Terminal terminal = terminalMapper.toEntity(terminalDTO);
        terminal = terminalRepository.save(terminal);
        return terminalMapper.toDto(terminal);
    }

    @Override
    public Optional<TerminalDTO> partialUpdate(TerminalDTO terminalDTO) {
        log.debug("Request to partially update Terminal : {}", terminalDTO);

        return terminalRepository
            .findById(terminalDTO.getId())
            .map(existingTerminal -> {
                terminalMapper.partialUpdate(existingTerminal, terminalDTO);

                return existingTerminal;
            })
            .map(terminalRepository::save)
            .map(terminalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerminalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Terminals");
        return terminalRepository.findAll(pageable).map(terminalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TerminalDTO> findOne(Long id) {
        log.debug("Request to get Terminal : {}", id);
        return terminalRepository.findById(id).map(terminalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Terminal : {}", id);
        terminalRepository.deleteById(id);
    }
}
