package com.tiskel.crm.service.mapper;

import com.tiskel.crm.domain.Receipt;
import com.tiskel.crm.service.dto.ReceiptDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Receipt} and its DTO {@link ReceiptDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class, CashRegisterMapper.class, TerminalMapper.class })
public interface ReceiptMapper extends EntityMapper<ReceiptDTO, Receipt> {
    @Mapping(target = "company", source = "company", qualifiedByName = "id")
    @Mapping(target = "cashRegister", source = "cashRegister", qualifiedByName = "id")
    @Mapping(target = "terminal", source = "terminal", qualifiedByName = "id")
    ReceiptDTO toDto(Receipt s);
}
