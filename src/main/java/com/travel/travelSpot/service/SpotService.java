package com.travel.travelSpot.service;

import com.travel.travelSpot.domain.Rating;
import com.travel.travelSpot.domain.Spot;
import com.travel.travelSpot.dto.SpotResultDto;
import com.travel.travelSpot.repository.SpotRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public SpotResultDto findSimilarSpot(String inputName, double alpha) {
        Spot inputMeta = spotRepository.findByName(inputName);

        if (inputMeta == null) {
            throw new IllegalArgumentException("Spot is not found. SpotName = " + inputName);
        }

        List<Spot> allSpots = spotRepository.findAll();
        List<SpotResultDto> result = new ArrayList<>();

        Set<String> inputSet = similarityService.makeSetData(inputMeta);

        for (Spot spotMeta: allSpots) {
            if (spotMeta.getName().equals(inputName)) {
                continue;
            }

            Set<String> spotSet = similarityService.makeSetData(spotMeta);

            double jaccard = similarityService.jaccardSimilarity(inputSet, spotSet);
            double pearson = similarityService.pearsonSimilarity(inputMeta.getStars(), spotMeta.getStars());

            double score = alpha * pearson + (1 - alpha) * jaccard;

            result.add(new SpotResultDto(spotMeta.getName(), spotMeta.getCountry(), score));
        }

        result.sort(Comparator.comparingDouble(SpotResultDto::getScore).reversed());

        return result.get(0);
    }
}
