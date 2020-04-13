package de.qaware.mercury.storage.shop.impl;

import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.storage.shop.ShopBreaksRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaShopBreaksRepositoryImpl implements ShopBreaksRepository {
    private final ShopBreakDataRepository shopBreakDataRepository;
    private final UUIDFactory uuidFactory;

    @Override
    public void insert(Shop.Id shopId, Breaks breaks) {
        List<ShopBreakEntity> entities = new ArrayList<>();

        for (var entry : breaks.groupedByDayOfWeek().entrySet()) {
            int dayOfWeek = entry.getKey().getValue();

            for (Breaks.Break aBreak : entry.getValue()) {
                entities.add(new ShopBreakEntity(uuidFactory.create(), shopId.getId(), dayOfWeek, aBreak.getStart(), aBreak.getEnd()));
            }
        }

        shopBreakDataRepository.saveAll(entities);
    }
}
