package com.jpay.core_banking.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse{
    String id;  // trả về id để frontend bt user vừa tạo là ai
    String username;
    String fullName;
    String email;
    LocalDate dob;

}