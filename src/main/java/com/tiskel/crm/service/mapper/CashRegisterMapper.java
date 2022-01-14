package com.tiskel.crm.service.mapper;

import com.tiskel.crm.domain.CashRegister;
import com.tiskel.crm.service.dto.CashRegisterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CashRegister} and its DTO {@link CashRegisterDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface CashRegisterMapper extends EntityMapper<CashRegisterDTO, CashRegister> {
    @Mapping(target = "company", source = "company", qualifiedByName = "id")
    CashRegisterDTO toDto(CashRegister s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CashRegisterDTO toDtoId(CashRegister cashRegister);
}
