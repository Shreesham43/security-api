package com.shreesha.securityApi.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    val username: String,
    val password: String,
    val authorities: MutableCollection<SimpleGrantedAuthority>,
    val isAccountExpired: Boolean = false,
    val isAccountLocked : Boolean = false,
    val isEnabled: Boolean = true
)