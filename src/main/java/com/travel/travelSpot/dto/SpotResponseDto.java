package com.travel.travelSpot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotResponseDto {

    private Long id;
    private String name;
    private String country;
    private double score;
}
