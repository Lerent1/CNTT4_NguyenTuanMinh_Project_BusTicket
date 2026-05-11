package org.example.project_busticket.repository;

import org.example.project_busticket.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BusRepository extends JpaRepository<Bus, Long> {

    @Query("SELECT b FROM Bus b WHERE b.id = :id")
    Bus getBusById(@Param("id") Long id);

}
