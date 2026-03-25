package com.studyspace.space.repository;

import com.studyspace.space.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    boolean existsByNameIgnoreCase(String name);
}