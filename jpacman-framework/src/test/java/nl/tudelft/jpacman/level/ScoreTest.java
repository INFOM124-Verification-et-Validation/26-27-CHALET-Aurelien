package nl.tudelft.jpacman.level;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


/**
 * Test suite to confirm that {@link Score} correctly stores and
 * accumulates points.
 */
class ScoreTest {

    /**
     * The score under test.
     */
    private Score score;

    /**
     * Resets the score under test.
     */
    @BeforeEach
    void setUp() {
        score = new Score(0);
    }

    /**
     * Asserts that a freshly created score starts at the given amount of
     * points.
     */
    @Test
    void testInitialPoints() {
        assertThat(score.getPoints()).isEqualTo(0);
    }

    /**
     * Asserts that adding points increases the total by that amount.
     */
    @Test
    void testAddPoints() {
        int toAdd = 5;
        int valPrev = score.getPoints();
        score.addPoints(toAdd);
        assertThat(score.getPoints()).isEqualTo(valPrev + toAdd);
    }

    /**
     * Asserts that adding points twice accumulates both amounts.
     */
    @Test
    void testAddPointsTwice() {
        int toAdd = 5;
        int preVal = score.getPoints();
        score.addPoints(toAdd);
        score.addPoints(toAdd);
        assertThat(score.getPoints()).isEqualTo(preVal + 2*toAdd);
    }

    /**
     * Asserts that adding negative points decreases the total by that amount.
     */
    @Test
    void testAddNegativePoints() {
        int toAdd = -5;
        int preVal = score.getPoints();
        score.addPoints(toAdd);
        assertThat(score.getPoints()).isEqualTo(preVal + toAdd);
    }
}
