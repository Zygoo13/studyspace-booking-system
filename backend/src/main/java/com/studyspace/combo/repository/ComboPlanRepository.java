package com.studyspace.combo.repository;

import com.studyspace.combo.entity.ComboPlan;
import com.studyspace.space.enums.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComboPlanRepository extends JpaRepository<ComboPlan, Long> {

    List<ComboPlan> findAllByIsActiveTrueOrderByIdAsc();

    List<ComboPlan> findAllByApplicableSpaceTypeAndIsActiveTrueOrderByIdAsc(SpaceType applicableSpaceType);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}