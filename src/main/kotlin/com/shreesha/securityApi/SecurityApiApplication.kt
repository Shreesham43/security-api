package com.shreesha.securityApi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.shreesha.securityApi.repository"])
class SecurityApiApplication

fun main(args: Array<String>) {
	runApplication<SecurityApiApplication>(*args)
}
