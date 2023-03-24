package ru.marilka888.jeweller.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    USER,
    ADMIN;

    @Override
    public String getAuthority() {//возвращает роли в строковом виде
        return name();
    }
}
