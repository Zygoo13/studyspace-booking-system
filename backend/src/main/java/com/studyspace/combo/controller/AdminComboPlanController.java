package com.studyspace.combo.controller;

import com.studyspace.combo.dto.request.CreateComboPlanRequest;
import com.studyspace.combo.dto.request.UpdateComboPlanRequest;
import com.studyspace.combo.dto.response.ComboPlanResponse;
import com.studyspace.combo.service.ComboPlanAdminService;
import com.studyspace.common.constant.ApiPaths;
import com.studyspace.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_ADMIN + "/combo-plans")
@RequiredArgsConstructor
public class AdminComboPlanController {

    private final ComboPlanAdminService comboPlanAdminService;

    @Operation(summary = "Get all combo plans")
    @GetMapping
    public ApiResponse<List<ComboPlanResponse>> getAll() {
        return ApiResponse.success("Get combo plans successfully", comboPlanAdminService.getAll(), HttpStatus.OK.value());
    }

    @Operation(summary = "Get combo plan by id")
    @GetMapping("/{id}")
    public ApiResponse<ComboPlanResponse> getById(@PathVariable Long id) {
        return ApiResponse.success("Get combo plan successfully", comboPlanAdminService.getById(id), HttpStatus.OK.value());
    }

    @Operation(summary = "Create combo plan")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ComboPlanResponse> create(@Valid @RequestBody CreateComboPlanRequest request) {
        return ApiResponse.success("Create combo plan successfully", comboPlanAdminService.create(request), HttpStatus.CREATED.value());
    }

    @Operation(summary = "Update combo plan")
    @PutMapping("/{id}")
    public ApiResponse<ComboPlanResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateComboPlanRequest request) {
        return ApiResponse.success("Update combo plan successfully", comboPlanAdminService.update(id, request), HttpStatus.OK.value());
    }

    @Operation(summary = "Delete combo plan")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        comboPlanAdminService.delete(id);
        return ApiResponse.success("Delete combo plan successfully", null, HttpStatus.OK.value());
    }
}