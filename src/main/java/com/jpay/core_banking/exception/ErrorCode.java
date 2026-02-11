package com.jpay.core_banking.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_EXISTED_ERROR(1001, "User existed"),
    USER_NOT_EXISTED_ERROR(1002, "User not exist"),
    UNAUTHENTICATED_EXCEPTION(1003, "Lỗi xác thực tài khoản"),
    RECEIVED_USER_NOT_EXISTED_ERROR(1004, " received user not exist"),

    WALLET_NOT_EXISTED_ERROR(2001, "User not exist"),
    AMOUNT_TOO_SMALL(2002, "AMOUNT TOO SMALL FOR TRANSACTION"),
    NOT_ENOUGH_BALANCE(2003, "NOT ENOUGH BALANCE FOR TRANSFER"),
    SAME_NAME_TRANSFER(2004, "KHONG THE TU GUI TIEN CHO CHINH MINH"),
    INVALID_KEY(9999, "Uncategorized error"), // Lỗi này dùng khi gõ sai tên Enum
    ;

    private int code;
    private String message;
    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }
}
