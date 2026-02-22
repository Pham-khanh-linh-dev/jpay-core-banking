package com.jpay.core_banking.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER_OUT,
    TRANSFER_IN;

    private String type;
}
