package com.travel.travelSpot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestDto {

    @NotBlank(message = "accessToken을 입력해주세요")
    private String accessToken;

    @NotBlank(message = "refreshToken을 입력해주세요")
    private String refreshToken;
}
