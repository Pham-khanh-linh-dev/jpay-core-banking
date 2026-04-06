package com.jpay.core_banking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.core.support.FragmentNotImplementedException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String categoryName;

    boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<Budget> budgets = new ArrayList<>();

    // helper
    public void addBudget(Budget budget) {
        budgets.add(budget);
        budget.setCategory(this);
    }

    @OneToMany(mappedBy = "category")
    List<TransactionHistory> transactionHistories;
}
