package resa.rodriguez.config.security.jwt

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import resa.rodriguez.models.User
import java.util.*

/**
 * Contiene los métodos necesarios para la creación de tokens, comprobación y decodificación para verificarlos
 *
 */
@Component
class JwtTokensUtils {

    @Value("\${jwt.secret}")
    private val algorithmSecret: String? = null

    fun create(user: User): String {
        return JWT.create()
            .withSubject(user.id.toString())
            .withHeader(mapOf("typ" to TOKEN_TYPE))
            .withClaim("username", user.username)
            .withClaim("email", user.email)
            .withClaim("active", user.active)
            .withExpiresAt(Date(System.currentTimeMillis() + (24 * 60 * 60 * 1_000)))
            .sign(Algorithm.HMAC512(algorithmSecret))
    }

    fun decode(token: String): DecodedJWT? {
        val verifier = JWT.require(Algorithm.HMAC512(algorithmSecret))
            .build()

        return try {
            verifier.verify(token)
        } catch (_: Exception) {
            null
        }
    }

    // Nos permite obtener los campos importantes de manera sencilla
    companion object {

        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer " // Importante
        const val TOKEN_TYPE = "JWT"
    }
}