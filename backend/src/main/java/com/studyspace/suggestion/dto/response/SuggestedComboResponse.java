package com.studyspace.suggestion.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SuggestedComboResponse {

    private Long id;
    private String name;
    private Integer durationMinutes;
    private BigDecimal price;
}