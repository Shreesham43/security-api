package com.shreesha.securityApi.service

import com.shreesha.securityApi.domain.User
import com.shreesha.securityApi.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val authenticationManager: AuthenticationManager,
    val jwtService: JwtService
) {
    val encoder = BCryptPasswordEncoder(12)
    fun register(user: User): User{
        val encodedUser = user.copy(
            password = encoder.encode(user.password)
        )
        log.info("Creating user")
        return userRepository.save(encodedUser)
    }

    fun verify(user: User): String{
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(user.username, user.password))
        return if(authentication.isAuthenticated){
            JwtService.generateToken(user.username)
        } else{
            "fail"
        }
    }


    companion object{
        val log = LoggerFactory.getLogger(UserService::class.java)
    }
}