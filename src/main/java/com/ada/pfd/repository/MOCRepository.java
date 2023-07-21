package com.ada.pfd.repository;

import com.ada.pfd.domain.MOC;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MOC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MOCRepository extends JpaRepository<MOC, Long> {}
