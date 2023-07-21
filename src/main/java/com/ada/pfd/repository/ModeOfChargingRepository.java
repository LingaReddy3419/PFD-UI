package com.ada.pfd.repository;

import com.ada.pfd.domain.ModeOfCharging;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ModeOfCharging entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModeOfChargingRepository extends JpaRepository<ModeOfCharging, Long> {}
