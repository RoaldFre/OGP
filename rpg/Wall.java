package rpg;

import rpg.exceptions.*;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of walls than can be slippery.
 *
 * @author Roald Frederickx
 */
public class Wall extends Border{
    /** 
     * Create a new wall with the given intrinsic slipperyness that 
     * replaces the given border.
     * 
     * @param border
     * The border to replace with this new wall.
     * @param isSlippery
     * The intrinsic slipperyness for this new wall.
     * @post
     *   | new.isSlippery() == isSlippery
     * @effect
     *   | super(border)
     */
    @Raw
    public Wall(Border border, boolean isSlippery) 
                                        throws IllegalArgumentException {
        super(border);
        this.isSlippery = isSlippery;
    }

    /** 
     * Create a new non-slippery wall that replaces the given border.
     * 
     * @param border
     * The border to replace with this new wall.
     * @effect
     *   | this(border, false)
     */
    @Raw
    public Wall(Border border) throws IllegalArgumentException,
                                      BorderConstraintsException {
        this(border, false);
    }

    /** 
     * Create a new wall with the given intrinsic slipperyness that borders 
     * the given square.
     * 
     * @param square 
     * The square that this wall will border.
     * @param isSlippery 
     * The intrinsic slipperyness for this new wall.
     * @post
     *   | new.isSlippery() == isSlippery
     * @effect
     *   | super(square)
     */
    @Raw
    Wall(Square square, boolean isSlippery) throws IllegalArgumentException {
        super(square);
        this.isSlippery = isSlippery;
    }

    /** 
     * Create a new non-slippery wall that borders the given square.
     * 
     * @param square 
     * The square that this wall will border.
     * @effect
     *   | this(square, false)
     */
    @Raw
    Wall(Square square) throws IllegalArgumentException {
        this(square, false);
    }

    @Override
    @Immutable
    public boolean isWall() {
        return true;
    }

    @Override
    @Immutable
    public boolean isSlippery() {
        return isSlippery;
    }

    /** 
     * Return a small string as a symbol for this border.
     * 
     * @return 
     *   | result.equals("W" + (isSlippery() ? "s" : ""))
     */
    @Override
    public String symbol() {
        return "W" + (isSlippery() ? "s" : "");
    }

    @Override
    @Immutable
    protected int openness() {
        return 10;
    }

    /** 
     * Variable registering the slipperyness of this wall.
     */
    private boolean isSlippery;
}

// vim: ts=4:sw=4:expandtab:smarttab

