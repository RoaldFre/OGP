package rpg;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class representing a coordinate.
 *
 * @author Roald Frederickx
 */
@Value
public class Coordinate {
    /** 
     * Create a new coordinate with the given components.
     * 
     * @param x 
     * The component in the x-direction for this new coordinate.
     * @param y 
     * The component in the y-direction for this new coordinate.
     * @param z 
     * The component in the z-direction for this new coordinate.
     * @post
     *   | new.x == x  &amp;&amp;  new.y == y  &amp;&amp;  new.z == z
     */
    public Coordinate(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** 
     * Variables registering the components of this coordinate.
     */
    public final long x, y, z;

    /** 
     * Returns whether or not this coordinate is bounded by the given 
     * bounds.
     * 
     * @param lowerBound 
     * The lower bound for this coordinate.
     * @param upperBound 
     * The upper bound for this coordinate.
     * @return 
     *   | result == (formsValidBoundingBox(lowerBound, this)
     *   |                  &amp;&amp; formsValidBoundingBox(this. upperBound))
     */
    public boolean isBoundedBy(Coordinate lowerBound, Coordinate upperBound) {
        return formsValidBoundingBox(lowerBound, this) &&
            formsValidBoundingBox(this, upperBound);
    }

    /** 
     * Returns whether or not the given boundaries form a valid bounding 
     * box (ie a box with a thickness in each dimension of at least 0).
     * 
     * @param lowerBound
     * The lower bound of the bounding box.
     * @param upperBound
     * The lower bound of the bounding box.
     * @return
     *   | if (lowerBound == null  ||  upperBound == null)
     *   |      then result == false
     *   | else result == (lowerBound.x &lt;= upperBound.x
     *   |          &amp;&amp; lowerBound.y &lt;= upperBound.y
     *   |          &amp;&amp; lowerBound.z &lt;= upperBound.z)
     */
    public static boolean formsValidBoundingBox(Coordinate lowerBound,
                                                Coordinate upperBound) {
        if (lowerBound == null  ||  upperBound == null)
            return false;
        return lowerBound.x <= upperBound.x
            && lowerBound.y <= upperBound.y
            && lowerBound.z <= upperBound.z;
    }

    /** 
     * Return a new coordinate that is the result of moving this coordinate 
     * in the given direction.
     * 
     * @param direction 
     * The direction in which to move this coordinate.
     * @return
     * The coordiante resulting in moving this coordinate in the given 
     * direction.
     * @throws IllegalArgumentException 
     * The given direction is unknown or not effective.
     */
    public Coordinate moveTo(Direction direction) throws IllegalArgumentException {
        if (direction == null)
            throw new IllegalArgumentException();
        long new_x = x;
        long new_y = y;
        long new_z = z;
        switch (direction) {
            case EAST:
                new_x++; break;
            case WEST:
                new_x--; break;
            case NORTH:
                new_y++; break;
            case SOUTH:
                new_y--; break;
            case UP:
                new_z++; break;
            case DOWN:
                new_z--; break;
            default:
                throw new IllegalArgumentException();
        }
        return new Coordinate(new_x, new_y, new_z);
    }

    /** 
     * Check for equality between this coordinate and a given coordinate.
     * 
     * @param other
     * The coordinate to compare this coordinate to.
     * @return
     * True iff the given object is an effective coordinate and this 
     * coordinate is equal to the given coordinate.
     *   | if (other == null)
     *   |      then result == false
     *   | else if (this.getClass() != other.getClass())
     *   |      then result == false
     *   | else result == (x == otherCoord.x
     *   |                  &amp;&amp; y == otherCoord.y
     *   |                  &amp;&amp; z == otherCoord.z)
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;
        Coordinate otherCoord = (Coordinate) other;
        return x == otherCoord.x
            && y == otherCoord.y
            && z == otherCoord.z;
    }
    
    /** 
     * Returns a hashcode for this coordinate object.
     * 
     * @return
     *   | result == (int) (x + y + z)
     */
    @Override
    public int hashCode() {
        //light and easy
        return (int) (x + y + z);

        /*
        //Goedel encoding may be a bit of overkill, but at least it's 
        //guaranteed to be unique (well, modulo integer wrap-around) :-)
        //Negative coordinates will collide with positive ones, though.
        return (new java.lang.Double(
                    java.lang.Math.pow(2, java.lang.Math.abs(x)) 
                    * java.lang.Math.pow(3, java.lang.Math.abs(y))
                    * java.lang.Math.pow(5, java.lang.Math.abs(z)))).intValue();
        */
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

