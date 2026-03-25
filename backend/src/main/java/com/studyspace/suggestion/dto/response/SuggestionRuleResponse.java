package com.studyspace.suggestion.dto.response;

import com.studyspace.space.enums.SpaceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SuggestionRuleResponse {

    private Long id;
    private Integer minPeople;
    private Integer maxPeople;
    private SpaceType targetSpaceType;
    private Integer minCapacity;
    private Integer maxCapacity;
    private String applicablePriceGroup;
    private Integer priority;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}