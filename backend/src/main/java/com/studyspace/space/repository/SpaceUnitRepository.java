package com.studyspace.space.repository;

import com.studyspace.space.entity.SpaceUnit;
import com.studyspace.space.enums.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceUnitRepository extends JpaRepository<SpaceUnit, Long> {

    List<SpaceUnit> findAllByBranchIdOrderByIdAsc(Long branchId);

    List<SpaceUnit> findAllByParentIdOrderByIdAsc(Long parentId);

    List<SpaceUnit> findAllBySpaceTypeOrderByIdAsc(SpaceType spaceType);

    List<SpaceUnit> findAllByFloorIdOrderByIdAsc(Long floorId);

    boolean existsByParentId(Long parentId);

    boolean existsByFloorId(Long floorId);

    boolean existsByBranchIdAndParentIdAndNameIgnoreCase(Long branchId, Long parentId, String name);

    boolean existsByBranchIdAndParentIdAndNameIgnoreCaseAndIdNot(Long branchId, Long parentId, String name, Long id);
}