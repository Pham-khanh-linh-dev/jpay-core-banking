package com.jpay.core_banking.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpay.core_banking.dto.response.ApiResponse;
import com.jpay.core_banking.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED_EXCEPTION;

        //Ép HTTP Status Code là 401 (Unauthorized)
        response.setStatus(errorCode.getStatusCode().value());

        //Khai báo kiểu dữ liệu trả về là JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        //Chuyển Object Java thành chuỗi JSON (Dùng ObjectMapper) và ghi vào Response
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

        // Buộc đẩy dữ liệu về cho Client
        response.flushBuffer();
    }
}
