package com.jpay.core_banking.mapper;

import com.jpay.core_banking.dto.response.TransactionHistoryResponse;
import com.jpay.core_banking.entity.TransactionHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionHistoryMapper {
    List<TransactionHistoryResponse> toTransactionResponse(List<TransactionHistory> transactionHistory);
}
