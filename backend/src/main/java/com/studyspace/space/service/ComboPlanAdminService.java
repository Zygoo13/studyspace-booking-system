package com.studyspace.combo.service;

import com.studyspace.combo.dto.request.CreateComboPlanRequest;
import com.studyspace.combo.dto.request.UpdateComboPlanRequest;
import com.studyspace.combo.dto.response.ComboPlanResponse;
import com.studyspace.combo.entity.ComboPlan;
import com.studyspace.combo.mapper.ComboPlanMapper;
import com.studyspace.combo.repository.ComboPlanRepository;
import com.studyspace.common.exception.BadRequestException;
import com.studyspace.common.exception.ConflictException;
import com.studyspace.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComboPlanAdminService {

    private final ComboPlanRepository comboPlanRepository;
    private final ComboPlanMapper comboPlanMapper;

    @Transactional(readOnly = true)
    public List<ComboPlanResponse> getAll() {
        return comboPlanRepository.findAll()
                .stream()
                .map(comboPlanMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ComboPlanResponse getById(Long id) {
        return comboPlanMapper.toResponse(getComboPlanOrThrow(id));
    }

    @Transactional
    public ComboPlanResponse create(CreateComboPlanRequest request) {
        validateCapacityRange(request.getMinCapacity(), request.getMaxCapacity());

        if (comboPlanRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new ConflictException("Combo name already exists");
        }

        ComboPlan comboPlan = ComboPlan.builder()
                .name(request.getName().trim())
                .durationMinutes(request.getDurationMinutes())
                .price(request.getPrice())
                .applicableSpaceType(request.getApplicableSpaceType())
                .minCapacity(request.getMinCapacity())
                .maxCapacity(request.getMaxCapacity())
                .applicablePriceGroup(normalize(request.getApplicablePriceGroup()))
                .isActive(request.getIsActive())
                .build();

        return comboPlanMapper.toResponse(comboPlanRepository.save(comboPlan));
    }

    @Transactional
    public ComboPlanResponse update(Long id, UpdateComboPlanRequest request) {
        validateCapacityRange(request.getMinCapacity(), request.getMaxCapacity());

        ComboPlan comboPlan = getComboPlanOrThrow(id);

        if (comboPlanRepository.existsByNameIgnoreCaseAndIdNot(request.getName().trim(), id)) {
            throw new ConflictException("Combo name already exists");
        }

        comboPlan.setName(request.getName().trim());
        comboPlan.setDurationMinutes(request.getDurationMinutes());
        comboPlan.setPrice(request.getPrice());
        comboPlan.setApplicableSpaceType(request.getApplicableSpaceType());
        comboPlan.setMinCapacity(request.getMinCapacity());
        comboPlan.setMaxCapacity(request.getMaxCapacity());
        comboPlan.setApplicablePriceGroup(normalize(request.getApplicablePriceGroup()));
        comboPlan.setIsActive(request.getIsActive());

        return comboPlanMapper.toResponse(comboPlanRepository.save(comboPlan));
    }

    @Transactional
    public void delete(Long id) {
        ComboPlan comboPlan = getComboPlanOrThrow(id);
        comboPlanRepository.delete(comboPlan);
    }

    private ComboPlan getComboPlanOrThrow(Long id) {
        return comboPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Combo plan not found with id: " + id));
    }

    private void validateCapacityRange(Integer minCapacity, Integer maxCapacity) {
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