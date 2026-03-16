package com.jpay.core_banking.configuration;

import com.jpay.core_banking.entity.User;
import com.jpay.core_banking.exception.AppException;
import com.jpay.core_banking.exception.ErrorCode;
import com.jpay.core_banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .flatMap(userRepository::findByUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED_ERROR));
    }
}