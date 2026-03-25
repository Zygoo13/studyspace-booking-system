package com.studyspace.space.service;

import com.studyspace.common.exception.ConflictException;
import com.studyspace.common.exception.ResourceNotFoundException;
import com.studyspace.space.dto.request.CreateFloorRequest;
import com.studyspace.space.dto.request.UpdateFloorRequest;
import com.studyspace.space.dto.response.FloorResponse;
import com.studyspace.space.entity.Branch;
import com.studyspace.space.entity.Floor;
import com.studyspace.space.mapper.FloorMapper;
import com.studyspace.space.repository.BranchRepository;
import com.studyspace.space.repository.FloorRepository;
import com.studyspace.space.repository.SpaceUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FloorAdminService {

    private final FloorRepository floorRepository;
    private final BranchRepository branchRepository;
    private final SpaceUnitRepository spaceUnitRepository;
    private final FloorMapper floorMapper;

    @Transactional(readOnly = true)
    public List<FloorResponse> getAll() {
        return floorRepository.findAll()
                .stream()
                .map(floorMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FloorResponse getById(Long id) {
        return floorMapper.toResponse(getFloorOrThrow(id));
    }

    @Transactional
    public FloorResponse create(CreateFloorRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));

        if (floorRepository.existsByBranchIdAndNameIgnoreCase(branch.getId(), request.getName().trim())) {
            throw new ConflictException("Floor name already exists in this branch");
        }

        Floor floor = Floor.builder()
                .branch(branch)
                .name(request.getName().trim())
                .status(request.getStatus())
                .build();

        return floorMapper.toResponse(floorRepository.save(floor));
    }

    @Transactional
    public FloorResponse update(Long id, UpdateFloorRequest request) {
        Floor floor = getFloorOrThrow(id);

        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));

        if (floorRepository.existsByBranchIdAndNameIgnoreCaseAndIdNot(branch.getId(), request.getName().trim(), id)) {
            throw new ConflictException("Floor name already exists in this branch");
        }

        floor.setBranch(branch);
        floor.setName(request.getName().trim());
        floor.setStatus(request.getStatus());

        return floorMapper.toResponse(floorRepository.save(floor));
    }

    @Transactional
    public void delete(Long id) {
        Floor floor = getFloorOrThrow(id);

        if (spaceUnitRepository.existsByFloorId(id)) {
            throw new ConflictException("Cannot delete floor because it still has space units");
        }

        floorRepository.delete(floor);
    }

    private Floor getFloorOrThrow(Long id) {
        return floorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + id));
    }
}