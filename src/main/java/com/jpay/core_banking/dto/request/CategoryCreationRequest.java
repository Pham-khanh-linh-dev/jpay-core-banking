package com.jpay.core_banking.dto.request;

import com.jpay.core_banking.entity.Budget;
import com.jpay.core_banking.entity.TransactionHistory;
import com.jpay.core_banking.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreationRequest {
    @NotBlank(message = "Ten danh muc khong duoc de trong")
    String categoryName;

    Long amount;

    Integer month;
    Integer year;
}
