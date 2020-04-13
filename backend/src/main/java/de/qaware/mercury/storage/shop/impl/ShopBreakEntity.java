package de.qaware.mercury.storage.shop.impl;

import de.qaware.mercury.business.shop.Breaks;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop_break")
public class ShopBreakEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter
    @Column(name = "shop_id", nullable = false)
    private UUID shopId;

    @Setter
    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;


    @Setter
    @Column(name = "break_start", nullable = false, columnDefinition = "varchar")
    private LocalTime breakStart;

    @Setter
    @Column(name = "break_end", nullable = false, columnDefinition = "varchar")
    private LocalTime breakEnd;

    public Breaks.Break toBreak() {
        return Breaks.Break.of(breakStart, breakEnd);
    }
}
