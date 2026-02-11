package com.jpay.core_banking.service;

import com.jpay.core_banking.dto.request.UserCreationRequest;
import com.jpay.core_banking.dto.request.UserUpdateRequest;
import com.jpay.core_banking.dto.response.ApiResponse;
import com.jpay.core_banking.dto.response.UserResponse;
import com.jpay.core_banking.entity.User;
import com.jpay.core_banking.entity.Wallet;
import com.jpay.core_banking.exception.AppException;
import com.jpay.core_banking.exception.ErrorCode;
import com.jpay.core_banking.mapper.UserMapper;
import com.jpay.core_banking.repository.UserRepository;
import com.jpay.core_banking.repository.WalletRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // 2. Tự động tạo Constructor cho các biến final (Thay thế cho @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // 3. Các biến bên dưới là private final
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    WalletRepository walletRepository;

    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED_ERROR);
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(10L)
                .currency("VND")
                .build();
        walletRepository.save(wallet);

        return userMapper.toUserResponse(user);
    }
    public UserResponse updateUser(String id, UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED_ERROR)) ;

        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUser(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse myInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED_ERROR));
        return userMapper.toUserResponse(user);
    }
}
