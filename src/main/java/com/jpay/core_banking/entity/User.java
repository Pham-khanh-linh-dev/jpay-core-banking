package com.jpay.core_banking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor // contructor rỗng
@AllArgsConstructor // contructor full tham s
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE) // mặc định các file là private
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;
    String fullName;

    @Column(nullable = true)
    String email;
    LocalDate dob;
}
