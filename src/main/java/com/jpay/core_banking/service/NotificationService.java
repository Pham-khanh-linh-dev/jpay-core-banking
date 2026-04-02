package com.jpay.core_banking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @Async
    public void sendTransferNotification(String username, long amount, String receivedUsername) {
        log.info("Bắt đầu tiến trình gửi Email/SMS thông báo (chạy ngầm)...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("✅ THÔNG BÁO HOÀN TẤT: [{}] vừa chuyển {} VNĐ thành công cho [{}]", username, amount,
                receivedUsername);
    }
}