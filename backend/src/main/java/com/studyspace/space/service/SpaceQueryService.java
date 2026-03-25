package com.studyspace.space.service;

import com.studyspace.space.dto.response.SpaceUnitResponse;
import com.studyspace.space.mapper.SpaceUnitMapper;
import com.studyspace.space.repository.SpaceUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceQueryService {

    private final SpaceUnitRepository spaceUnitRepository;
    private final SpaceUnitMapper spaceUnitMapper;

    @Transactional(readOnly = true)
    public List<SpaceUnitResponse> getAll() {
        return spaceUnitRepository.findAll()
                .stream()
                .map(spaceUnitMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SpaceUnitResponse> getByBranchId(Long branchId) {
        return spaceUnitRepository.findAllByBranchIdOrderByIdAsc(branchId)
                .stream()
                .map(spaceUnitMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SpaceUnitResponse> getByFloorId(Long floorId) {
        return spaceUnitRepository.findAllByFloorIdOrderByIdAsc(floorId)
                .stream()
                .map(spaceUnitMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SpaceUnitResponse> getChildren(Long parentId) {
        return spaceUnitRepository.findAllByParentIdOrderByIdAsc(parentId)
                .stream()
                .map(spaceUnitMapper::toResponse)
                .toList();
    }
}