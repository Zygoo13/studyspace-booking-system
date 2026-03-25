package com.studyspace.space.mapper;

import com.studyspace.space.dto.response.FloorResponse;
import com.studyspace.space.entity.Floor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FloorMapper {

    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    FloorResponse toResponse(Floor floor);
}