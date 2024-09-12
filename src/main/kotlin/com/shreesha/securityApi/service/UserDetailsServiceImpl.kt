package com.shreesha.securityApi.service

import com.shreesha.securityApi.domain.UserPrincipal
import com.shreesha.securityApi.exception.UserNotFoundException
import com.shreesha.securityApi.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException("User not found")
        return UserPrincipal(user)
    }
}