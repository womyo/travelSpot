package com.travel.travelSpot.service;

import com.travel.travelSpot.domain.Spot;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SimilarityService {


    public double jaccardSimilarity(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) {
            return 0;
        }

        double intersectionSize = set1.stream().filter(set2::contains).count();
        double unionSize = set1.size() + set2.size() - intersectionSize;

        return intersectionSize / unionSize;
    }

    public Set<String> makeSetData(Spot meta) {
        String[] typesArray = meta.getTypes().split(",\\s");
        String[] keywordsArray = meta.getKeywords().split(",\\s*");

        Set<String> resultSet = Stream.concat(Arrays.stream(typesArray), Arrays.stream(keywordsArray))
            .collect(Collectors.toSet());

        return resultSet;
    }

    public void pearsonSimilarity() {

    }
}
