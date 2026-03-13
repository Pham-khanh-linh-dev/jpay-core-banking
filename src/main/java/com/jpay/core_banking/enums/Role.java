package com.jpay.core_banking.enums;

import java.util.Set;

public enum Role {
    ADMIN,
    USER;

    // Helper để lấy Role mặc định
    public static Set<Role> getDefaultRoles() {
        return Set.of(Role.USER);
    }
}
