package com.studyspace.space.controller;

import com.studyspace.common.constant.ApiPaths;
import com.studyspace.common.response.ApiResponse;
import com.studyspace.space.dto.request.CreateFloorRequest;
import com.studyspace.space.dto.request.UpdateFloorRequest;
import com.studyspace.space.dto.response.FloorResponse;
import com.studyspace.space.service.FloorAdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_ADMIN + "/floors")
@RequiredArgsConstructor
public class AdminFloorController {

    private final FloorAdminService floorAdminService;

    @Operation(summary = "Get all floors")
    @GetMapping
    public ApiResponse<List<FloorResponse>> getAll() {
        return ApiResponse.success("Get floors successfully", floorAdminService.getAll(), HttpStatus.OK.value());
    }

    @Operation(summary = "Get floor by id")
    @GetMapping("/{id}")
    public ApiResponse<FloorResponse> getById(@PathVariable Long id) {
        return ApiResponse.success("Get floor successfully", floorAdminService.getById(id), HttpStatus.OK.value());
    }

    @Operation(summary = "Create floor")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<FloorResponse> create(@Valid @RequestBody CreateFloorRequest request) {
        return ApiResponse.success("Create floor successfully", floorAdminService.create(request), HttpStatus.CREATED.value());
    }

    @Operation(summary = "Update floor")
    @PutMapping("/{id}")
    public ApiResponse<FloorResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateFloorRequest request) {
        return ApiResponse.success("Update floor successfully", floorAdminService.update(id, request), HttpStatus.OK.value());
    }

    @Operation(summary = "Delete floor")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        floorAdminService.delete(id);
        return ApiResponse.success("Delete floor successfully", null, HttpStatus.OK.value());
    }
}