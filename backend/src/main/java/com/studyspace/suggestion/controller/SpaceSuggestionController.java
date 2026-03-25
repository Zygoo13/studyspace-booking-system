package com.studyspace.suggestion.controller;

import com.studyspace.common.response.ApiResponse;
import com.studyspace.suggestion.dto.request.SuggestSpaceRequest;
import com.studyspace.suggestion.dto.response.SuggestedSpaceResponse;
import com.studyspace.suggestion.service.SpaceSuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
@RequiredArgsConstructor
public class SpaceSuggestionController {

    private final SpaceSuggestionService spaceSuggestionService;

    @Operation(summary = "Suggest spaces and combos for customer request")
    @PostMapping("/spaces")
    public ApiResponse<List<SuggestedSpaceResponse>> suggestSpaces(@Valid @RequestBody SuggestSpaceRequest request) {
        return ApiResponse.success(
                "Suggest spaces successfully",
                spaceSuggestionService.suggest(request),
                HttpStatus.OK.value()
        );
    }
}