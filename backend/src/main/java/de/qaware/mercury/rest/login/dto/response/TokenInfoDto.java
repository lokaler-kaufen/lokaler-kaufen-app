package de.qaware.mercury.rest.login.dto.response;


import de.qaware.mercury.business.login.VerifiedToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfoDto {
    private String status;
    @Nullable
    private Long expiryInSeconds;
    @Nullable
    private String expiry;

    public static TokenInfoDto notLoggedIn() {
        return new TokenInfoDto("NOT_LOGGED_IN", null, null);
    }

    public static TokenInfoDto loggedIn(VerifiedToken<?, ?> token, ZonedDateTime now) {
        return new TokenInfoDto("LOGGED_IN", token.expiryInSeconds(now), token.getExpiry().toString());
    }
}
