package de.qaware.mercury.storage.reservation.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

interface ReservationDataRepository extends JpaRepository<ReservationEntity, UUID> {
    @Query("SELECT r FROM ReservationEntity r WHERE r.shopId = :shopId AND r.startTime >= :start AND r.endTime <= :end")
    List<ReservationEntity> findReservationsForShop(
        @Param("shopId") UUID shopId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end
    );
}
