package rpg.util;

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
     * @effect
     *   result == moveTo(direction, 1)
     */
    public Coordinate moveTo(Direction direction) throws IllegalArgumentException {
        return moveTo(direction, 1);
    }

    /** 
     * Return a new coordinate that is the result of moving this coordinate 
     * in the given direction for the given number of steps.
     * 
     * @param direction 
     * The direction in which to move this coordinate.
     * @param steps
     * The number of steps in which to move this coordinate.
     * @return
     * The coordiante resulting in moving this coordinate in the given 
     * direction for the given number of steps.
     * @throws IllegalArgumentException 
     *   | direction == null
     */
    public Coordinate moveTo(Direction direction, long steps)
                                        throws IllegalArgumentException {
        if (direction == null)
            throw new IllegalArgumentException();
        return direction.moveCoordinate(this, steps);
    }

    /** 
     * Return a new coordinate that is the result of adding the given 
     * coordinate to this coordinate.
     * 
     * @param offset
     * The coordinate to add to this coordinate.
     * @pre
     *   | offset != null
     * @return
     * A new coordinate that is the result of adding the given coordinate 
     * to this coordinate.
     */
    public Coordinate add(Coordinate offset) {
        assert offset != null;
        return new Coordinate(x + offset.x, y + offset.y, z + offset.z);
    }

    /** 
     * Return a new coordinate that is the result of subtracting the given 
     * coordinate from this coordinate.
     * 
     * @param offset 
     * The coordinate to subtract from this coordinate.
     * @pre
     *   | offset != null
     * @return 
     * A new coordinate that is the result of subtracting the given 
     * coordinate from this coordinate.
     */
    public Coordinate sub(Coordinate offset) {
        assert offset != null;
        return add(offset.mirror());
    }

    /** 
     * Return a new coordinate that is the result of mirroring this 
     * coordinate around (0, 0, 0).
     *
     * @return 
     * A new coordinate that is the result of mirroring this 
     * coordinate around (0, 0, 0).
     */
    public Coordinate mirror() {
        return new Coordinate(-x, -y, -z);
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
        // Go for the maximum base possible without overflowing an integer. 
        // Hopefully the compiler can calculate this in advance and 
        // hard-code the result to make it faster and avoid floating point 
        // calculations.
        int base = (int) java.lang.Math.floor(java.lang.Math.pow(Integer.MAX_VALUE,1/3.));
        return (int) (x  +  base * y  +  base * base * z);
        // Note: this does collide when the 'span' of the coordinates 
        // exceed the cube root of Integer.MAX_VALUE.

        /*
        // Goedel encoding may be a bit of overkill, but at least it's 
        // guaranteed to be unique (well, modulo integer wrap-around) :-)
        // Negative coordinates will collide with positive ones, though.
        return (new java.lang.Double(
                    java.lang.Math.pow(2, java.lang.Math.abs(x)) 
                    * java.lang.Math.pow(3, java.lang.Math.abs(y))
                    * java.lang.Math.pow(5, java.lang.Math.abs(z)))).intValue();
        */
    }

    /**
     * Returns a string representation for this coordinate object
     *
     * @result
     *   | result.equals("(" + x + ", " + y + ", " + z + ")")
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

