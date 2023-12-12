package com.travel.travelSpot.repository;

import com.travel.travelSpot.domain.Spot;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    Optional<Spot> findByName(String name);
}
