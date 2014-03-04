package com.ontheserverside.batch.bank.screening;

import org.springframework.stereotype.Component;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;
import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;

/**
 * {@link FuzzyMatcher} strategy implementation using Cosine and Jaro metrics.
 *
 * @see FuzzyMatcher
 */
@Component
public final class CosineJaroFuzzyMatcher implements FuzzyMatcher {

    private static final double COSINE_MIN_MATCH_FACTOR = 0.5;
    private static final double JARO_MIN_MATCH_FACTOR = 0.89;

    private final InterfaceStringMetric cosineMetric = new CosineSimilarity();
    private final InterfaceStringMetric jaroMetric = new Jaro();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sequencesMatching(final String word1, final String word2) {
        final String word1LowerCase = word1.toLowerCase();
        final String word2LowerCase = word2.toLowerCase();

        if (cosineMetric.getSimilarity(word1LowerCase, word2LowerCase) >= COSINE_MIN_MATCH_FACTOR) {
            return true;
        }

        return jaroMetric.getSimilarity(word1LowerCase, word2LowerCase) >= JARO_MIN_MATCH_FACTOR;
    }
}
