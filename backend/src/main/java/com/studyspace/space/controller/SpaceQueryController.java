package com.studyspace.space.controller;

import com.studyspace.common.response.ApiResponse;
import com.studyspace.space.dto.response.SpaceUnitResponse;
import com.studyspace.space.service.SpaceQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceQueryController {

    private final SpaceQueryService spaceQueryService;

    @Operation(summary = "Get all spaces")
    @GetMapping
    public ApiResponse<List<SpaceUnitResponse>> getAll(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long floorId,
            @RequestParam(required = false) Long parentId
    ) {
        List<SpaceUnitResponse> data;

        if (parentId != null) {
            data = spaceQueryService.getChildren(parentId);
        } else if (floorId != null) {
            data = spaceQueryService.getByFloorId(floorId);
        } else if (branchId != null) {
            data = spaceQueryService.getByBranchId(branchId);
        } else {
            data = spaceQueryService.getAll();
        }

        return ApiResponse.success("Get spaces successfully", data, HttpStatus.OK.value());
    }
}