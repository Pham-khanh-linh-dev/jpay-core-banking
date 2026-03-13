package com.jpay.core_banking.repository;

import com.jpay.core_banking.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String> {

    Optional<Budget> findByCategoryIdAndMonthAndYear(String categoryId, int month, int year);

    boolean existsByCategoryIdAndMonthAndYear(String categoryId, int month, int year);

    List<Budget> findAllByCategoryUserId(String userId);
}
