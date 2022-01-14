package com.tiskel.crm.service.mapper;

import com.tiskel.crm.domain.Setting;
import com.tiskel.crm.service.dto.SettingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Setting} and its DTO {@link SettingDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SettingMapper extends EntityMapper<SettingDTO, Setting> {}
