package com.shreesha.securityApi.domain

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val userId: String?,
    val username: String,
    val password: String,
    val authorities: MutableCollection<out Authority> = mutableSetOf(),
    val isAccountExpired: Boolean = false,
    val isAccountLocked: Boolean = false,
    val isEnabled: Boolean = true
){
    fun toUserOutput(): UserOutput{
        return UserOutput(
            userId = this.userId,
            username = this.username,
            authorities = this.authorities,
            isAccountExpired = this.isAccountExpired,
            isAccountLocked = this.isAccountLocked,
            isEnabled = this.isEnabled
        )
    }
}