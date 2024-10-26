package com.shreesha.securityApi.filter

import com.shreesha.securityApi.service.JwtService
import com.shreesha.securityApi.service.UserDetailsServiceImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    val userDetailsServiceImpl: UserDetailsServiceImpl,
    val jwtService: JwtService
): OncePerRequestFilter() {

    @Override
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        var token: String? = null
        if(authHeader != null){
            token = authHeader.substring(7)
        }
        val username: String?
        if(token != null && authHeader.startsWith("Bearer ")) {
            username = jwtService.extractUsername(token)
            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsServiceImpl.loadUserByUsername(username)
                if (!jwtService.isExpired(token)) {
                    val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}