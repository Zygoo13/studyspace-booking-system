package com.studyspace.combo.dto.response;

import com.studyspace.space.enums.SpaceType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ComboPlanResponse {

    private Long id;
    private String name;
    private Integer durationMinutes;
    private BigDecimal price;
    private SpaceType applicableSpaceType;
    private Integer minCapacity;
    private Integer maxCapacity;
    private String applicablePriceGroup;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}