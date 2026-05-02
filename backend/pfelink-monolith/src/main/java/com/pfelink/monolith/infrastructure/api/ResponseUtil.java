package com.pfelink.monolith.infrastructure.api;

import com.pfelink.monolith.shared.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> toResponse(Result<T> result) {
        long startTime = getStartTime();
        long duration = System.currentTimeMillis() - startTime;

        if (result.isFailure()) {
            HttpStatus status = mapErrorToStatus(result.getError().code());
            return ResponseEntity.status(status).body(
                    ApiResponse.failure(result.getError().code(), result.getError().message(), status.value(), duration)
            );
        }
        return ResponseEntity.ok(ApiResponse.success(result.getValue(), duration));
    }

    public static <T> ResponseEntity<ApiResponse<T>> toResponse(T data) {
        return toResponse(data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> toResponse(T data, HttpStatus status) {
        long startTime = getStartTime();
        long duration = System.currentTimeMillis() - startTime;
        return ResponseEntity.status(status).body(ApiResponse.success(data, duration, status.value()));
    }

    private static long getStartTime() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();
            Object start = request.getAttribute(RequestTimingFilter.START_TIME_ATTR);
            if (start instanceof Long) return (Long) start;
        }
        return System.currentTimeMillis();
    }

    private static HttpStatus mapErrorToStatus(String errorCode) {
        if (errorCode == null) return HttpStatus.BAD_REQUEST;
        
        String code = errorCode.toLowerCase();
        if (code.contains("notfound") || code.contains("not.found")) return HttpStatus.NOT_FOUND;
        if (code.contains("unauthorized")) return HttpStatus.UNAUTHORIZED;
        if (code.contains("forbidden")) return HttpStatus.FORBIDDEN;
        if (code.contains("conflict") || code.contains("exists")) return HttpStatus.CONFLICT;
        
        return HttpStatus.BAD_REQUEST;
    }
}
