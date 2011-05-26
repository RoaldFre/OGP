package rpg.exceptions;

import rpg.square.Border;
import rpg.square.Square;
import rpg.util.Direction;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signalling a violation of the constraints on borders.
 * 
 * @author Roald Frederickx
 */
public class BorderConstraintsException extends RuntimeException {
    /** 
     * Initialize this new border constraints exception with the given 
     * offending square, border and direction.
     *
     * @param square
     * The offending square for this new border constraints exception.
     * @param border
     * The offending border for this new border constraints exception.
     * @param direction
     * The offending direction for this new border constraints exception.
     * @post
     * The offending border for this new border constraints exception is 
     * equal to the given offending border.
     *   | new.getBorder() == border
     * @post
     * The offending square for this new border constraints exception is 
     * equal to the given offending square.
     *   | new.getSquare() == square
     * @post
     * The offending direction for this new border constraints exception is 
     * equal to the given offending direction.
     *   | new.getDirection() == direction
     */
    public BorderConstraintsException(Square square, Border border,
                                                    Direction direction) {
        this.square = square;
        this.border = border;
        this.direction = direction;
    }

    /** 
     * Create a new default border constraints exception.
     *
     * @post
     *   | new.getBorder() == null
     * @post
     *   | new.getSquare() == null
     * @post
     *   | new.getDirection() == null
     */
    public BorderConstraintsException() {
        this.square = null;
        this.border = null;
        this.direction = null;
    }

    /**
     * Return the offending square for this border constraints exception.
     */
    @Immutable @Raw
    public Square getSquare() {
        return square;
    }
    
    /**
     * Variable registering the offending square for this border 
     * constraints exception.
     */
    private Square square;

    /**
     * Return the offending border for this border constraints exception.
     */
    @Immutable @Raw
    public Border getBorder() {
        return border;
    }
    
    /**
     * Variable registering the offending border for this border 
     * constraints exception.
     */
    private final Border border;

    /**
     * Return the offending direction for this border constraints 
     * exception.
     */
    @Immutable @Raw
    public Direction getDirection() {
        return direction;
    }
    
    /**
     * Variable registering the offending direction for this border 
     * constraints exception.
     */
    private final Direction direction;

    static final long serialVersionUID = 1;
}

// vim: ts=4:sw=4:expandtab:smarttab

