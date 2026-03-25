package com.studyspace.combo.entity;

import com.studyspace.common.entity.BaseEntity;
import com.studyspace.space.enums.SpaceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "combo_plans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicable_space_type", nullable = false, length = 30)
    private SpaceType applicableSpaceType;

    @Column(name = "min_capacity")
    private Integer minCapacity;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "applicable_price_group", length = 50)
    private String applicablePriceGroup;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}