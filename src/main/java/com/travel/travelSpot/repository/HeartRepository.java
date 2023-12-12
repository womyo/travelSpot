package com.travel.travelSpot.repository;

import com.travel.travelSpot.domain.Heart;
import com.travel.travelSpot.domain.Member;
import com.travel.travelSpot.domain.Spot;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByMemberAndSpot(Member member, Spot spot);
}
