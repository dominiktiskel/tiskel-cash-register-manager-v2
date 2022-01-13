package com.tiskel.crm.service.mapper;

import com.tiskel.crm.domain.Payment;
import com.tiskel.crm.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, InvoiceMapper.class })
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "company", source = "company", qualifiedByName = "id")
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "id")
    PaymentDTO toDto(Payment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoId(Payment payment);
}
