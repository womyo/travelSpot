package com.travel.travelSpot.dto;

import com.travel.travelSpot.domain.Location;
import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Data
public class GeoDto {

    private String latitude;
    private String longitude;

    public Location toEntity() {
        return Location.builder()
            .name("TEST NAME")
            .location(new GeoJsonPoint(Double.parseDouble(latitude), Double.parseDouble(longitude)))
            .build();
    }
}
