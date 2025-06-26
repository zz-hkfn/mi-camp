package com.xiaomi.cug.dto;

import com.xiaomi.cug.entity.WarningRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarnRuleDTOMapper {
        @Mapping(source = "warnId", target = "id")
        WarningRule toEntity(WarnDTO warnDTO);
        @Mapping(source = "warnId", target = "id")
        List<WarningRule> toList(List<WarnDTO> warnDTOS);
}
