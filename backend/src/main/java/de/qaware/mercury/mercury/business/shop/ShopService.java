package de.qaware.mercury.mercury.business.shop;

import java.util.List;

public interface ShopService {
    List<Shop> listAll();

    Shop create(String name, boolean enabled);

    void setAvailability(String shopId, boolean enabled);
}
