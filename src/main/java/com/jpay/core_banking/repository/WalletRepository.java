package com.jpay.core_banking.repository;

import com.jpay.core_banking.entity.Wallet;
import com.jpay.core_banking.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface WalletRepository extends JpaRepository<Wallet, String>{
    Optional<Wallet> findByUser(User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.user = :user")
    Optional<Wallet> findByUserForUpdate(User user);
}
