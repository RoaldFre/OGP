package rpg.exceptions;

import rpg.*;
import rpg.util.Coordinate;
import be.kuleuven.cs.som.annotate.*;


/**
 * A class for signalling a collision with an already occpied coordinate.
 * 
 * @author Roald Frederickx
 */
public class CoordinateOccupiedException extends RuntimeException {
    /** 
     * Initialize this new coordinate occupied exception with the given 
     * offending coordinate and dungeon.
     *
     * @param coordinate
     * The offending coordinate.
     * @param dungeon
     * The dungeon in which the coordinate is already occupied.
     * @post
     *   | new.getCoordinate().equals(coordinate)
     *   |      &amp;&amp; new.getDungeon() == dungeon
     */
    public CoordinateOccupiedException(Coordinate coordinate,
                                                Dungeon<?> dungeon) {
        this.coordinate = coordinate;
        this.dungeon = dungeon;
    }

    /**
     * Return the offending coordinate for this coordinate occupied exception.
     */
    @Immutable @Raw
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Variable referencing the offending coordinate for this coordinate 
     * occupied exception.
     */
    private final Coordinate coordinate;

    /**
     * Return the offending dungeon for this coordinate occupied exception.
     */
    @Immutable @Raw
    public Dungeon<?> getDungeon() {
        return dungeon;
    }
    
    /**
     * Variable referencing the offending dungeon for this coordinate 
     * occupied exception.
     */
    private final Dungeon<?> dungeon;

    static final long serialVersionUID = 1;
}

// vim: ts=4:sw=4:expandtab:smarttab

