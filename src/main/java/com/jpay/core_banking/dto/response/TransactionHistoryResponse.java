package com.jpay.core_banking.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jpay.core_banking.entity.Wallet;
import com.jpay.core_banking.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // không hiện field nếu nó null
public class TransactionHistoryResponse {
    Wallet wallet;

    TransactionType type;

    long amount;

    long balanceAfter;

    String message;

    LocalDate createdAt;
}
