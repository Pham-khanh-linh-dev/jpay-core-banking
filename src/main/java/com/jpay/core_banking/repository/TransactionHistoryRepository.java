package com.jpay.core_banking.repository;

import com.jpay.core_banking.entity.TransactionHistory;
import com.jpay.core_banking.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {

    List<TransactionHistoryRepository> findByWalletOrderByCreatedAt(Wallet wallet);

}
