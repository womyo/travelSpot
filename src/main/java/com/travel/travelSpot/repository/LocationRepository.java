package com.travel.travelSpot.repository;

import com.travel.travelSpot.domain.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String> {

}
