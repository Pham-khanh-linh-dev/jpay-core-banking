package com.jpay.core_banking.repository;

import com.jpay.core_banking.entity.Category;
import com.jpay.core_banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByCategoryName(String name);

    Optional<Category> findByCategoryName(String categoryName);
    Optional<Category> findById(String id);
    Optional<Category> findByUserAndIsDefault(User user, boolean isDefault);
}
