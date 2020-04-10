package de.qaware.mercury.rest.shop.dto.requestresponse;

import de.qaware.mercury.business.shop.SocialLinks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialLinksDto {
    @Nullable
    private String instagram;
    @Nullable
    private String facebook;
    @Nullable
    private String twitter;

    public SocialLinks toSocialLinks() {
        return new SocialLinks(instagram, facebook, twitter);
    }

    public static SocialLinksDto of(SocialLinks socialLinks) {
        return new SocialLinksDto(
            socialLinks.getInstagram(),
            socialLinks.getFacebook(),
            socialLinks.getTwitter()
        );
    }
}
