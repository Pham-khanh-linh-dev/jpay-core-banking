package com.jpay.core_banking.repository;

import com.jpay.core_banking.entity.TransactionHistory;
import com.jpay.core_banking.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {

    @Query(value = "SELECT t FROM TransactionHistory t JOIN FETCH t.wallet WHERE t.wallet = :wallet", countQuery = "SELECT COUNT(t) FROM TransactionHistory t WHERE t.wallet = :wallet")
    Page<TransactionHistory> findByWalletWithPagination(@Param("wallet") Wallet wallet, Pageable pageable);

}
