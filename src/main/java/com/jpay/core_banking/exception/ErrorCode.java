package com.jpay.core_banking.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED_ERROR(1001, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED_ERROR(1002, "User not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED_EXCEPTION(1003, "Lỗi xác thực tài khoản", HttpStatus.UNAUTHORIZED),
    RECEIVED_USER_NOT_EXISTED_ERROR(1004, " received user not exist", HttpStatus.NOT_FOUND),
    ACCESS_DENIED(1005, "You are not have permission", HttpStatus.FORBIDDEN),

    WALLET_NOT_EXISTED_ERROR(2001, "User not exist", HttpStatus.NOT_FOUND),
    AMOUNT_TOO_SMALL(2002, "AMOUNT TOO SMALL, AT LEAST {value} VND", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_BALANCE(2003, "NOT ENOUGH BALANCE FOR TRANSFER", HttpStatus.BAD_REQUEST),
    SAME_NAME_TRANSFER(2004, "KHONG THE TU GUI TIEN CHO CHINH MINH", HttpStatus.BAD_REQUEST),

    CATEGORY_EXISTED(3001, "category existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXIST(3002, "category not found", HttpStatus.NOT_FOUND),
    DEFAULT_CATEGORY_NOT_EXIST(3003, "default category not found", HttpStatus.NOT_FOUND),

    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống chưa được phân loại. Vui lòng liên hệ admin.",
            HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST), // Lỗi này dùng khi gõ sai tên Enum
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
