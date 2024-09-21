package moe.nerinyan.apiv3.service.bancho

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Jwts
import java.time.Instant

enum class BanchoTokenRenewalType {
    LOGIN,
    REFRESH,
    VALID
}

class BanchoTokenDTO {
    private val log = KotlinLogging.logger {}
    var tokenType: String = ""
    var accessToken: String = ""
    var refreshToken: String = ""
    var expiresIn: Long = 0
    var expiresTimestamp: Long = 0

    fun validate(): BanchoTokenRenewalType {
        if (tokenType.isBlank()) {
            log.info { "tokenType is blank." }
            return BanchoTokenRenewalType.LOGIN
        }
        if (accessToken.isBlank()) {
            log.info { "accessToken is blank." }
            return BanchoTokenRenewalType.LOGIN
        }
        if (refreshToken.isBlank()) {
            log.info { "refreshToken is blank." }
            return BanchoTokenRenewalType.LOGIN
        }
        if (expiresIn == 0L) {
            log.info { "expiresIn is 0." }
            return BanchoTokenRenewalType.LOGIN
        }

        if (getExpiresTimestamp() <= Instant.now().minusSeconds(5).toEpochMilli()) {
            log.info { "expiresTimestamp is less than 5 seconds." }
            return BanchoTokenRenewalType.REFRESH
        }

        return BanchoTokenRenewalType.VALID
    }

    fun getExpiresTimestamp(): Long {
        if (expiresTimestamp == 0L) {
            expiresTimestamp = Jwts.parser().build().parseUnsecuredClaims(accessToken).payload.expiration.time
        }
        return expiresTimestamp
    }
}