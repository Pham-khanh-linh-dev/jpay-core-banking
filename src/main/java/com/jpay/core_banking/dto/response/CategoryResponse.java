package com.jpay.core_banking.dto.response;

import com.jpay.core_banking.entity.Budget;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    String categoryName;

    List<BudgetResponse> budgets;
}
