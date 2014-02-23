package com.ontheserverside.batch.bank.screening;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Group of tests used to adjust sensitivity factors of metrics - we don't want to test framework here, huh? ;)
 */
public class CosineJaroFuzzyMatcherTest {

    private CosineJaroFuzzyMatcher matcher = new CosineJaroFuzzyMatcher();

    @Test
    public void shouldNotConsiderWordsOrder() {
        assertThat(matcher.sequencesMatching("conan the conqueror", "the conqueror conan"), is(true));
    }

    @Test
    public void shouldBeCaseInsensitive() {
        assertThat(matcher.sequencesMatching("john rambo", "JOHN RAMBO"), is(true));
    }

    @Test
    public void shouldMatchForOneLetterTypos() {
        assertThat(matcher.sequencesMatching("jovn rambo", "john rambo"), is(true));
        assertThat(matcher.sequencesMatching("john rampo", "john rambo"), is(true));
    }

    @Test
    public void shouldMatchForOneLetterMissing() {
        assertThat(matcher.sequencesMatching("conan the onqueror", "conan the conqueror"), is(true));
        assertThat(matcher.sequencesMatching("coan the conqueror", "conan the conqueror"), is(true));
    }

    @Test
    public void shouldMatchForOneLetterAdded() {
        assertThat(matcher.sequencesMatching("terrorist", "terroristt"), is(true));
        assertThat(matcher.sequencesMatching("bomb", "bombb"), is(true));
        assertThat(matcher.sequencesMatching("my new gun", "my new gunn"), is(true));
        assertThat(matcher.sequencesMatching("gun", "gunn"), is(true));
    }

    @Test
    public void shouldMatchForOneLetterMisplaced() {
        assertThat(matcher.sequencesMatching("conna the conqueror", "conan the conqueror"), is(true));
        assertThat(matcher.sequencesMatching("conan the ocnqueror", "conan the conqueror"), is(true));
    }

    @Test
    public void shouldMatchForOneLetterTypoAndDifferentWordsOrder() {
        assertThat(matcher.sequencesMatching("rambo jovn", "john rambo"), is(true));
        assertThat(matcher.sequencesMatching("rampo john", "john rambo"), is(true));
    }

    @Test
    public void shouldMatchForOneLetterMissingAndDifferentWordsOrder() {
        assertThat(matcher.sequencesMatching("the brbarian conan", "conan the barbarian"), is(true));
        assertThat(matcher.sequencesMatching("barbarian conan th", "conan the barbarian"), is(true));
    }

    @Test
    public void shouldMatchForOneLetterMisplacedAndDifferentWordsOrder() {
        assertThat(matcher.sequencesMatching("the cnoan conqueror", "conan the conqueror"), is(true));
        assertThat(matcher.sequencesMatching("conan ocnqueror the", "conan the conqueror"), is(true));
    }

    @Test
    public void shouldMatchWithAdditionalWords() {
        assertThat(matcher.sequencesMatching("conan the barbarian valhalla", "conan the barbarian"), is(true));
        assertThat(matcher.sequencesMatching("conan the barbarian valhalla fighter", "conan the barbarian"), is(true));
        assertThat(matcher.sequencesMatching("john rambo", "john rambo famous vietnam veteran"), is(true));
    }

    @Test
    public void shouldNotMatchForDifferentWords() {
        // it's hard do define what "different words" are in terms of fuzzy matching;
        // this test (as well as all others within this class) should be considered just
        // as a way to adjust sensitivity factors of metrics
        assertThat(matcher.sequencesMatching("bombai", "bomb"), is(false));
        assertThat(matcher.sequencesMatching("attack", "attention"), is(false));
        assertThat(matcher.sequencesMatching("taliban", "talisman"), is(false));
        assertThat(matcher.sequencesMatching("nuclear", "uncle"), is(false));
        assertThat(matcher.sequencesMatching("gun", "gunner"), is(false));
        assertThat(matcher.sequencesMatching("uranium", "uranus"), is(false));
    }
}
