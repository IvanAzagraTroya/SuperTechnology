package resa.rodriguez.services

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import resa.rodriguez.models.User
import resa.rodriguez.models.UserRole
import java.time.Instant
import java.util.*

private val algorithm: Algorithm = Algorithm.HMAC256("Zanahoria turbopropulsada")

/**
 * @author Daniel Rodriguez Muñoz
 * Contiene los métodos necesarios para la creación de tokens, comprobación y decodificación para verificarlos
 */
fun create(user: User): String {
    return JWT.create()
        .withIssuer(user.id.toString())
        .withClaim("username", user.username)
        .withClaim("email", user.email)
        .withClaim("role", user.role.name)
        .withClaim("active", user.active)
        .withExpiresAt(Date(System.currentTimeMillis() + (24*60*60*1_000)))
        .sign(algorithm)
}

fun decode(token: String): DecodedJWT? {
    val verifier = JWT.require(algorithm)
        .build()

    return try {
        verifier.verify(token)
    } catch (_: Exception) {
        null
    }
}

fun checkToken(token: String, role: UserRole): ResponseEntity<out Any>? {
    val decoded = decode(token)
        ?: return ResponseEntity("No token detected.", HttpStatus.UNAUTHORIZED)
    if (decoded.getClaim("role").isMissing || decoded.getClaim("active").isMissing ||
        decoded.getClaim("role").isNull || decoded.getClaim("active").isNull ||
        decoded.getClaim("active").asBoolean() == false)
        return ResponseEntity("Invalid token.", HttpStatus.UNAUTHORIZED)
    if (decoded.expiresAtAsInstant.isBefore(Instant.now()))
        return ResponseEntity("Token expired.", HttpStatus.UNAUTHORIZED)
    when (role) {
        UserRole.SUPER_ADMIN -> {
            if (!decoded.getClaim("role").asString().equals(UserRole.SUPER_ADMIN.name)) {
                return ResponseEntity("You are not allowed to to this.", HttpStatus.FORBIDDEN)
            }
        }
        UserRole.ADMIN -> {
            if (!(decoded.getClaim("role").asString().equals(UserRole.SUPER_ADMIN.name) ||
                        decoded.getClaim("role").asString().equals(UserRole.ADMIN.name))) {
                return ResponseEntity("You are not allowed to to this.", HttpStatus.FORBIDDEN)
            }
        }
        UserRole.USER -> {}
    }
    return null
}

fun getRole(token: String): UserRole? {
    val decoded = decode(token) ?: return null
    return  if (!decoded.getClaim("role").isNull && !decoded.getClaim("role").isMissing) {
        val claim = decoded.getClaim("role")
        if (claim.asString().equals(UserRole.SUPER_ADMIN.name)) UserRole.SUPER_ADMIN
        else if (claim.asString().equals(UserRole.ADMIN.name)) UserRole.ADMIN
        else if (claim.asString().equals(UserRole.USER.name)) UserRole.USER
        else null
    }
    else null
}