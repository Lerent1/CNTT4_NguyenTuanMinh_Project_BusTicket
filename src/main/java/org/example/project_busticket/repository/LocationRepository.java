package org.example.project_busticket.repository;

import org.example.project_busticket.model.Locations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Locations, Integer> {
}