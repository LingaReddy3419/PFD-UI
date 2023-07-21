package com.ada.pfd.repository;

import com.ada.pfd.domain.Reactor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reactor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReactorRepository extends JpaRepository<Reactor, Long> {}
