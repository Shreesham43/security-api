package com.shreesha.securityApi.domain

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

data class UserOutput (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val userId: String?,
    val username: String,
    val authorities: MutableCollection<out Authority> = mutableSetOf(),
    val isAccountExpired: Boolean = false,
    val isAccountLocked : Boolean = false,
    val isEnabled: Boolean = true
)