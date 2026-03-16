package com.jpay.core_banking.service;

import com.jpay.core_banking.entity.Budget;
import com.jpay.core_banking.entity.Category;
import com.jpay.core_banking.exception.AppException;
import com.jpay.core_banking.exception.ErrorCode;
import com.jpay.core_banking.repository.BudgetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor // 2. Tự động tạo Constructor cho các biến final (Thay thế cho @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // 3. Các biến bên dưới là private final

public class BudgetService {

    private final BudgetRepository budgetRepository;

    @Transactional
    public void updateSpentAmount(Category category, long amount){
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();
        Budget budget = budgetRepository.findByCategoryIdAndMonthAndYear(category.getId(), currentMonth, currentYear)
                .orElseGet(() -> {return Budget.builder()
                            .category(category)
                            .month(currentMonth)
                            .year(currentYear)
                            .amount(0L) // Hạn mức mặc định
                            .spentAmount(0L)
                            .build();
                });
        budget.setSpentAmount(budget.getSpentAmount() + amount);

        budgetRepository.save(budget);
    }
}
