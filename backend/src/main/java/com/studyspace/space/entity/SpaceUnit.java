package com.studyspace.space.entity;

import com.studyspace.common.entity.BaseEntity;
import com.studyspace.space.enums.SpaceStatus;
import com.studyspace.space.enums.SpaceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "space_units")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceUnit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SpaceUnit parent;

    @Enumerated(EnumType.STRING)
    @Column(name = "space_type", nullable = false, length = 30)
    private SpaceType spaceType;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Builder.Default
    @Column(name = "is_directly_rentable", nullable = false)
    private Boolean isDirectlyRentable = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private SpaceStatus status;

    @Column(name = "price_group", length = 50)
    private String priceGroup;
}