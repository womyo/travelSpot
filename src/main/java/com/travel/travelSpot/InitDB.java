package com.travel.travelSpot;

import com.travel.travelSpot.domain.Rating;
import com.travel.travelSpot.domain.Spot;
import com.travel.travelSpot.service.SpotService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

//    @PostConstruct
//    public void init() {
//        initService.dbInit1();
//    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final SpotService spotService;

        public void dbInit1() {
            Random random = new Random();
            List<Spot> spots = spotService.findSpots();

            for(Spot spot: spots) {
                int userCount = 5;

                for (int i=0; i<userCount; i++) {
                    double randomStarRating = Math.round(random.nextDouble() * 10) / 2.0;
                    Rating rating = Rating.createRating(spot, randomStarRating);
                    em.persist(rating);
                }
            }
        }
    }
}
