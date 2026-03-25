package com.studyspace.space.controller;

import com.studyspace.common.constant.ApiPaths;
import com.studyspace.common.response.ApiResponse;
import com.studyspace.space.dto.request.CreateSpaceUnitRequest;
import com.studyspace.space.dto.request.UpdateSpaceUnitRequest;
import com.studyspace.space.dto.response.SpaceUnitResponse;
import com.studyspace.space.service.SpaceUnitAdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_ADMIN + "/space-units")
@RequiredArgsConstructor
public class AdminSpaceUnitController {

    private final SpaceUnitAdminService spaceUnitAdminService;

    @Operation(summary = "Get all space units")
    @GetMapping
    public ApiResponse<List<SpaceUnitResponse>> getAll() {
        return ApiResponse.success("Get space units successfully", spaceUnitAdminService.getAll(), HttpStatus.OK.value());
    }

    @Operation(summary = "Get space unit by id")
    @GetMapping("/{id}")
    public ApiResponse<SpaceUnitResponse> getById(@PathVariable Long id) {
        return ApiResponse.success("Get space unit successfully", spaceUnitAdminService.getById(id), HttpStatus.OK.value());
    }

    @Operation(summary = "Create space unit")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SpaceUnitResponse> create(@Valid @RequestBody CreateSpaceUnitRequest request) {
        return ApiResponse.success("Create space unit successfully", spaceUnitAdminService.create(request), HttpStatus.CREATED.value());
    }

    @Operation(summary = "Update space unit")
    @PutMapping("/{id}")
    public ApiResponse<SpaceUnitResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateSpaceUnitRequest request) {
        return ApiResponse.success("Update space unit successfully", spaceUnitAdminService.update(id, request), HttpStatus.OK.value());
    }

    @Operation(summary = "Delete space unit")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        spaceUnitAdminService.delete(id);
        return ApiResponse.success("Delete space unit successfully", null, HttpStatus.OK.value());
    }
}