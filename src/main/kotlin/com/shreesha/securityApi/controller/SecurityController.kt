package com.shreesha.securityApi.controller

import com.shreesha.securityApi.domain.Authority
import com.shreesha.securityApi.domain.User
import com.shreesha.securityApi.domain.UserOutput
import com.shreesha.securityApi.domain.UserPrincipal
import com.shreesha.securityApi.service.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
class SecurityController(
    val userService: UserService
) {

    @PostMapping("/signup")
    fun createUser(@RequestBody user: User): UserOutput{
        return userService.register(user).toUserOutput()
    }

    @PostMapping("/login")
    fun login(@RequestBody user: User): String{
        return userService.verify(user)
    }

    @PutMapping("/update_role/{userId}", consumes = ["application/json"])
    fun updateRole(@PathVariable userId: String, @RequestBody authorities: MutableList<Authority>){
        return userService.updateAuthority(userId, authorities)
    }

    @GetMapping("/validate_token")
    fun validateToken(@RequestHeader("Authorization") authHeader: String): UserPrincipal {
        return userService.decodeToken(authHeader)
    }
}