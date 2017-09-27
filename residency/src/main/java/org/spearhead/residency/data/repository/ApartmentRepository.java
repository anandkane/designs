package org.spearhead.residency.data.repository;

import org.spearhead.residency.data.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
}
