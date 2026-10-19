package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



/**
 * Test suite to confirm that {@link Square}s correctly link to and report
 * their neighbouring squares.
 */
class SquareNeighbourTest {

    /**
     * The square under test.
     */
    private Square square;

    /**
     * A neighbouring square used to test linking.
     */
    private Square neighbour;

    /**
     * Resets the squares under test.
     */
    @BeforeEach
    void setUp() {
        square = new BasicSquare();
        neighbour = new BasicSquare();
    }

    /**
     * Asserts that a square has no neighbour in a given direction before it
     * has been linked to one.
     */
    @Test
    void testNoNeighbourByDefault() {
        for (Direction dir : Direction.values()){
            assertThat(square.getSquareAt(dir)).isNull();
        }
    }

    /**
     * Asserts that after linking a square to a neighbour, that neighbour can
     * be retrieved in the given direction.
     */
    @Test
    void testLinkedNeighbour() {
        square.link(neighbour,Direction.NORTH);
        assertThat(square.getSquareAt(Direction.NORTH)).isEqualTo(neighbour);
    }

    /**
     * Asserts that linking a square is a one-way connection: linking square
     * to neighbour does not automatically link neighbour back to square.
     */
    @Test
    void testLinkIsOneWay() {
        square.link(neighbour,Direction.NORTH);
        assertThat(square.getSquareAt(Direction.NORTH)).isEqualTo(neighbour);
        assertThat(neighbour.getSquareAt(Direction.SOUTH)).isNotEqualTo(square);
        //Attention a bien mettre l'opposé vu qu'on parle en direction
    }
}
