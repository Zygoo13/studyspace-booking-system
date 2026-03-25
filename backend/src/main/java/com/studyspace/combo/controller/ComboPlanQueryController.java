package com.studyspace.combo.controller;

import com.studyspace.combo.dto.response.ComboPlanResponse;
import com.studyspace.combo.service.ComboPlanQueryService;
import com.studyspace.common.response.ApiResponse;
import com.studyspace.space.enums.SpaceType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/combo-plans")
@RequiredArgsConstructor
public class ComboPlanQueryController {

    private final ComboPlanQueryService comboPlanQueryService;

    @Operation(summary = "Get active combo plans by space type")
    @GetMapping
    public ApiResponse<List<ComboPlanResponse>> getActiveBySpaceType(@RequestParam SpaceType spaceType) {
        return ApiResponse.success(
                "Get combo plans successfully",
                comboPlanQueryService.getActiveBySpaceType(spaceType),
                HttpStatus.OK.value()
        );
    }
}