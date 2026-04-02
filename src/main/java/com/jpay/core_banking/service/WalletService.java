package com.jpay.core_banking.service;

import com.jpay.core_banking.dto.request.TransactionRequest;
import com.jpay.core_banking.dto.request.TransferRequest;
import com.jpay.core_banking.dto.response.PageResponse;
import com.jpay.core_banking.dto.response.TransactionHistoryResponse;
import com.jpay.core_banking.dto.response.WalletResponse;
import com.jpay.core_banking.entity.TransactionHistory;
import com.jpay.core_banking.entity.User;
import com.jpay.core_banking.entity.Wallet;
import com.jpay.core_banking.enums.TransactionType;
import com.jpay.core_banking.exception.AppException;
import com.jpay.core_banking.exception.ErrorCode;
import com.jpay.core_banking.mapper.TransactionHistoryMapper;
import com.jpay.core_banking.mapper.WalletMapper;
import com.jpay.core_banking.repository.TransactionHistoryRepository;
import com.jpay.core_banking.repository.UserRepository;
import com.jpay.core_banking.repository.WalletRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionHistoryMapper transactionHistoryMapper;
    private final NotificationService notificationService;

    public WalletResponse getMyWallet() {
        var context = SecurityContextHolder.getContext();

        var username = context.getAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED_ERROR));
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_EXISTED_ERROR));

        return walletMapper.toWalletResponse(wallet);
    }

    @Transactional
    public WalletResponse deposit(TransactionRequest request) {
        var context = SecurityContextHolder.getContext();

        var username = context.getAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED_ERROR));
        var wallet = walletRepository.findByUserForUpdate(user)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_EXISTED_ERROR));
        wallet.setBalance(wallet.getBalance() + request.getAmount());
        walletRepository.save(wallet);

        var transactionHistory = TransactionHistory.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .message("Vua thuc hien giao dich nap tien")
                .balanceAfter(wallet.getBalance())
                .type(TransactionType.DEPOSIT)
                .build();
        transactionHistoryRepository.save(transactionHistory);

        return walletMapper.toWalletResponse(wallet);
    }

    @Transactional
    public WalletResponse withdraw(TransactionRequest request) {
        var context = SecurityContextHolder.getContext();

        var username = context.getAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED_ERROR));
        var wallet = walletRepository.findByUserForUpdate(user)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_EXISTED_ERROR));

        if (wallet.getBalance() < request.getAmount())
            throw new AppException(ErrorCode.NOT_ENOUGH_BALANCE);
        wallet.setBalance(wallet.getBalance() - request.getAmount());
        walletRepository.save(wallet);

        var transactionHistory = TransactionHistory.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .message("Vua thuc hien giao dich Rut tien")
                .balanceAfter(wallet.getBalance())
                .type(TransactionType.WITHDRAW)
                .build();
        transactionHistoryRepository.save(transactionHistory);
        return walletMapper.toWalletResponse(wallet);
    }

    @Transactional
    public WalletResponse transfer(TransferRequest request) {
        var context = SecurityContextHolder.getContext();
        var username = context.getAuthentication().getName();
        var receivedUsername = request.getReceivedUsername();

        if (username.equals(receivedUsername))
            throw new AppException(ErrorCode.SAME_NAME_TRANSFER);

        var sendUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException((ErrorCode.USER_NOT_EXISTED_ERROR)));
        var received_user = userRepository.findByUsername(receivedUsername)
                .orElseThrow(() -> new AppException((ErrorCode.RECEIVED_USER_NOT_EXISTED_ERROR)));

        Wallet sender_wallet;
        Wallet received_wallet;

        if (username.compareTo(receivedUsername) < 0) {
            sender_wallet = walletRepository.findByUserForUpdate(sendUser)
                    .orElseThrow(() -> new AppException((ErrorCode.WALLET_NOT_EXISTED_ERROR)));
            received_wallet = walletRepository.findByUserForUpdate(received_user)
                    .orElseThrow(() -> new AppException((ErrorCode.WALLET_NOT_EXISTED_ERROR)));
        } else {
            received_wallet = walletRepository.findByUserForUpdate(received_user)
                    .orElseThrow(() -> new AppException((ErrorCode.WALLET_NOT_EXISTED_ERROR)));
            sender_wallet = walletRepository.findByUserForUpdate(sendUser)
                    .orElseThrow(() -> new AppException((ErrorCode.WALLET_NOT_EXISTED_ERROR)));
        }

        if (sender_wallet.getBalance() < request.getAmount())
            throw new AppException(ErrorCode.NOT_ENOUGH_BALANCE);

        log.info(receivedUsername);

        sender_wallet.setBalance(sender_wallet.getBalance() - request.getAmount());
        received_wallet.setBalance(received_wallet.getBalance() + request.getAmount());

        walletRepository.save(sender_wallet);
        var transactionHistorySender = TransactionHistory.builder()
                .wallet(sender_wallet)
                .amount(request.getAmount())
                .message("Vua thuc hien giao dich Chuyen tien")
                .balanceAfter(sender_wallet.getBalance())
                .type(TransactionType.TRANSFER_OUT)
                .build();
        transactionHistoryRepository.save(transactionHistorySender);

        walletRepository.save(received_wallet);
        var transactionHistoryReceiver = TransactionHistory.builder()
                .wallet(received_wallet)
                .amount(request.getAmount())
                .message("Vua nhan tien")
                .balanceAfter(received_wallet.getBalance())
                .type(TransactionType.TRANSFER_IN)
                .build();
        transactionHistoryRepository.save(transactionHistoryReceiver);

        notificationService.sendTransferNotification(username, request.getAmount(), receivedUsername);

        return walletMapper.toWalletResponse(sender_wallet);
    }

    @Transactional(readOnly = true)
    public PageResponse<TransactionHistoryResponse> myTransferHistory(int page, int size) {
        var user = currentUser();
        var wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_EXISTED_ERROR));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<TransactionHistory> transactionPage = transactionHistoryRepository.findByWalletWithPagination(wallet,
                pageable);

        var data = transactionHistoryMapper.toTransactionResponse(transactionPage.getContent());

        return PageResponse.<TransactionHistoryResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(transactionPage.getTotalPages())
                .totalElements(transactionPage.getTotalElements())
                .data(data)
                .build();
    }

    public User currentUser() {
        var context = SecurityContextHolder.getContext();
        var username = context.getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED_ERROR));
    }

}
