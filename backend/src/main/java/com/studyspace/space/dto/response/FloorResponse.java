package com.studyspace.space.dto.response;

import com.studyspace.space.enums.SpaceStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FloorResponse {

    private Long id;
    private Long branchId;
    private String branchName;
    private String name;
    private SpaceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}