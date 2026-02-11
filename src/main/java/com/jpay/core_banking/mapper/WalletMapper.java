package com.jpay.core_banking.mapper;

import com.jpay.core_banking.dto.response.WalletResponse;
import com.jpay.core_banking.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletResponse toWalletResponse(Wallet wallet);
}
