package com.ada.pfd.repository;

import com.ada.pfd.domain.General;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the General entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneralRepository extends JpaRepository<General, Long> {}
