package com.studyspace.suggestion.entity;

import com.studyspace.common.entity.BaseEntity;
import com.studyspace.space.enums.SpaceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suggestion_rules")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_people", nullable = false)
    private Integer minPeople;

    @Column(name = "max_people", nullable = false)
    private Integer maxPeople;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_space_type", nullable = false, length = 30)
    private SpaceType targetSpaceType;

    @Column(name = "min_capacity")
    private Integer minCapacity;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "applicable_price_group", length = 50)
    private String applicablePriceGroup;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}