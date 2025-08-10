package com.mxrsoon.volare.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.google.api.client.googleapis.apache.v2.GoogleApacheHttpTransport
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.json.gson.GsonFactory
import com.mxrsoon.volare.auth.refresh.RefreshToken
import com.mxrsoon.volare.auth.refresh.RefreshTokenRepository
import com.mxrsoon.volare.auth.refresh.RefreshTokensRequest
import com.mxrsoon.volare.common.network.error.ConflictError
import com.mxrsoon.volare.common.network.error.UnauthorizedError
import com.mxrsoon.volare.user.User
import com.mxrsoon.volare.user.UserReference
import com.mxrsoon.volare.user.UserRepository
import com.mxrsoon.volare.user.toReference
import java.util.Collections
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtParams: JwtParams,
    private val jwtVerifier: JWTVerifier,
    googleParams: GoogleAuthParams
) {

    private val googleIdVerifier = GoogleIdTokenVerifier.Builder(GoogleApacheHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
        .setAudience(Collections.singletonList(googleParams.clientId))
        .build()

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
                    ),
                    googleId = null
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

    suspend fun signInWithGoogle(idTokenString: String): LoginResponse {
        val idToken = googleIdVerifier.verify(idTokenString) ?: throw IllegalStateException("Unable to authenticate")
        val payload = idToken.payload
        val googleId = payload.subject
        val email = payload.email

        val user = userRepository.findByGoogleId(googleId)
            ?: userRepository
                .findByEmail(email)
                ?.copy(googleId = googleId)
                ?.also { userRepository.update(it.id, it) }
            ?: userRepository.create(
                User(
                    firstName = payload["given_name"] as String,
                    lastName = payload["family_name"] as? String,
                    email = email,
                    password = null,
                    googleId = googleId
                )
            )

        return LoginResponse(
            userId = user.id,
            tokens = generateTokenPair(user.id)
        )
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

    @OptIn(ExperimentalTime::class)
    private suspend fun generateTokenPair(userId: String): TokenPair {
        val accessTokenExpiration = Clock.System.now() + ACCESS_TOKEN_LIFETIME.milliseconds
        val accessToken = generateJwt(userId, accessTokenExpiration)
        val refreshToken = generateJwt(userId, null)

        refreshTokenRepository.create(
            RefreshToken(
                token = refreshToken,
                userId = userId,
                revoked = false
            )
        )

        return TokenPair(accessToken, refreshToken)
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    private fun generateJwt(userId: String, expiresAt: Instant?): String =
        JWT.create()
            .withAudience(jwtParams.audience)
            .withIssuer(jwtParams.issuer)
            .withClaim(JwtClaims.USER_ID, userId)
            .withIssuedAt(Clock.System.now().toJavaInstant())
            .withJWTId(Uuid.random().toHexString())
            .apply { expiresAt?.let { withExpiresAt(it.toJavaInstant()) } }
            .sign(Algorithm.HMAC256(jwtParams.secret))

    companion object {
        private const val ACCESS_TOKEN_LIFETIME = 18000L
        private const val PASSWORD_HASHING_ROUNDS = 12
    }
}