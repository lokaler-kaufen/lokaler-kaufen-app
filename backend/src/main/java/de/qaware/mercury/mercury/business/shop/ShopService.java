package de.qaware.mercury.mercury.business.shop;

import java.util.List;

public interface ShopService {
    List<Shop> listAll();

    Shop create(String name);
}
