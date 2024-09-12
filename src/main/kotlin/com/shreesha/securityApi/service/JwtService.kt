package com.shreesha.securityApi.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cglib.core.internal.Function
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@Service
class JwtService(
    @Qualifier("UserPrincipal")
    val userDetails: UserDetails
) {
    companion object {
        private val key = KeyGenerator.getInstance("HmacSHA256").generateKey()
        fun extractUsername(token: String): String? {
            return extractClaim(token, Claims::getSubject)
        }

        private fun extractExpiration(token: String): Date{
            return extractClaim(token, Claims::getExpiration)
        }

        private fun <T> extractClaim(token: String, claimResolver: Function<Claims, T>): T {
            val claims = extractClaims(token)
            return claimResolver.apply(claims)
        }

        private fun extractClaims(token: String): Claims{
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        }

        fun isExpired(token: String): Boolean{
            return extractExpiration(token).before(Date())
        }

        fun generateToken(userName: String): String{
            val claims = HashMap<String, Any>()
            return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userName)
                .issuedAt(Date(System.currentTimeMillis()))
                .expiration(Date(System.currentTimeMillis() + 60 * 60 * 30))
                .and()
                .signWith(key)
                .compact()
        }
    }

}