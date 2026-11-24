package nl.tudelft.jpacman.npc.ghost;

import java.util.*;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * <p>
 * An implementation of the classic Pac-Man ghost Shadow.
 * </p>
 * <p>
 * Nickname: Blinky. As his name implies, Shadow is usually a constant shadow on
 * Pac-Man's tail. When he's not patrolling the top-right corner of the maze,
 * Shadow tries to find the quickest route to Pac-Man's position. Despite the
 * fact that Pinky's real name is Speedy, Shadow is actually the fastest of the
 * ghosts because of when there are only a few pellets left, Blinky drastically
 * speeds up, which can make him quite deadly. In the original Japanese version,
 * his name is Oikake/Akabei.
 * </p>
 * <p>
 * <b>AI:</b> When the ghosts are not patrolling in their home corners (Blinky:
 * top-right, Pinky: top-left, Inky: bottom-right, Clyde: bottom-left), Blinky
 * will attempt to shorten the distance between Pac-Man and himself. If he has
 * to choose between shortening the horizontal or vertical distance, he will
 * choose to shorten whichever is greatest. For example, if Pac-Man is four grid
 * spaces to the left, and seven grid spaces above Blinky, he'll try to move up
 * towards Pac-Man before he moves to the left.
 * </p>
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen 
 *
 */
public class Blinky extends Ghost {

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = 50;

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = 250;

    /**
     * The decision cycle
     */
    private int decision_cycle = 0;

    private Square target;
    /**
     * Creates a new "Blinky", a.k.a. "Shadow".
     *
     * @param spriteMap
     *            The sprites for this ghost.
     */
    // TODO Blinky should speed up when there are a few pellets left, but he
    // has no way to find out how many there are.
    public Blinky(Map<Direction, Sprite> spriteMap) {
        super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * When the ghosts are not patrolling in their home corners (Blinky:
     * top-right, Pinky: top-left, Inky: bottom-right, Clyde: bottom-left),
     * Blinky will attempt to shorten the distance between Pac-Man and himself.
     * If he has to choose between shortening the horizontal or vertical
     * distance, he will choose to shorten whichever is greatest. For example,
     * if Pac-Man is four grid spaces to the left, and seven grid spaces above
     * Blinky, he'll try to move up towards Pac-Man before he moves to the left.
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert hasSquare();
        if (target == null){
            target = getTarget(getSquare());
        }
        //Normal behavior
        if(decision_cycle < 8){
            incrementMovement();
            return chase();

        //Patrol
        }else {
            incrementMovement();
            if(decision_cycle == 12){
                setMovement();
            }
            if (target == null){
                return Optional.empty();
            }

            if (getSquare().equals(target)){
                return chase();
            }

            return firstStepTowards(target);
        }
    }

    private Optional<Direction> chase(){
        Unit nearest = Navigation.findNearest(Player.class, getSquare());
        if (nearest == null) {
            return Optional.empty();
        }
        assert nearest.hasSquare();
        Square target = nearest.getSquare();

        List<Direction> path = Navigation.shortestPath(getSquare(), target, this);
        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }

    private Optional<Direction> firstStepTowards(Square target) {
        List<Direction> path = Navigation.shortestPath(getSquare(), target, this);
        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }

    private Square getTarget(Square start) {
        if (start == null || !start.isAccessibleTo(this)) {
            return null;
        }
        Map<Square, int[]> coords = new HashMap<>();
        Queue<Square> queue = new ArrayDeque<>();
        coords.put(start, new int[]{0, 0});
        queue.add(start);

        Square best = start;
        while (!queue.isEmpty()) {
            Square current = queue.poll();
            int[] c = coords.get(current);
            int[] b = coords.get(best);
            if (c[1] < b[1] || (c[1] == b[1] && c[0] > b[0])) {
                best = current;
            }
            for (Direction d : Direction.values()) {
                Square next = current.getSquareAt(d);
                if (next != null && !coords.containsKey(next) && next.isAccessibleTo(this)) {
                    coords.put(next, new int[]{c[0] + d.getDeltaX(), c[1] + d.getDeltaY()});
                    queue.add(next);
                }
            }
        }
        return best;
    }

    /**
     * Va parcourir tout les squares du plateau pour obtenir les coordonées
     * @param ourSquare Le square où se trouve le ghost (ou square de départ)
     * @return Toutes les coordonnées du plateau
     */
    public Map<Square, int[]> getCoordMap(Square ourSquare) {
        Map<Square, int[]> coords = new HashMap<>();
        if (ourSquare == null) {
            return coords;
        }

        Square top = ourSquare;
        Set<Square> seenNorth = new HashSet<>();
        while (top.getSquareAt(Direction.NORTH) != null && seenNorth.add(top)) {
            top = top.getSquareAt(Direction.NORTH);
        }

        Square topLeft = top;
        Set<Square> seenWest = new HashSet<>();
        while (topLeft.getSquareAt(Direction.WEST) != null && seenWest.add(topLeft)) {
            topLeft = topLeft.getSquareAt(Direction.WEST);
        }

        Square rowStart = topLeft;
        Set<Square> seenRows = new HashSet<>();
        int y = 0;
        while (rowStart != null && seenRows.add(rowStart)) {
            Square current = rowStart;
            Set<Square> seenCols = new HashSet<>();
            int x = 0;
            while (current != null && seenCols.add(current)) {
                coords.putIfAbsent(current, new int[]{x, y});
                current = current.getSquareAt(Direction.EAST);
                x++;
            }
            rowStart = rowStart.getSquareAt(Direction.SOUTH);
            y++;
        }

        return coords;
    }

    private void setMovement(){
        this.decision_cycle = 0;
    }

    private void incrementMovement(){
        this.decision_cycle++;
    }
}
