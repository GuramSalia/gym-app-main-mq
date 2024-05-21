package com.epam.gym_app_main_mq.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    NONE("NONE"), TRAINER("TRAINER"), TRAINEE("TRAINEE"), USER("USER");
    private final String role;

    Role(String role) {this.role = role;}

    @Override
    public String getAuthority() {
        return role;
    }
}
