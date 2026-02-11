package com.jpay.core_banking.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // không hiện field nếu nó null
public class ApiResponse<T> {
    @Builder.Default
    int code = 1000;
    private String message;
    T result;
}
