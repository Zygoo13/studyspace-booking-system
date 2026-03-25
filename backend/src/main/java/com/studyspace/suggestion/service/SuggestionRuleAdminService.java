package com.studyspace.suggestion.service;

import com.studyspace.common.exception.BadRequestException;
import com.studyspace.common.exception.ResourceNotFoundException;
import com.studyspace.suggestion.dto.request.CreateSuggestionRuleRequest;
import com.studyspace.suggestion.dto.request.UpdateSuggestionRuleRequest;
import com.studyspace.suggestion.dto.response.SuggestionRuleResponse;
import com.studyspace.suggestion.entity.SuggestionRule;
import com.studyspace.suggestion.mapper.SuggestionRuleMapper;
import com.studyspace.suggestion.repository.SuggestionRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionRuleAdminService {

    private final SuggestionRuleRepository suggestionRuleRepository;
    private final SuggestionRuleMapper suggestionRuleMapper;

    @Transactional(readOnly = true)
    public List<SuggestionRuleResponse> getAll() {
        return suggestionRuleRepository.findAllByOrderByPriorityDescIdAsc()
                .stream()
                .map(suggestionRuleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SuggestionRuleResponse getById(Long id) {
        return suggestionRuleMapper.toResponse(getSuggestionRuleOrThrow(id));
    }

    @Transactional
    public SuggestionRuleResponse create(CreateSuggestionRuleRequest request) {
        validateRanges(request.getMinPeople(), request.getMaxPeople(), request.getMinCapacity(), request.getMaxCapacity());

        SuggestionRule entity = SuggestionRule.builder()
                .minPeople(request.getMinPeople())
                .maxPeople(request.getMaxPeople())
                .targetSpaceType(request.getTargetSpaceType())
                .minCapacity(request.getMinCapacity())
                .maxCapacity(request.getMaxCapacity())
                .applicablePriceGroup(normalize(request.getApplicablePriceGroup()))
                .priority(request.getPriority())
                .isActive(request.getIsActive())
                .build();

        return suggestionRuleMapper.toResponse(suggestionRuleRepository.save(entity));
    }

    @Transactional
    public SuggestionRuleResponse update(Long id, UpdateSuggestionRuleRequest request) {
        validateRanges(request.getMinPeople(), request.getMaxPeople(), request.getMinCapacity(), request.getMaxCapacity());

        SuggestionRule entity = getSuggestionRuleOrThrow(id);

        entity.setMinPeople(request.getMinPeople());
        entity.setMaxPeople(request.getMaxPeople());
        entity.setTargetSpaceType(request.getTargetSpaceType());
        entity.setMinCapacity(request.getMinCapacity());
        entity.setMaxCapacity(request.getMaxCapacity());
        entity.setApplicablePriceGroup(normalize(request.getApplicablePriceGroup()));
        entity.setPriority(request.getPriority());
        entity.setIsActive(request.getIsActive());

        return suggestionRuleMapper.toResponse(suggestionRuleRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        SuggestionRule entity = getSuggestionRuleOrThrow(id);
        suggestionRuleRepository.delete(entity);
    }

    private SuggestionRule getSuggestionRuleOrThrow(Long id) {
        return suggestionRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion rule not found with id: " + id));
    }

    private void validateRanges(Integer minPeople, Integer maxPeople, Integer minCapacity, Integer maxCapacity) {
        if (minPeople > maxPeople) {
            throw new BadRequestException("Min people must be less than or equal to max people");
        }

        if (minCapacity != null && maxCapacity != null && minCapacity > maxCapacity) {
            throw new BadRequestException("Min capacity must be less than or equal to max capacity");
        }
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}