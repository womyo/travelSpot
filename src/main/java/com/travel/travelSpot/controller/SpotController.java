package com.travel.travelSpot.controller;

import com.travel.travelSpot.domain.Member;
import com.travel.travelSpot.dto.HeartResponseDto;
import com.travel.travelSpot.dto.SpotDto;
import com.travel.travelSpot.dto.SpotResponseDto;
import com.travel.travelSpot.service.HeartService;
import com.travel.travelSpot.service.SpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SpotController {
    private final SpotService spotService;
    private final HeartService heartService;

    @GetMapping("/api/v1/similarSpot")
    public SpotResponseDto similarSpot(@RequestBody @Valid SpotDto spotDto) {
        return spotService.findSimilarSpot(spotDto.getName(), 0.2);
    }

    @PostMapping("/api/v1/spot/{spotId}/heart")
    public HeartResponseDto heartSpot(@PathVariable("spotId") Long spotId, Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return heartService.addHeart(userId, spotId);
    }
}
