package rpg;

import rpg.util.Coordinate;
import rpg.util.CoordinateSystem;
import rpg.util.Direction;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of shafts, representing a leaf dungeon that only has one degree 
 * of freedom in its coordinate system.
 *
 * @author Roald Frederickx
 */
public class Shaft<S extends Square> extends LeafDungeon<S> {

    /** 
     * Create a new shaft with the given origin, length and direction.
     * 
     * @param origin
     * The origin of this new shaft.
     * @param length 
     * The length of this new shaft.
     * @param direction 
     * The direction of this new shaft.
     * @effect
     *   | super(new CoordinateSystem(origin, 
     *   |                          origin.moveTo(direction, length - 1)))
     */
    public Shaft(Coordinate origin, long length, Direction direction) 
                                            throws IllegalArgumentException {
        super(new CoordinateSystem(origin, 
                                   origin.moveTo(direction, length - 1)));
    }

    /** 
     * Create a new shaft with the given direction and length and with a 
     * default origin.
     * 
     * @param length 
     * The length of this new shaft.
     * @param direction 
     * The direction of this new shaft.
     * @effect
     *   | this(new Coordinate(0, 0, 0), length, direction)
     */
    public Shaft(long length, Direction direction) 
                                            throws IllegalArgumentException {
        this(new Coordinate(0, 0, 0), length, direction);
    }

    /**
     * Checks whether the given coordinate system is a valid coordinate 
     * system for this shaft.
     *
     * @param coordSyst
     * The coordinate system to check.
     * @return
     * True iff this shaft can have the given coordinate system as a 
     * possible coordinate system, and the coordinate system has a 
     * size of 1 in at least two dimensions (it only has one degree of 
     * freedom or less).
     */
    @Raw
    public boolean canHaveAsCoordSyst(CoordinateSystem coordSyst) {
        if (!canPossiblyHaveAsCoordSyst(coordSyst))
            return false;
        Coordinate lo = coordSyst.getLowerBound();
        Coordinate hi = coordSyst.getUpperBound();
        if (lo.x == hi.x) {
            if (lo.y == hi.y)
                return true;
            if (lo.z == hi.z)
                return true;
            return false;
        } else
            return lo.y == hi.y  &&  lo.z == hi.z;
    }

    /** 
     * Checks whether this shaft can have the given square at the given 
     * coordinate.
     * 
     * @return 
     *   | result == (canPossiblyHaveAsSquareAt(coordinate, square)
     *   |      &amp;&amp; (!square instanceof Rock)
     *   |      &amp;&amp; hasValidBorders(coordinate, square))
     */
    @Raw
    @Override
    public boolean canHaveAsSquareAt(Coordinate coordinate, S square) {
        return canPossiblyHaveAsSquareAt(coordinate, square)
            && !(square instanceof Rock)
            && hasValidBorders(coordinate, square);
    }

    /** 
     * Check whether the given square has valid borders to belong to this 
     * shaft, if it would be placed at the given coordinate.
     * 
     * @param square
     * The square to check.
     * @param coordinate
     * The coordinate of the square to check.
     * @return
     *   | result == 
     *   |    (for each direction in Direction.values() :
     *   |        (!isOccupied(coordinate.moveTo(direction)) || 
     *   |            isValidSharedBorder(square.getBorderAt(direction))))
     */
    @Raw
    public boolean hasValidBorders(Coordinate coordinate, S square) {
        for (Direction direction : Direction.values())
            if (isOccupied(coordinate.moveTo(direction))
                    && !isValidSharedBorder(square.getBorderAt(direction)))
                return false;
        return true;
    }

    /** 
     * Checks whether the given border is a valid border for a square along 
     * the direction of this shaft, that borders another square of this 
     * shaft.
     * 
     * @param border 
     * The border to check.
     * @return
     *   | result == !border.isDoor()
     */
    public boolean isValidSharedBorder(Border border) {
        return !border.isDoor();
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

