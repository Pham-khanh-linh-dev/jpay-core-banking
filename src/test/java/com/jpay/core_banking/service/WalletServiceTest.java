package com.jpay.core_banking.service;


import com.jpay.core_banking.dto.request.TransactionRequest;
import com.jpay.core_banking.dto.response.WalletResponse;
import com.jpay.core_banking.entity.User;
import com.jpay.core_banking.entity.Wallet;
import com.jpay.core_banking.mapper.WalletMapper;
import com.jpay.core_banking.repository.UserRepository;
import com.jpay.core_banking.repository.WalletRepository;
import com.jpay.core_banking.repository.TransactionHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class) // Bật tính năng "Diễn viên đóng thế" của Mockito
public class WalletServiceTest {

    // 1. TẠO CÁC DIỄN VIÊN ĐÓNG THẾ (@Mock)
    @Mock
    WalletRepository walletRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TransactionHistoryRepository historyRepository;
    @Mock
    WalletMapper walletMapper;

    // 2. NHÉT CÁC DIỄN VIÊN VÀO SÂN KHẤU CHÍNH (@InjectMocks)
    // WalletService thật sẽ được tiêm các class giả ở trên vào
    @InjectMocks
    WalletService walletService;

    // Các biến dùng chung cho các Test Case
    User mockUser;
    Wallet mockWallet;
    TransactionRequest depositRequest;

    // 3. HÀM CHẠY TRƯỚC MỖI TEST CASE (Chuẩn bị đạo cụ)
    @BeforeEach
    void setUp() {
        // Tạo sẵn 1 ông User giả
        mockUser = User.builder().id("user-1").username("nguyen_van_a").build();

        // Tạo sẵn 1 cái Ví giả đang có 100.000đ
        mockWallet = Wallet.builder().id("wallet-1").user(mockUser).balance(100000L).currency("VND").build();

        // Tạo sẵn 1 yêu cầu Nạp 50.000đ
        depositRequest = TransactionRequest.builder().amount(50000L).build();

        // --- MA THUẬT: Đóng giả SecurityContext (Hào quang) ---
        // Vì test không có Postman truyền Token, ta phải tự giả lập Token hợp lệ
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("nguyen_van_a"); // Ép Token giả này mang tên nguyen_van_a

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void deposit_Success_WhenValidRequest() {
        // --- 1. GIVEN (GIẢ ĐỊNH DỮ LIỆU TỪ MOCKITO) ---
        // Dặn UserRepository: Nếu ai gọi tìm "nguyen_van_a", hãy trả về mockUser
        Mockito.when(userRepository.findByUsername("nguyen_van_a"))
                .thenReturn(Optional.of(mockUser));

        // Dặn WalletRepository: Nếu ai gọi tìm ví của mockUser để update, hãy trả về mockWallet (đang có 100k)
        Mockito.when(walletRepository.findByUserForUpdate(mockUser))
                .thenReturn(Optional.of(mockWallet));

        // Dặn WalletRepository: Khi gọi save() lưu ví, cứ trả về lại chính cái ví đó
        Mockito.when(walletRepository.save(any(Wallet.class)))
                .thenReturn(mockWallet);

        // Dặn Mapper: Khi map Entity sang DTO, trả về 1 Response giả định chứa 150k
        WalletResponse expectedResponse = WalletResponse.builder().balance(150000L).build();
        Mockito.when(walletMapper.toWalletResponse(any(Wallet.class)))
                .thenReturn(expectedResponse);

        // --- 2. WHEN (HÀNH ĐỘNG - GỌI HÀM CẦN TEST) ---
        // Ta gọi hàm nạp tiền với số tiền nạp là 50k (depositRequest)
        WalletResponse actualResponse = walletService.deposit(depositRequest);

        // --- 3. THEN (KIỂM CHỨNG KẾT QUẢ) ---
        // Kiểm tra xem số tiền trả về có ĐÚNG BẰNG 150k (100k gốc + 50k nạp) không?
        assertEquals(150000L, actualResponse.getBalance());

        // (Tùy chọn) Kiểm tra xem hàm ghi sổ Lịch sử giao dịch (historyRepository.save) CÓ ĐƯỢC GỌI chạy 1 lần không?
        Mockito.verify(historyRepository, Mockito.times(1)).save(any());
    }
}
