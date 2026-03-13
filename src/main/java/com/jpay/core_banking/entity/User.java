package com.jpay.core_banking.entity;

import com.jpay.core_banking.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

    @ElementCollection // Tạo một bảng phụ ẩn trong DB để lưu danh sách quyền
    @Enumerated(EnumType.STRING)
    Set<Role> roles;
    String fullName;

    @Column
    String email;
    LocalDate dob;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Category> categories;
}
