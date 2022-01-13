package com.tiskel.crm.service.mapper;

import com.tiskel.crm.domain.PaymentItem;
import com.tiskel.crm.service.dto.PaymentItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentItem} and its DTO {@link PaymentItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { TerminalMapper.class, PaymentMapper.class })
public interface PaymentItemMapper extends EntityMapper<PaymentItemDTO, PaymentItem> {
    @Mapping(target = "terminal", source = "terminal", qualifiedByName = "id")
    @Mapping(target = "payment", source = "payment", qualifiedByName = "id")
    PaymentItemDTO toDto(PaymentItem s);
}
