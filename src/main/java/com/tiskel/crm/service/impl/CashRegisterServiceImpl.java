package com.tiskel.crm.service.impl;

import com.tiskel.crm.domain.CashRegister;
import com.tiskel.crm.repository.CashRegisterRepository;
import com.tiskel.crm.service.CashRegisterService;
import com.tiskel.crm.service.dto.CashRegisterDTO;
import com.tiskel.crm.service.mapper.CashRegisterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CashRegister}.
 */
@Service
@Transactional
public class CashRegisterServiceImpl implements CashRegisterService {

    private final Logger log = LoggerFactory.getLogger(CashRegisterServiceImpl.class);

    private final CashRegisterRepository cashRegisterRepository;

    private final CashRegisterMapper cashRegisterMapper;

    public CashRegisterServiceImpl(CashRegisterRepository cashRegisterRepository, CashRegisterMapper cashRegisterMapper) {
        this.cashRegisterRepository = cashRegisterRepository;
        this.cashRegisterMapper = cashRegisterMapper;
    }

    @Override
    public CashRegisterDTO save(CashRegisterDTO cashRegisterDTO) {
        log.debug("Request to save CashRegister : {}", cashRegisterDTO);
        CashRegister cashRegister = cashRegisterMapper.toEntity(cashRegisterDTO);
        cashRegister = cashRegisterRepository.save(cashRegister);
        return cashRegisterMapper.toDto(cashRegister);
    }

    @Override
    public Optional<CashRegisterDTO> partialUpdate(CashRegisterDTO cashRegisterDTO) {
        log.debug("Request to partially update CashRegister : {}", cashRegisterDTO);

        return cashRegisterRepository
            .findById(cashRegisterDTO.getId())
            .map(existingCashRegister -> {
                cashRegisterMapper.partialUpdate(existingCashRegister, cashRegisterDTO);

                return existingCashRegister;
            })
            .map(cashRegisterRepository::save)
            .map(cashRegisterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CashRegisterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CashRegisters");
        return cashRegisterRepository.findAll(pageable).map(cashRegisterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CashRegisterDTO> findOne(Long id) {
        log.debug("Request to get CashRegister : {}", id);
        return cashRegisterRepository.findById(id).map(cashRegisterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CashRegister : {}", id);
        cashRegisterRepository.deleteById(id);
    }
}
