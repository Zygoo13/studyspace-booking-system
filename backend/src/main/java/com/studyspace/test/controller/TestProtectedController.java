package com.studyspace.test.controller;

import com.studyspace.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestProtectedController {

    @GetMapping("/authenticated")
    public ApiResponse<Map<String, Object>> authenticated(Authentication authentication) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", "You are authenticated");
        data.put("name", authentication.getName());

        return ApiResponse.success("Authenticated access success", data, HttpStatus.OK.value());
    }

    @GetMapping("/staff")
    public ApiResponse<Map<String, Object>> staff(Authentication authentication) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", "You have STAFF access");
        data.put("name", authentication.getName());

        return ApiResponse.success("Staff access success", data, HttpStatus.OK.value());
    }

    @GetMapping("/admin")
    public ApiResponse<Map<String, Object>> admin(Authentication authentication) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", "You have ADMIN access");
        data.put("name", authentication.getName());

        return ApiResponse.success("Admin access success", data, HttpStatus.OK.value());
    }
}