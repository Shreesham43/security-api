package com.shreesha.securityApi.controller

import com.shreesha.securityApi.domain.User
import com.shreesha.securityApi.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class SecurityController(
    val userService: UserService
) {

    @PostMapping("/signup")
    fun createUser(@RequestBody user: User): User{
        return userService.register(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody user: User): String{
        return userService.verify(user)
    }
}