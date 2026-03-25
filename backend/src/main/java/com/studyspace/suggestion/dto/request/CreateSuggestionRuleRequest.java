package com.studyspace.suggestion.dto.request;

import com.studyspace.space.enums.SpaceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSuggestionRuleRequest {

    @NotNull(message = "Min people is required")
    @Min(value = 1, message = "Min people must be at least 1")
    private Integer minPeople;

    @NotNull(message = "Max people is required")
    @Min(value = 1, message = "Max people must be at least 1")
    private Integer maxPeople;

    @NotNull(message = "Target space type is required")
    private SpaceType targetSpaceType;

    @Min(value = 1, message = "Min capacity must be at least 1")
    private Integer minCapacity;

    @Min(value = 1, message = "Max capacity must be at least 1")
    private Integer maxCapacity;

    @Size(max = 50, message = "Applicable price group must be at most 50 characters")
    private String applicablePriceGroup;

    @NotNull(message = "Priority is required")
    private Integer priority;

    @NotNull(message = "Active flag is required")
    private Boolean isActive;
}