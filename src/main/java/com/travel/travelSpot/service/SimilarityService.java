package com.travel.travelSpot.service;

import com.travel.travelSpot.domain.Spot;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    public double pearsonSimilarity(List<Double> v1, List<Double> v2) {
        double meanV1 = calculateMean(v1);
        double meanV2 = calculateMean(v2);

        List<Double> centeredV1 = centerVector(v1, meanV1);
        List<Double> centeredV2 = centerVector(v2, meanV2);

        double denom = Math.sqrt(sumOfSquares(centeredV1) * sumOfSquares(centeredV2));


        if (denom != 0) {
            return dotProduct(centeredV1, centeredV2) / denom;
        } else {
            return 0;
        }
    }

    private double calculateMean(List<Double> vector) {
        return vector.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private List<Double> centerVector(List<Double> vector, double mean) {
        return vector.stream().map(value -> value - mean).collect(Collectors.toList());
    }

    private double sumOfSquares(List<Double> vector) {
        return vector.stream().mapToDouble(value -> value * value).sum();
    }

    private double dotProduct(List<Double> v1, List<Double> v2) {
        return IntStream.range(0, v1.size())
            .mapToDouble(i -> v1.get(i) * v2.get(i))
            .sum();
    }
}
