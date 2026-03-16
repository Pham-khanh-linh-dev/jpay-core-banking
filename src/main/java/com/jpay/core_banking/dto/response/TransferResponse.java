package com.jpay.core_banking.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferResponse {
    String senderUsername;
    String receiverUsername;
    long amount;
    long balanceAfter;
    String categoryName;
    String message;
    LocalDate timestamp;
}
