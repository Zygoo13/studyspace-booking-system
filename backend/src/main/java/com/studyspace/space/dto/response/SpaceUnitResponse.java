package com.studyspace.space.dto.response;

import com.studyspace.space.enums.SpaceStatus;
import com.studyspace.space.enums.SpaceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SpaceUnitResponse {

    private Long id;
    private Long branchId;
    private String branchName;
    private Long floorId;
    private String floorName;
    private Long parentId;
    private String parentName;
    private SpaceType spaceType;
    private String name;
    private Integer capacity;
    private Boolean isDirectlyRentable;
    private SpaceStatus status;
    private String priceGroup;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}