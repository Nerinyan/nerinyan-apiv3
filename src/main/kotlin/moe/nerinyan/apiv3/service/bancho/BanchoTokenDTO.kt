package moe.nerinyan.apiv3.service.bancho

import com.fasterxml.jackson.annotation.JsonProperty
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
    
    @JsonProperty("token_type")
    var tokenType: String = ""

    @JsonProperty("access_token")
    var accessToken: String = ""

    @JsonProperty("refresh_token")
    var refreshToken: String = ""

    @JsonProperty("expires_in")
    var expiresIn: Long = 0

    var expiresTimestamp: Long = 0
        get() {
            if (field == 0L) {
                field = Jwts.parser().build().parseUnsecuredClaims(accessToken).payload.expiration.time
            }
            return field
        }

    fun validate(): BanchoTokenRenewalType {
        if (tokenType.isBlank()) {
            return BanchoTokenRenewalType.LOGIN
        }
        if (accessToken.isBlank()) {
            return BanchoTokenRenewalType.LOGIN
        }
        if (refreshToken.isBlank()) {
            return BanchoTokenRenewalType.LOGIN
        }
        if (expiresIn == 0L) {
            return BanchoTokenRenewalType.LOGIN
        }

        if (expiresTimestamp <= Instant.now().minusSeconds(5).toEpochMilli()) {
            return BanchoTokenRenewalType.REFRESH
        }

        return BanchoTokenRenewalType.VALID
    }


}