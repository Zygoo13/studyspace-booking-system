package com.studyspace.space.dto.request;

import com.studyspace.space.enums.SpaceStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSpaceUnitRequest {

    @NotBlank(message = "Space name must not be blank")
    @Size(max = 100, message = "Space name must be at most 100 characters")
    private String name;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotNull(message = "Directly rentable flag is required")
    private Boolean isDirectlyRentable;

    @NotNull(message = "Status is required")
    private SpaceStatus status;

    @Size(max = 50, message = "Price group must be at most 50 characters")
    private String priceGroup;
}