package com.shreesha.securityApi.repository

import com.shreesha.securityApi.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, String> {
    fun findByUsername(username: String?): User?
    fun findByUserId(userId: String): User?
}