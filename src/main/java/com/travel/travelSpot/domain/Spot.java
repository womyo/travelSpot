package com.travel.travelSpot.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "spots")
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

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();

    public List<Double> getStars() {
        return ratings.stream()
            .map(Rating::getStarRating)
            .collect(Collectors.toList());
    }

    @Builder
    public Spot(String name, String country, String types, String keywords) {
        this.name = name;
        this.country = country;
        this.types = types;
        this.keywords = keywords;
    }
}
