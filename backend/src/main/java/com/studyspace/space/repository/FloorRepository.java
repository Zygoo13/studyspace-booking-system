package com.studyspace.space.repository;

import com.studyspace.space.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Long> {

    List<Floor> findAllByBranchIdOrderByIdAsc(Long branchId);

    boolean existsByBranchIdAndNameIgnoreCase(Long branchId, String name);

    boolean existsByBranchIdAndNameIgnoreCaseAndIdNot(Long branchId, String name, Long id);
}