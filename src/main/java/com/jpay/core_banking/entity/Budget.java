package com.jpay.core_banking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "budget", uniqueConstraints = {
        @UniqueConstraint(name = "uk_category_month_year", // Đặt tên gợi nhớ,
                            columnNames = {"category_id", "month", "year"})
})
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    Long amount;
    Long spentAmount;

    @Column(nullable = false)
    int month;
    @Column(nullable = false)
    int year;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
