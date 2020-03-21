package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ShopNotFoundException extends BusinessException {
    Shop.Id id;
}
