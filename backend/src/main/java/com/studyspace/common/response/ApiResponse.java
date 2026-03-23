package com.studyspace.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Object errors;
    private LocalDateTime timestamp;
    private Integer status;

    public static <T> ApiResponse<T> success(String message, T data, int status) {
        return new ApiResponse<>(true, message, data, null, LocalDateTime.now(), status);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return success(message, data, 200);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success("Success", data, 200);
    }

    public static <T> ApiResponse<T> successMessage(String message) {
        return success(message, null, 200);
    }

    public static <T> ApiResponse<T> error(String message, Object errors, int status) {
        return new ApiResponse<>(false, message, null, errors, LocalDateTime.now(), status);
    }
}