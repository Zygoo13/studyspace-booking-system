package com.studyspace.suggestion.mapper;

import com.studyspace.suggestion.dto.response.SuggestionRuleResponse;
import com.studyspace.suggestion.entity.SuggestionRule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SuggestionRuleMapper {

    SuggestionRuleResponse toResponse(SuggestionRule suggestionRule);
}