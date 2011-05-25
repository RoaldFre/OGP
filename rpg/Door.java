package rpg;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of doors than can be opened or closed.
 *
 * @author Roald Frederickx
 */
public class Door extends Border{
    /** 
     * Create a new door with the given intrinsic slipperyness that borders 
     * the given square.
     * 
     * @param square 
     * The square that this door will border.
     * @param isSlippery 
     * The 'openness' of this new door.
     * @post
     *   | isOpen() == isOpened
     * @effect
     *   | super(square)
     */
    @Raw
    public Door(Square square, boolean isOpened) {
        super(square);
        this.isOpened = isOpened;
    }

    /** 
     * Create a new door with the given 'openness' that replaces the given 
     * border.
     * 
     * @param border
     * The border to replace with this new door.
     * @param isOpened
     * The 'openness' of this new door.
     * @post
     *   | isOpen() == isOpened
     * @effect
     *   | super(border)
     */
    @Raw
    public Door(Border border, boolean isOpened) {
        super(border);
        this.isOpened = isOpened;
    }

    @Override
    @Basic
    public boolean isOpen() {
        return isOpened;
    }

    @Override
    @Immutable
    @Basic
    public boolean isDoor() {
        return true;
    }

    /** 
     * Return a small string as a symbol for this door.
     * 
     * @return 
     *   | result.equals("D" + (isOpen() ? "o" : "c"))
     */
    @Override
    public String symbol() {
        return "D" + (isOpen() ? "o" : "c");
    }

    @Override
    @Immutable
    protected int openness() {
        return 20;
    }

    /**
     * Open this door.
     *
     * @post
     *   | new.isOpened() == true
     * @effect
     *   | if (isSharedByTwoSquares())
     *   |      then getASquare().equilibrateMyArea()
     */
    @Raw
    public void open() {
        isOpened = true;
        if (isSharedByTwoSquares())
            getASquare().equilibrateMyArea();
    }

    /**
     * Close this door.
     *
     * @post
     *   | new.isOpened() == false
     */
    @Raw
    public void close() {
        this.isOpened = false;
    }
    
    /**
     * Variable registering the opened or closed state for this door.
     */
    private boolean isOpened;
}

// vim: ts=4:sw=4:expandtab:smarttab

