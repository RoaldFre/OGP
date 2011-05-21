package rpg;

import be.kuleuven.cs.som.annotate.*;

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
     * Create a new level with the given direction and length and with a 
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
     * size of 1 coordinate in at least two dimensions.
     */
    @Raw
    public boolean canHaveAsCoordSyst(CoordinateSystem coordSyst) {
        if (!canHaveAsPossibleCoordSyst(coordSyst))
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
}

// vim: ts=4:sw=4:expandtab:smarttab

