package com.shreesha.securityApi.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserNotFoundException(exception: UserNotFoundException): ResponseEntity<String> {
        return ResponseEntity(exception.message, HttpStatus.NOT_FOUND )
    }

    @ExceptionHandler(DuplicateEntryException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDuplicateEntryException(exception: DuplicateEntryException): ResponseEntity<String> {
        return ResponseEntity(exception.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(InvalidTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleInvalidTokenException(exception: InvalidTokenException): ResponseEntity<String> {
        return ResponseEntity(exception.message, HttpStatus.UNAUTHORIZED)
    }
}