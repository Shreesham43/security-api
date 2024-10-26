package com.shreesha.securityApi.domain

import org.springframework.security.core.GrantedAuthority


enum class Authority: GrantedAuthority{
    USER_READ,
    USER_WRITE,
    USER_ADMIN;

    override fun getAuthority(): String {
        return name
    }
}