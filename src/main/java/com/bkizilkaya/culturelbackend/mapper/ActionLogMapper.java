package com.bkizilkaya.culturelbackend.mapper;

import com.bkizilkaya.culturelbackend.dto.actionlog.ActionLogDTO;
import com.bkizilkaya.culturelbackend.model.ActionLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper

public interface ActionLogMapper {
    ActionLogMapper INSTANCE = Mappers.getMapper(ActionLogMapper.class);

    ActionLogDTO entityToDto(ActionLog actionLog);

}
