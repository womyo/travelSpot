package com.travel.travelSpot.service;

import com.travel.travelSpot.domain.Spot;
import com.travel.travelSpot.dto.SpotDto;
import com.travel.travelSpot.repository.SpotRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final SimilarityService similarityService;

    @Transactional(readOnly = true)
    public List<Spot> findSpots() {
        return spotRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SpotDto findSimilarSpot(String inputName, double alpha) {
        Spot inputMeta = spotRepository.findByName(inputName).orElseThrow(() -> new EntityNotFoundException(inputName + " -> 장소 데이터베이스에서 찾을 수 없습니다."));

        List<Spot> allSpots = spotRepository.findAll();
        List<SpotDto> result = new ArrayList<>();

        Set<String> inputSet = similarityService.makeSetData(inputMeta);

        for (Spot spotMeta: allSpots) {
            if (spotMeta.getName().equals(inputName)) {
                continue;
            }

            Set<String> spotSet = similarityService.makeSetData(spotMeta);

            double jaccard = similarityService.jaccardSimilarity(inputSet, spotSet);
            double pearson = similarityService.pearsonSimilarity(inputMeta.getStars(), spotMeta.getStars());

            double score = alpha * pearson + (1 - alpha) * jaccard;

            result.add(new SpotDto(spotMeta.getId(), spotMeta.getName(), spotMeta.getCountry(), score));
        }

        result.sort(Comparator.comparingDouble(SpotDto::getScore).reversed());

        return result.get(0);
    }
}
