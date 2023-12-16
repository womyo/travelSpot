package com.travel.travelSpot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotRequestDto {

    @NotBlank(message = "여행지를 입력해주세요")
    String name;
}
