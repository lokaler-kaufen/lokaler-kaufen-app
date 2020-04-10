package de.qaware.mercury.business.shop;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value
public class SocialLinks {
    @Nullable
    String instagram;
    @Nullable
    String facebook;
    @Nullable
    String twitter;

    public static SocialLinks none() {
        return new SocialLinks(null, null, null);
    }
}
