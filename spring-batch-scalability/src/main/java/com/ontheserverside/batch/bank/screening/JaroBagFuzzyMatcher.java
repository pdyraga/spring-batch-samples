package com.ontheserverside.batch.bank.screening;

import org.springframework.stereotype.Component;
import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link FuzzyMatcher} strategy implementation using Jaro metrics.
 *
 * @see FuzzyMatcher
 */
@Component
public final class JaroBagFuzzyMatcher implements FuzzyMatcher {

    private static final double JARO_SINGLE_WORD_PHRASE_MIN_MATCH_FACTOR = 0.9;
    private static final double JARO_WORD_MIN_MATCH_FACTOR = 0.8333;
    private static final double BAG_MIN_MATCH_FACTOR = 0.932;

    private final InterfaceStringMetric jaroMetric = new Jaro();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sequencesMatching(final String phrase1, final String phrase2) {
        final String phrase1LowerCase = removeNonAlphanumeric(phrase1).toLowerCase();
        final String phrase2LowerCase = removeNonAlphanumeric(phrase2).toLowerCase();

        final List<String> phrase1Bag = new ArrayList<>(Arrays.asList(phrase1LowerCase.split("\\s+")));
        final List<String> phrase2Bag = new ArrayList<>(Arrays.asList(phrase2LowerCase.split("\\s+")));

        if (phrase1Bag.size() == 1 && phrase2Bag.size() == 1) {
            // single-word phrases deserve special treatment...
            return jaroMetric(phrase1LowerCase, phrase2LowerCase) >= JARO_SINGLE_WORD_PHRASE_MIN_MATCH_FACTOR;
        } else if (phrase1Bag.size() < phrase2Bag.size()) {
            return compareBags(phrase1Bag, phrase2Bag) >= BAG_MIN_MATCH_FACTOR;
        } else {
            return compareBags(phrase2Bag, phrase1Bag) >= BAG_MIN_MATCH_FACTOR;
        }
    }

    private double compareBags(final List<String> bagA, final List<String> bagB) {
        if (bagA.size() > bagB.size()) {
            throw new IllegalArgumentException("bagA size must be less than bagB size for algorithm to work!");
        }

        double matchedLength = 0;
        double overalLength = 0;
        for (String word: bagA) {
            overalLength += word.length();
        }
        for (String word: bagB) {
            overalLength += word.length();
        }

        for (String a_word : bagA) {
            for (int i = 0; i < bagB.size(); i++) {
                String b_word = bagB.get(i);

                if (jaroMetric(a_word, b_word) > JARO_WORD_MIN_MATCH_FACTOR) {
                    bagB.remove(i);
                    matchedLength += a_word.length() + b_word.length();
                    break;
                }
            }
        }

        return matchedLength / overalLength;
    }

    private double jaroMetric(final String word1, final String word2) {
        return jaroMetric.getSimilarity(word1, word2);
    }

    private String removeNonAlphanumeric(final String str) {
        return str.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
}
