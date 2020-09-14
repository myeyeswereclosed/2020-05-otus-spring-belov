package ru.otus.spring.app_authorization.security.user;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Role {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    ANONYMOUS("ANONYMOUS"),
    USER("USER");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<Role> withPrivileges() {
        return
            Arrays
                .stream(values())
                .filter(role -> role.equals(ADMIN) || role.equals(MANAGER))
                .collect(Collectors.toList())
            ;
    }

    public String toSecurityRole() {
        return "ROLE_" + name;
    }

    public boolean hasSecurityRole(String role) {
        return toSecurityRole().equals(role);
    }
}
