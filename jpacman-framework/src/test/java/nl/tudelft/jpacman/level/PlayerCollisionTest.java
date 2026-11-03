package nl.tudelft.jpacman.level;


import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;


public class PlayerCollisionTest {
    PlayerCollisions playerCollisions;
    PacManSprites pacManSprites;
    BoardFactory boardFactory;

    @BeforeEach
    void setUp() {
        playerCollisions = new PlayerCollisions();
        pacManSprites = new PacManSprites();
        boardFactory = new BoardFactory(pacManSprites);
    }

    @Test
    void testPlayerVersusPellet() {

        Pellet pellet = new Pellet(1,pacManSprites.getPelletSprite());

        Player player = new Player(pacManSprites.getPacmanSprites(),pacManSprites.getPacManDeathAnimation());
        Square square = boardFactory.createGround();
        pellet.occupy(square);

        int value = player.getScore();
        playerCollisions.collide(player, pellet);
        Assertions.assertEquals(value + pellet.getValue(), player.getScore());
        assertThat(pellet.hasSquare()).isFalse();

    }

    @Test
    void testPelletVersusPlayer() {

        Pellet pellet = new Pellet(1,pacManSprites.getPelletSprite());

        Player player = new Player(pacManSprites.getPacmanSprites(),pacManSprites.getPacManDeathAnimation());

        int value = player.getScore();
        playerCollisions.collide(pellet, player);
        Assertions.assertEquals(value + pellet.getValue(), player.getScore());

    }

    @Test
    void testPlayerVersusGhost(){
        Player player = new Player(pacManSprites.getPacmanSprites(),pacManSprites.getPacManDeathAnimation());
        GhostFactory ghostFactory = new GhostFactory(pacManSprites);

        Ghost ghost = ghostFactory.createBlinky();
        playerCollisions.collide(player, ghost);
        assertThat(player.isAlive()).isFalse();

    }

    @Test
    void testGhostVersusPlayer(){
        Player player = new Player(pacManSprites.getPacmanSprites(),pacManSprites.getPacManDeathAnimation());
        GhostFactory ghostFactory = new GhostFactory(pacManSprites);

        Ghost ghost = ghostFactory.createBlinky();
        playerCollisions.collide(ghost, player);
        assertThat(player.isAlive()).isFalse();

    }

    @Test
    void testGhostVersusPellet(){
        GhostFactory ghostFactory = new GhostFactory(pacManSprites);
        Square square = boardFactory.createGround();
        Ghost ghost = ghostFactory.createBlinky();
        Pellet pellet = new Pellet(10,pacManSprites.getPelletSprite());
        pellet.occupy(square);
        playerCollisions.collide(ghost,pellet);
        assertThat(pellet.hasSquare()).isTrue();
    }
}
