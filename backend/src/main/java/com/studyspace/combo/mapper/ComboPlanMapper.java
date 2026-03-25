package com.studyspace.combo.mapper;

import com.studyspace.combo.dto.response.ComboPlanResponse;
import com.studyspace.combo.entity.ComboPlan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComboPlanMapper {

    ComboPlanResponse toResponse(ComboPlan comboPlan);
}