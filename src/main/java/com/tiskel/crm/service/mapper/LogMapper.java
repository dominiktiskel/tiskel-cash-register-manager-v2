package com.tiskel.crm.service.mapper;

import com.tiskel.crm.domain.Log;
import com.tiskel.crm.service.dto.LogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Log} and its DTO {@link LogDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LogMapper extends EntityMapper<LogDTO, Log> {}
