package de.qaware.mercury.mercury.storage.reservation.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ReservationDataRepository extends JpaRepository<ReservationEntity, UUID> {
}
