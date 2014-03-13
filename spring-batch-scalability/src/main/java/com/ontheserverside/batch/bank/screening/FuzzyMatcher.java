package com.ontheserverside.batch.bank.screening;

/**
 * Strategy performing fuzzy matching of two sequences. Implementations may use
 * various distance metrics that best fit the particular purpose.
 */
public interface FuzzyMatcher {

    boolean sequencesMatching(String phrase1, String phrase2);
}
