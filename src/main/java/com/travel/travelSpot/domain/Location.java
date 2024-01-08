package com.travel.travelSpot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
public class Location {

    @Id
    private String id;

    @Column(name = "location_name")
    private String name;

    private GeoJsonPoint location;

    @Builder
    public Location(String id, String name, GeoJsonPoint location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}
