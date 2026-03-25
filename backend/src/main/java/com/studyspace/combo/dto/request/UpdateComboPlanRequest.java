package com.studyspace.combo.dto.request;

import com.studyspace.space.enums.SpaceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateComboPlanRequest {

    @NotBlank(message = "Combo name must not be blank")
    @Size(max = 150, message = "Combo name must be at most 150 characters")
    private String name;

    @NotNull(message = "Duration minutes is required")
    @Min(value = 1, message = "Duration minutes must be at least 1")
    private Integer durationMinutes;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Applicable space type is required")
    private SpaceType applicableSpaceType;

    @Min(value = 1, message = "Min capacity must be at least 1")
    private Integer minCapacity;

    @Min(value = 1, message = "Max capacity must be at least 1")
    private Integer maxCapacity;

    @Size(max = 50, message = "Applicable price group must be at most 50 characters")
    private String applicablePriceGroup;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;
}