package com.shreesha.securityApi.service

import com.shreesha.securityApi.domain.Authority
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.cglib.core.internal.Function
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey


@Service
class JwtService {
    val key: SecretKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray())
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

    fun generateToken(userName: String, authorities: MutableCollection<out Authority>): String{
        val claims = HashMap<String, Any>()
        claims["roles"] = authorities.map { it.name }
        return Jwts.builder()
            .claims()
            .add(claims)
            .subject(userName)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .and()
            .signWith(key)
            .compact()
    }

    companion object{
        val log = LoggerFactory.getLogger(JwtService::class.java)
        const val SECRET_KEY = "aVerySecureSecretKeyWith256bitLengthHere123!"
    }
}