package com.travel.travelSpot.controller;

import com.travel.travelSpot.domain.Location;
import com.travel.travelSpot.dto.GeoDto;
import com.travel.travelSpot.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationRepository locationRepository;

    @PostMapping("/location")
    public String saveLocation(@RequestBody GeoDto geoDto) {
        Location location = locationRepository.save(geoDto.toEntity());

        return location.getId();
    }
}
