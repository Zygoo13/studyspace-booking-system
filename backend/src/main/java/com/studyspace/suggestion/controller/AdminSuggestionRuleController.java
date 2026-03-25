package com.studyspace.suggestion.controller;

import com.studyspace.common.constant.ApiPaths;
import com.studyspace.common.response.ApiResponse;
import com.studyspace.suggestion.dto.request.CreateSuggestionRuleRequest;
import com.studyspace.suggestion.dto.request.UpdateSuggestionRuleRequest;
import com.studyspace.suggestion.dto.response.SuggestionRuleResponse;
import com.studyspace.suggestion.service.SuggestionRuleAdminService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_ADMIN + "/suggestion-rules")
@RequiredArgsConstructor
public class AdminSuggestionRuleController {

    private final SuggestionRuleAdminService suggestionRuleAdminService;

    @Operation(summary = "Get all suggestion rules")
    @GetMapping
    public ApiResponse<List<SuggestionRuleResponse>> getAll() {
        return ApiResponse.success("Get suggestion rules successfully", suggestionRuleAdminService.getAll(), HttpStatus.OK.value());
    }

    @Operation(summary = "Get suggestion rule by id")
    @GetMapping("/{id}")
    public ApiResponse<SuggestionRuleResponse> getById(@PathVariable Long id) {
        return ApiResponse.success("Get suggestion rule successfully", suggestionRuleAdminService.getById(id), HttpStatus.OK.value());
    }

    @Operation(summary = "Create suggestion rule")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SuggestionRuleResponse> create(@Valid @RequestBody CreateSuggestionRuleRequest request) {
        return ApiResponse.success("Create suggestion rule successfully", suggestionRuleAdminService.create(request), HttpStatus.CREATED.value());
    }

    @Operation(summary = "Update suggestion rule")
    @PutMapping("/{id}")
    public ApiResponse<SuggestionRuleResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateSuggestionRuleRequest request) {
        return ApiResponse.success("Update suggestion rule successfully", suggestionRuleAdminService.update(id, request), HttpStatus.OK.value());
    }

    @Operation(summary = "Delete suggestion rule")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        suggestionRuleAdminService.delete(id);
        return ApiResponse.success("Delete suggestion rule successfully", null, HttpStatus.OK.value());
    }
}