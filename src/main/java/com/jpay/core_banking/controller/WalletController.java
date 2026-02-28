package com.jpay.core_banking.controller;

import com.jpay.core_banking.dto.request.TransactionRequest;
import com.jpay.core_banking.dto.request.TransferRequest;
import com.jpay.core_banking.dto.response.ApiResponse;
import com.jpay.core_banking.dto.response.TransactionHistoryResponse;
import com.jpay.core_banking.dto.response.WalletResponse;
import com.jpay.core_banking.service.WalletService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Builder;
import org.mapstruct.MappingTarget;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletController {
    WalletService walletService;

    @GetMapping("/my-wallet")
    public ApiResponse<WalletResponse> getMyWallet(){
        return ApiResponse.<WalletResponse>builder()
                .result(walletService.getMyWallet())
                .build();

    }

    @PostMapping("/deposit")
    public ApiResponse<WalletResponse> deposit(@Valid @RequestBody TransactionRequest request){
        return ApiResponse.<WalletResponse>builder()
                .result(walletService.deposit(request))
                .build();
    }

    @PostMapping("/withdraw")
    public ApiResponse<WalletResponse> withdraw(@Valid @RequestBody TransactionRequest request){
        return ApiResponse.<WalletResponse>builder()
                .result(walletService.withdraw(request))
                .build();
    }

    @PostMapping("/transfer")
    public ApiResponse<WalletResponse> transfer(@RequestBody TransferRequest request){
        return ApiResponse.<WalletResponse>builder()
                .result(walletService.transfer(request))
                .build();
    }

    @GetMapping("/myTransferHistory")
    public ApiResponse<List<TransactionHistoryResponse>> myTransferHistory(){
        return ApiResponse.<List<TransactionHistoryResponse>>builder()
                .result(walletService.myTransferHistory())
                .build();
    }
}
