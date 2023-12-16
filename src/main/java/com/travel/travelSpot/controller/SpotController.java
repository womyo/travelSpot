package com.travel.travelSpot.controller;

import com.travel.travelSpot.dto.HeartDto;
import com.travel.travelSpot.dto.SpotRequestDto;
import com.travel.travelSpot.dto.SpotDto;
import com.travel.travelSpot.service.HeartService;
import com.travel.travelSpot.service.SpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SpotController {
    private final SpotService spotService;
    private final HeartService heartService;

    @GetMapping("/api/v1/similarSpot")
    public SpotDto similarSpot(@RequestBody @Valid SpotRequestDto spotDto) {
        return spotService.findSimilarSpot(spotDto.getName(), 0.2);
    }

    @PostMapping("/api/v1/spot/{spotId}/heart")
    public HeartDto heartSpot(@PathVariable("spotId") Long spotId) {
        return heartService.addHeart(spotId);
    }
}
