package de.qaware.mercury.business.login;

import lombok.Value;

@Value(staticConstructor = "of")
public class ShopCreationToken implements Token {
    String token;
}
