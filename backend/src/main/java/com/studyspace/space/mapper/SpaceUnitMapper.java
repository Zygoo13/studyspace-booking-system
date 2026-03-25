package com.studyspace.space.mapper;

import com.studyspace.space.dto.response.SpaceUnitResponse;
import com.studyspace.space.entity.SpaceUnit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpaceUnitMapper {

    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "branchName", source = "branch.name")
    @Mapping(target = "floorId", source = "floor.id")
    @Mapping(target = "floorName", source = "floor.name")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    SpaceUnitResponse toResponse(SpaceUnit spaceUnit);
}