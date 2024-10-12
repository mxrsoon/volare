package com.mxrsoon.volare.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mxrsoon.volare.auth.refresh.JwtClaims
import com.mxrsoon.volare.auth.refresh.RefreshToken
import com.mxrsoon.volare.auth.refresh.RefreshTokenRepository
import com.mxrsoon.volare.user.User
import com.mxrsoon.volare.user.UserReference
import com.mxrsoon.volare.user.UserRepository
import com.mxrsoon.volare.user.toReference
import io.ktor.server.plugins.NotFoundException
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtParams: JwtParams
) {

    suspend fun login(credentials: Credentials): LoginResponse {
        val user = userRepository.findByEmail(credentials.email)

        if (user != null && BCrypt.checkpw(credentials.password, user.password)) {
            return LoginResponse(
                userId = user.id,
                tokens = generateTokenPair(user.id)
            )
        } else {
            // TODO: Use specific error
            throw NotFoundException()
        }
    }

    suspend fun register(request: RegisterRequest): UserReference {
        val existingUser = userRepository.findByEmail(request.email)

        if (existingUser == null) {
            val user = userRepository.create(
                User(
                    firstName = request.firstName,
                    lastName = request.lastName,
                    email = request.email,
                    password = BCrypt.hashpw(request.password, BCrypt.gensalt(PASSWORD_HASHING_ROUNDS))
                )
            )

            return user.toReference()
        } else {
            // TODO: Use specific error
            throw Exception("User already exists")
        }
    }

    private suspend fun generateTokenPair(userId: String): TokenPair {
        val accessTokenExpiration = Clock.System.now() + ACCESS_TOKEN_LIFETIME.milliseconds
        val refreshTokenExpiration = Clock.System.now() + REFRESH_TOKEN_LIFETIME.milliseconds
        val accessToken = generateJwt(userId, accessTokenExpiration)
        val refreshToken = generateJwt(userId, refreshTokenExpiration)

        refreshTokenRepository.create(
            RefreshToken(
                token = refreshToken,
                userId = userId,
                expiresAt = refreshTokenExpiration
            )
        )

        return TokenPair(accessToken, refreshToken)
    }

    private fun generateJwt(userId: String, expiresAt: Instant): String =
        JWT.create()
            .withAudience(jwtParams.audience)
            .withIssuer(jwtParams.issuer)
            .withClaim(JwtClaims.USER_ID, userId)
            .withExpiresAt(expiresAt.toJavaInstant())
            .sign(Algorithm.HMAC256(jwtParams.secret))

    companion object {
        private const val ACCESS_TOKEN_LIFETIME = 18000L
        private const val REFRESH_TOKEN_LIFETIME = 86400000L
        private const val PASSWORD_HASHING_ROUNDS = 12
    }
}