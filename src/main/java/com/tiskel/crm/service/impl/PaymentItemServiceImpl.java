package com.tiskel.crm.service.impl;

import com.tiskel.crm.domain.PaymentItem;
import com.tiskel.crm.repository.PaymentItemRepository;
import com.tiskel.crm.service.PaymentItemService;
import com.tiskel.crm.service.dto.PaymentItemDTO;
import com.tiskel.crm.service.mapper.PaymentItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentItem}.
 */
@Service
@Transactional
public class PaymentItemServiceImpl implements PaymentItemService {

    private final Logger log = LoggerFactory.getLogger(PaymentItemServiceImpl.class);

    private final PaymentItemRepository paymentItemRepository;

    private final PaymentItemMapper paymentItemMapper;

    public PaymentItemServiceImpl(PaymentItemRepository paymentItemRepository, PaymentItemMapper paymentItemMapper) {
        this.paymentItemRepository = paymentItemRepository;
        this.paymentItemMapper = paymentItemMapper;
    }

    @Override
    public PaymentItemDTO save(PaymentItemDTO paymentItemDTO) {
        log.debug("Request to save PaymentItem : {}", paymentItemDTO);
        PaymentItem paymentItem = paymentItemMapper.toEntity(paymentItemDTO);
        paymentItem = paymentItemRepository.save(paymentItem);
        return paymentItemMapper.toDto(paymentItem);
    }

    @Override
    public Optional<PaymentItemDTO> partialUpdate(PaymentItemDTO paymentItemDTO) {
        log.debug("Request to partially update PaymentItem : {}", paymentItemDTO);

        return paymentItemRepository
            .findById(paymentItemDTO.getId())
            .map(existingPaymentItem -> {
                paymentItemMapper.partialUpdate(existingPaymentItem, paymentItemDTO);

                return existingPaymentItem;
            })
            .map(paymentItemRepository::save)
            .map(paymentItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentItems");
        return paymentItemRepository.findAll(pageable).map(paymentItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentItemDTO> findOne(Long id) {
        log.debug("Request to get PaymentItem : {}", id);
        return paymentItemRepository.findById(id).map(paymentItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentItem : {}", id);
        paymentItemRepository.deleteById(id);
    }
}
