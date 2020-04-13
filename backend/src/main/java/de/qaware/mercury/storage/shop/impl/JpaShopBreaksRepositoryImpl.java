package de.qaware.mercury.storage.shop.impl;

import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.storage.shop.ShopBreaksRepository;
import de.qaware.mercury.util.Sets;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaShopBreaksRepositoryImpl implements ShopBreaksRepository {
    private final ShopBreakDataRepository shopBreakDataRepository;
    private final UUIDFactory uuidFactory;

    @Override
    public void insert(Shop.Id shopId, Breaks breaks) {
        log.debug("Inserting breaks {} for shop {}", breaks, shopId);

        List<ShopBreakEntity> entities = new ArrayList<>();
        for (var entry : breaks.groupedByDayOfWeek().entrySet()) {
            int dayOfWeek = entry.getKey().getValue();

            for (Breaks.Break aBreak : entry.getValue()) {
                entities.add(new ShopBreakEntity(uuidFactory.create(), shopId.getId(), dayOfWeek, aBreak.getStart(), aBreak.getEnd()));
            }
        }

        shopBreakDataRepository.saveAll(entities);
    }

    @Override
    public void update(Shop.Id shopId, Breaks breaks) {
        log.debug("Updating breaks to {} for shop {}", breaks, shopId);

        // We are lazy, therefore we just delete all breaks for the shop and insert them again
        List<ShopBreakEntity> existingBreaks = shopBreakDataRepository.findAllByShopId(shopId.getId());
        shopBreakDataRepository.deleteInBatch(existingBreaks);

        insert(shopId, breaks);
    }

    @Override
    public Breaks findByShopId(Shop.Id shopId) {
        List<ShopBreakEntity> breaks = shopBreakDataRepository.findAllByShopId(shopId.getId());

        Map<DayOfWeek, Set<ShopBreakEntity>> groupedByDayOfWeek = breaks.stream().collect(
            Collectors.groupingBy(e -> DayOfWeek.of(e.getDayOfWeek()), Collectors.toSet())
        );

        return new Breaks(
            toBreaks(groupedByDayOfWeek.get(DayOfWeek.MONDAY)),
            toBreaks(groupedByDayOfWeek.get(DayOfWeek.TUESDAY)),
            toBreaks(groupedByDayOfWeek.get(DayOfWeek.WEDNESDAY)),
            toBreaks(groupedByDayOfWeek.get(DayOfWeek.THURSDAY)),
            toBreaks(groupedByDayOfWeek.get(DayOfWeek.FRIDAY)),
            toBreaks(groupedByDayOfWeek.get(DayOfWeek.SATURDAY)),
            toBreaks(groupedByDayOfWeek.get(DayOfWeek.SUNDAY))
        );
    }

    private Set<Breaks.Break> toBreaks(@Nullable Set<ShopBreakEntity> entities) {
        if (entities == null) {
            return Set.of();
        }

        return Sets.map(entities, ShopBreakEntity::toBreak);
    }
}
