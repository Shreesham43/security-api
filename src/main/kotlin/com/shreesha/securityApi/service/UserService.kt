package com.shreesha.securityApi.service

import com.shreesha.securityApi.exception.InvalidTokenException
import com.shreesha.securityApi.domain.Authority
import com.shreesha.securityApi.domain.User
import com.shreesha.securityApi.domain.UserPrincipal
import com.shreesha.securityApi.exception.DuplicateEntryException
import com.shreesha.securityApi.exception.UserNotFoundException
import com.shreesha.securityApi.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val authenticationManager: AuthenticationManager,
    val jwtService: JwtService,
    val userDetailsServiceImpl: UserDetailsServiceImpl
) {
    val encoder = BCryptPasswordEncoder(12)
    fun register(user: User): User{
        val encodedUser = user.copy(
            password = encoder.encode(user.password),
            authorities = mutableSetOf(Authority.USER_READ),
            isEnabled = true
        )
        log.info("Creating user")
        log.info(user.toString())
        if(userRepository.findByUsername(user.username) != null){
            throw DuplicateEntryException("User ${user.username} already exists")
        }
        return userRepository.save(encodedUser)
    }

    fun verify(user: User): String{
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(user.username, user.password))
        log.info(authentication.isAuthenticated.toString())
        return if(authentication.isAuthenticated){
            val userAuthorities = userRepository.findByUsername(user.username)!!.authorities
            jwtService.generateToken(user.username, userAuthorities)
        } else{
            "fail"
        }
    }

    fun updateAuthority(userId: String, authorities:  MutableCollection<Authority>) {
        val user = userRepository.findByUserId(userId) ?: throw UserNotFoundException("user $userId not found")
        authorities.addAll(user.authorities)
        val updatedUser = user.copy(authorities = authorities)
        userRepository.save(updatedUser)
        log.info("Roles for user $userId has been updated")
    }

    fun decodeToken(authHeader: String): UserPrincipal{
        val token: String?
        token = authHeader.substring(7)
        val username: String?
        if(authHeader.startsWith("Bearer ")) {
            try{
                username = jwtService.extractUsername(token)
            } catch (ex: Exception) {
                throw InvalidTokenException("Invalid token found")
            }
            if (username != null) {
                val userDetails = userDetailsServiceImpl.loadUserByUsername(username)
                if (!jwtService.isExpired(token)) {
                    return toUserPrincipalOutput(userDetails)
                }
            }
            else {
                throw InvalidTokenException("Invalid token found")
            }
        }
        throw InvalidTokenException("Invalid token found")
    }

    fun toUserPrincipalOutput(userDetails: UserDetails): UserPrincipal {
        return UserPrincipal(
            User(
                userId = "",
                username = userDetails.username,
                password = "",
                authorities = userDetails.authorities as MutableCollection<out Authority>,
                isEnabled = userDetails.isEnabled,
                isAccountLocked = !userDetails.isAccountNonLocked,
                isAccountExpired = !userDetails.isAccountNonExpired,
            )
        )
    }

    companion object{
        val log: Logger = LoggerFactory.getLogger(UserService::class.java)
    }
}