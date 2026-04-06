package com.jpay.core_banking.service;

import com.jpay.core_banking.configuration.SecurityUtils;
import com.jpay.core_banking.dto.request.BudgetCreationRequest;
import com.jpay.core_banking.dto.request.CategoryCreationRequest;
import com.jpay.core_banking.dto.response.BudgetResponse;
import com.jpay.core_banking.dto.response.CategoryResponse;
import com.jpay.core_banking.entity.Budget;
import com.jpay.core_banking.entity.Category;
import com.jpay.core_banking.entity.User;
import com.jpay.core_banking.exception.AppException;
import com.jpay.core_banking.exception.ErrorCode;
import com.jpay.core_banking.mapper.CategoryMapper;
import com.jpay.core_banking.repository.BudgetRepository;
import com.jpay.core_banking.repository.CategoryRepository;
import com.jpay.core_banking.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // 2. Tự động tạo Constructor cho các biến final (Thay thế cho @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // 3. Các biến bên dưới là private final
public class CategoryService {
        CategoryRepository categoryRepository;
        UserRepository userRepository;
        BudgetRepository budgetRepository;
        CategoryMapper categoryMapper;
        SecurityUtils securityUtils;
        public static final String DEFAULT_CATEGORY_NAME = "Chi tiêu chung";

        @Transactional
        public CategoryResponse createCategory(CategoryCreationRequest request) {
                User user = securityUtils.getCurrentUser();

                if (categoryRepository.existsByCategoryName(request.getCategoryName()))
                        throw new AppException(ErrorCode.CATEGORY_EXISTED);

                Category category = Category.builder()
                                .categoryName(request.getCategoryName())
                                .user(user)
                                .build();

                int curentMonth = request.getMonth() != null ? request.getMonth() : LocalDate.now().getMonthValue();
                int currentYear = request.getYear() != null ? request.getYear() : LocalDate.now().getYear();
                long amount = request.getAmount() != null ? request.getAmount() : 0L;
                Budget budget = Budget.builder()
                                .amount(amount)
                                .spentAmount(0L)
                                .month(curentMonth)
                                .year(currentYear)
                                .build();

                category.addBudget(budget);

                Category savedCategory = categoryRepository.save(category);

                return categoryMapper.toCategoryResponse(savedCategory);
        }

        @Transactional
        public CategoryResponse upsertBudget(BudgetCreationRequest request) {

                var category = categoryRepository.findByCategoryName(request.getCategoryName())
                                .orElseThrow(() -> new AppException((ErrorCode.CATEGORY_NOT_EXIST)));
                int month = request.getMonth();
                int year = request.getYear();

                Budget budget = budgetRepository.findByCategoryIdAndMonthAndYear(category.getId(), month, year)
                                .map(existedBudget -> {
                                        existedBudget.setAmount(request.getAmount());
                                        return budgetRepository.save(existedBudget);
                                }).orElseGet(() -> {
                                        return budgetRepository.save(Budget.builder()
                                                        .amount(request.getAmount())
                                                        .month(month)
                                                        .year(year)
                                                        .category(category)
                                                        .spentAmount(0L)
                                                        .build());
                                });
                return categoryMapper.toCategoryResponse(category);
        }

        @Transactional(readOnly = true)
        public List<CategoryResponse> getMyCategories() {
                User user = securityUtils.getCurrentUser();
                
                List<Category> categories = categoryRepository.findByUser(user);
                return categories.stream().map(categoryMapper::toCategoryResponse).toList();
        }

}
