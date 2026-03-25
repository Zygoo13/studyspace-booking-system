package com.studyspace.combo.service;

import com.studyspace.combo.dto.response.ComboPlanResponse;
import com.studyspace.combo.mapper.ComboPlanMapper;
import com.studyspace.combo.repository.ComboPlanRepository;
import com.studyspace.space.enums.SpaceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComboPlanQueryService {

    private final ComboPlanRepository comboPlanRepository;
    private final ComboPlanMapper comboPlanMapper;

    @Transactional(readOnly = true)
    public List<ComboPlanResponse> getActiveBySpaceType(SpaceType spaceType) {
        return comboPlanRepository.findAllByApplicableSpaceTypeAndIsActiveTrueOrderByDurationMinutesAscIdAsc(spaceType)
                .stream()
                .map(comboPlanMapper::toResponse)
                .toList();
    }
}