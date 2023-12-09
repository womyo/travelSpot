package com.travel.travelSpot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Spot {

    @Id
    @GeneratedValue
    @Column(name = "spot_id")
    private Long id;

    @Column(nullable = false, unique = true, name = "spot_name")
    private String name;

    @Column(nullable = false, name = "country_name")
    private String country;

    @Column(columnDefinition = "TEXT")
    private String types;

    @Column(columnDefinition = "TEXT")
    private String keywords;

    public Spot(String name, String country, String types, String keywords) {
        this.name = name;
        this.country = country;
        this.types = types;
        this.keywords = keywords;
    }
}
