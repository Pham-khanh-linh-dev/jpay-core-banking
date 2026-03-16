package com.jpay.core_banking.dto.request;

import jakarta.persistence.Column;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BudgetCreationRequest {
    Long amount;      // Hạn mức đặt ra (ví dụ: 5.000.000)
    Long spentAmount; // Số tiền đã tiêu thực tế (ví dụ: 1.200.000)

    int month;
    int year;

    String categoryName;

}
