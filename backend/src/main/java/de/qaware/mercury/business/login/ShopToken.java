package de.qaware.mercury.business.login;

import lombok.Value;

@Value(staticConstructor = "of")
public class ShopToken implements Token {
    String token;
}
