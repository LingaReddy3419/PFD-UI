package com.ada.pfd.repository;

import com.ada.pfd.domain.ImpellerType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImpellerType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImpellerTypeRepository extends JpaRepository<ImpellerType, Long> {}
