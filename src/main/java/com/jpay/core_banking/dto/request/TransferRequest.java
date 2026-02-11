package com.jpay.core_banking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferRequest {

    @NotBlank(message = "Ten nguoi nhan khong duoc de trong")
    String receivedUsername;

    @Min(value = 1000, message = "So tien giao dich khong thap hon 1000")
    long amount;
}
