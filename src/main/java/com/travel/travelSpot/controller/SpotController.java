package com.travel.travelSpot.controller;

import com.travel.travelSpot.domain.Spot;
import com.travel.travelSpot.dto.SpotDto;
import com.travel.travelSpot.dto.SpotResultDto;
import com.travel.travelSpot.service.SpotService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;

    @GetMapping("/api/v1/similarSpot")
    public SpotResultDto similarSpotV1(@RequestBody @Valid SpotDto spotDto) {
        return spotService.findSimilarSpot(spotDto.getName(), 0.2);
    }
}
