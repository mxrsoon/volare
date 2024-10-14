package com.mxrsoon.volare.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.mxrsoon.volare.auth.refresh.RefreshToken
import com.mxrsoon.volare.auth.refresh.RefreshTokenRepository
import com.mxrsoon.volare.auth.refresh.RefreshTokensRequest
import com.mxrsoon.volare.common.network.error.ConflictError
import com.mxrsoon.volare.common.network.error.UnauthorizedError
import com.mxrsoon.volare.user.User
import com.mxrsoon.volare.user.UserReference
import com.mxrsoon.volare.user.UserRepository
import com.mxrsoon.volare.user.toReference
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtParams: JwtParams,
    private val jwtVerifier: JWTVerifier
) {

    suspend fun register(request: RegisterRequest): UserReference {
        val existingUser = userRepository.findByEmail(request.email)

        if (existingUser == null) {
            val user = userRepository.create(
                User(
                    firstName = request.firstName,
                    lastName = request.lastName,
                    email = request.email,
                    password = BCrypt.hashpw(
                        request.password,
                        BCrypt.gensalt(PASSWORD_HASHING_ROUNDS)
                    )
                )
            )

            return user.toReference()
        } else {
            throw ConflictError("User already exists")
        }
    }

    suspend fun login(credentials: Credentials): LoginResponse {
        val user = userRepository.findByEmail(credentials.email)

        if (user != null && BCrypt.checkpw(credentials.password, user.password)) {
            return LoginResponse(
                userId = user.id,
                tokens = generateTokenPair(user.id)
            )
        } else {
            throw UnauthorizedError("Unable to authenticate")
        }
    }

    suspend fun refreshTokens(request: RefreshTokensRequest): TokenPair {
        val decodedToken = verifyRefreshToken(request.refreshToken)
        val savedToken = refreshTokenRepository.findByToken(request.refreshToken)

        if (savedToken?.userId != null && decodedToken?.userId == savedToken.userId) {
            return generateTokenPair(savedToken.userId).also {
                refreshTokenRepository.deleteByToken(savedToken.token)
            }
        } else {
            throw UnauthorizedError("Unable to authenticate")
        }
    }

    private fun verifyRefreshToken(token: String): DecodedJWT? {
        val decodedJwt = jwtVerifier.verify(token)
        return decodedJwt.takeIf { it.audience.contains(jwtParams.audience) }
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