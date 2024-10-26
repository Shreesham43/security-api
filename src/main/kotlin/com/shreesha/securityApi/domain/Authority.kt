package com.shreesha.securityApi.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.security.core.GrantedAuthority

data class AuthorityList(
    @JsonProperty("authorities") val authorities: MutableCollection<Authority>
)

enum class Authority: GrantedAuthority{
    USER_READ,
    USER_WRITE,
    USER_ADMIN;

    override fun getAuthority(): String {
        return name
    }
}