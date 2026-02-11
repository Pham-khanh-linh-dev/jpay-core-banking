package com.jpay.core_banking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "wallets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    long balance;

    @Builder.Default
    String currency = "VND";

    // @OneToOne: Khẳng định quan hệ 1 Ví - 1 User
    // @JoinColumn: Tạo một cột tên là 'user_id' trong bảng wallets để liên kết với 'id' của bảng users
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;
}
