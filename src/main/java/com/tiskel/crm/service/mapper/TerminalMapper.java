package com.tiskel.crm.service.mapper;

import com.tiskel.crm.domain.Terminal;
import com.tiskel.crm.service.dto.TerminalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Terminal} and its DTO {@link TerminalDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface TerminalMapper extends EntityMapper<TerminalDTO, Terminal> {
    @Mapping(target = "company", source = "company", qualifiedByName = "id")
    TerminalDTO toDto(Terminal s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TerminalDTO toDtoId(Terminal terminal);
}
