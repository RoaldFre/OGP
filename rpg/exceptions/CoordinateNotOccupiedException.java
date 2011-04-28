package rpg.exceptions;

import rpg.*;
import be.kuleuven.cs.som.annotate.*;


/**
 * A class for signalling an error when requesting a square on a 
 * non-occupied coordinate.
 * 
 * @author Roald Frederickx
 */
public class CoordinateNotOccupiedException extends RuntimeException {
    /** 
     * Initialize this new coordinate not occupied exception with the given 
     * offending coordinate and dungeon.
     *
     * @param coordinate
     * The offending coordinate.
     * @param dungeon
     * The dungeon in which the coordinate is not occupied.
     * @post
     *   | new.getCoordinate().equals(coordinate)
     *   |      &amp;&amp; new.getDungeon() == dungeon
     */
    public CoordinateNotOccupiedException(Coordinate coordinate,
                                                Dungeon dungeon) {
        this.coordinate = coordinate;
        this.dungeon = dungeon;
    }

    /**
     * Return the offending coordinate for this coordinate not occupied exception.
     */
    @Immutable @Raw
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Variable referencing the offending coordinate for this coordinate 
     * not occupied exception.
     */
    private Coordinate coordinate;

    /**
     * Return the offending dungeon for this coordinate not occupied exception.
     */
    @Immutable @Raw
    public Dungeon getDungeon() {
        return dungeon;
    }
    
    /**
     * Variable referencing the offending dungeon for this coordinate 
     * not occupied exception.
     */
    private Dungeon dungeon;

    static final long serialVersionUID = 1;
}

// vim: ts=4:sw=4:expandtab:smarttab

