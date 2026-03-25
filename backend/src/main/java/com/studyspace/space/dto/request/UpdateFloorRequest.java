package com.studyspace.space.dto.request;

import com.studyspace.space.enums.SpaceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFloorRequest {

    @NotNull(message = "Branch id is required")
    private Long branchId;

    @NotBlank(message = "Floor name must not be blank")
    @Size(max = 100, message = "Floor name must be at most 100 characters")
    private String name;

    @NotNull(message = "Status is required")
    private SpaceStatus status;
}