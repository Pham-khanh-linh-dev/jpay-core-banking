package com.jpay.core_banking.mapper;

import com.jpay.core_banking.dto.request.BudgetCreationRequest;
import com.jpay.core_banking.dto.response.BudgetResponse;
import com.jpay.core_banking.entity.Budget;
import org.mapstruct.Mapper;

import java.util.List;

// componentModel = "spring" -> Giúp Spring hiểu Mapper này là một Bean (có thể @Autowired được)
@Mapper(componentModel = "spring")
public interface BudgetMapper {
    BudgetResponse toBudgetResponse(Budget budget);
    //List<BudgetResponse> toBudgetResponse(List<Budget> budgets);

    Budget toBudget(BudgetCreationRequest budgetCreationRequest);
}
