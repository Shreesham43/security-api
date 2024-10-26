package com.shreesha.securityApi.controller

import com.shreesha.securityApi.domain.*
import com.shreesha.securityApi.service.UserService
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

    @PutMapping("/update_role/{userId}")
    fun updateRole(@PathVariable userId: String, @RequestBody authorities: AuthorityList){
        return userService.updateAuthority(userId, authorities.authorities)
    }

    @GetMapping("/validate_token")
    fun validateToken(@RequestHeader("Authorization") authHeader: String): UserPrincipal {
        return userService.decodeToken(authHeader)
    }
}