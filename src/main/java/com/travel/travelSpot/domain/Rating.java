package com.travel.travelSpot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @Column(nullable = false)
    private double starRating;

    // 연관관계 매서드
    public void setSpot(Spot spot) {
        this.spot = spot;
        spot.getRatings().add(this);
    }

    public void setStarRating(double starRating) {
        this.starRating = starRating;
    }

    // 생성 매서드
    public static Rating createRating(Spot spot, double starRating) {
        Rating rating = new Rating();
        rating.setSpot(spot);
        rating.setStarRating(starRating);

        return rating;
    }
}
