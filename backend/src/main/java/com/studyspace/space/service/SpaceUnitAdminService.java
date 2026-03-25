package com.studyspace.space.service;

import com.studyspace.common.exception.BadRequestException;
import com.studyspace.common.exception.ConflictException;
import com.studyspace.common.exception.ResourceNotFoundException;
import com.studyspace.space.dto.request.CreateSpaceUnitRequest;
import com.studyspace.space.dto.request.UpdateSpaceUnitRequest;
import com.studyspace.space.dto.response.SpaceUnitResponse;
import com.studyspace.space.entity.Branch;
import com.studyspace.space.entity.Floor;
import com.studyspace.space.entity.SpaceUnit;
import com.studyspace.space.enums.SpaceType;
import com.studyspace.space.mapper.SpaceUnitMapper;
import com.studyspace.space.repository.BranchRepository;
import com.studyspace.space.repository.FloorRepository;
import com.studyspace.space.repository.SpaceUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceUnitAdminService {

    private final SpaceUnitRepository spaceUnitRepository;
    private final BranchRepository branchRepository;
    private final FloorRepository floorRepository;
    private final SpaceUnitMapper spaceUnitMapper;

    @Transactional(readOnly = true)
    public List<SpaceUnitResponse> getAll() {
        return spaceUnitRepository.findAll()
                .stream()
                .map(spaceUnitMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SpaceUnitResponse getById(Long id) {
        return spaceUnitMapper.toResponse(getSpaceUnitOrThrow(id));
    }

    @Transactional
    public SpaceUnitResponse create(CreateSpaceUnitRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + request.getBranchId()));

        Floor floor = resolveFloor(request.getFloorId());
        SpaceUnit parent = resolveParent(request.getParentId());

        validateCreateRules(request, branch, floor, parent);

        Long parentId = parent != null ? parent.getId() : null;
        if (spaceUnitRepository.existsByBranchIdAndParentIdAndNameIgnoreCase(branch.getId(), parentId, request.getName().trim())) {
            throw new ConflictException("Space name already exists in the same level");
        }

        SpaceUnit entity = SpaceUnit.builder()
                .branch(branch)
                .floor(floor)
                .parent(parent)
                .spaceType(request.getSpaceType())
                .name(request.getName().trim())
                .capacity(request.getCapacity())
                .isDirectlyRentable(request.getIsDirectlyRentable())
                .status(request.getStatus())
                .priceGroup(normalize(request.getPriceGroup()))
                .build();

        return spaceUnitMapper.toResponse(spaceUnitRepository.save(entity));
    }

    @Transactional
    public SpaceUnitResponse update(Long id, UpdateSpaceUnitRequest request) {
        SpaceUnit entity = getSpaceUnitOrThrow(id);

        Long parentId = entity.getParent() != null ? entity.getParent().getId() : null;
        if (spaceUnitRepository.existsByBranchIdAndParentIdAndNameIgnoreCaseAndIdNot(
                entity.getBranch().getId(),
                parentId,
                request.getName().trim(),
                id
        )) {
            throw new ConflictException("Space name already exists in the same level");
        }

        if (entity.getSpaceType() == SpaceType.ROOM && Boolean.TRUE.equals(request.getIsDirectlyRentable()) && entity.getFloor() == null) {
            throw new BadRequestException("Room must belong to a floor");
        }

        if (entity.getSpaceType() != SpaceType.ROOM && entity.getFloor() != null) {
            throw new BadRequestException("Only ROOM may store floorId");
        }

        entity.setName(request.getName().trim());
        entity.setCapacity(request.getCapacity());
        entity.setIsDirectlyRentable(request.getIsDirectlyRentable());
        entity.setStatus(request.getStatus());
        entity.setPriceGroup(normalize(request.getPriceGroup()));

        return spaceUnitMapper.toResponse(spaceUnitRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        SpaceUnit entity = getSpaceUnitOrThrow(id);

        if (spaceUnitRepository.existsByParentId(id)) {
            throw new ConflictException("Cannot delete space unit because it still has child space units");
        }

        spaceUnitRepository.delete(entity);
    }

    private void validateCreateRules(CreateSpaceUnitRequest request, Branch branch, Floor floor, SpaceUnit parent) {
        SpaceType spaceType = request.getSpaceType();

        if (spaceType == SpaceType.ROOM) {
            if (parent != null) {
                throw new BadRequestException("ROOM must not have parent");
            }
            if (floor == null) {
                throw new BadRequestException("ROOM must have floorId");
            }
            if (!floor.getBranch().getId().equals(branch.getId())) {
                throw new BadRequestException("Floor does not belong to the given branch");
            }
        }

        if (spaceType == SpaceType.TABLE) {
            if (parent == null) {
                throw new BadRequestException("TABLE must have parent ROOM");
            }
            if (parent.getSpaceType() != SpaceType.ROOM) {
                throw new BadRequestException("TABLE parent must be ROOM");
            }
            if (floor != null) {
                throw new BadRequestException("TABLE must not store floorId directly");
            }
            if (!parent.getBranch().getId().equals(branch.getId())) {
                throw new BadRequestException("Parent does not belong to the given branch");
            }
        }

        if (spaceType == SpaceType.SEAT) {
            if (parent == null) {
                throw new BadRequestException("SEAT must have parent TABLE");
            }
            if (parent.getSpaceType() != SpaceType.TABLE) {
                throw new BadRequestException("SEAT parent must be TABLE");
            }
            if (floor != null) {
                throw new BadRequestException("SEAT must not store floorId directly");
            }
            if (!parent.getBranch().getId().equals(branch.getId())) {
                throw new BadRequestException("Parent does not belong to the given branch");
            }
        }
    }

    private Floor resolveFloor(Long floorId) {
        if (floorId == null) {
            return null;
        }
        return floorRepository.findById(floorId)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + floorId));
    }

    private SpaceUnit resolveParent(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return spaceUnitRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent space unit not found with id: " + parentId));
    }

    private SpaceUnit getSpaceUnitOrThrow(Long id) {
        return spaceUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Space unit not found with id: " + id));
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}