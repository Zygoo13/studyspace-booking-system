package com.studyspace.suggestion.repository;

import com.studyspace.suggestion.entity.SuggestionRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionRuleRepository extends JpaRepository<SuggestionRule, Long> {

    List<SuggestionRule> findAllByIsActiveTrueOrderByPriorityDescIdAsc();

    List<SuggestionRule> findAllByOrderByPriorityDescIdAsc();
}