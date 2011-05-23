package rpg.util;

import be.kuleuven.cs.som.annotate.*;
import java.util.Map;
import java.util.EnumMap;

/**
 * A class representing a coordinate system with a lower and an upper 
 * coordinate bound.
 *
 * @invar
 * Each coordinate system can have its lower coordinate bound as its lower 
 * coordinate bound.
 *   | canHaveAsLowerBound(getLowerBound())
 * @invar
 * Each coordinate system can have its upper coordinate bound as its upper 
 * coordinate bound.
 *   | canHaveAsUpperBound(getUpperBound())
 *
 * @author Roald Frederickx
 */
public class CoordinateSystem implements Cloneable {

    /** 
     * Create a new coordinate system with the given lower and upper 
     * coordinate bounds.
     * 
     * @param lowerBound 
     * The lower coordinate bound for this new coordinate system.
     * @param upperBound 
     * The upper coordinate bound for this new coordinate system.
     * @post
     *   | new.getLowerBound().equals(lowerBound)
     * @post
     *   | new.getUpperBound().equals(upperBound)
     * @throws IllegalArgumentException
     *   | !isPossibleLowerBound(lowerBound) 
     *   |      || !isPossibleUpperBound(upperBound)
     *   |      || !matchesLowerUpperBound(lowerBound, upperBound)
     */
    public CoordinateSystem(Coordinate lowerBound, Coordinate upperBound) 
                                            throws IllegalArgumentException {
        setLowerBoundRaw(lowerBound);
        setUpperBound(upperBound);
    }

    /**
     * Return the lower coordinate bound for this coordinate system.
     */
    @Basic @Raw
    public Coordinate getLowerBound() {
        return lowerBound;
    }
    
    /**
     * Set the lower coordinate bound for this coordinate system to the 
     * given lower coordinate bound.
     *
     * @param lowerBound
     * The new lower coordinate bound for this coordinate system.
     * @post
     * The new lower coordinate bound for this coordinate system is equal 
     * to the given lower coordinate bound.
     *   | new.getLowerBound() == lowerBound
     * @throws IllegalArgumentException
     * This coordinate system cannot have the given lower coordinate bound 
     * as its lower coordinate bound.
     *   | ! canHaveAsLowerBound(lowerBound)
     */
    @Raw
    public void setLowerBound(Coordinate lowerBound) 
                                        throws IllegalArgumentException {
        if (!canHaveAsLowerBound(lowerBound))
            throw new IllegalArgumentException();
        setLowerBoundRaw(lowerBound);
    }

    /**
     * Set the lower coordinate bound for this coordinate system to the 
     * given lower coordinate bound, no matter what.
     *
     * @param lowerBound
     * The new lower coordinate bound for this coordinate system.
     * @post
     * The new lower coordinate bound for this coordinate system is equal 
     * to the given lower coordinate bound.
     *   | new.getLowerBound() == lowerBound
     */
    @Raw
    public void setLowerBoundRaw(Coordinate lowerBound) {
        this.lowerBound = lowerBound;
    }

    
    /**
     * Checks whether this coordinate system can have the given lower 
     * coordinate bound as its lower coordinate bound.
     *
     * @param lowerBound
     * The lower coordinate bound to check.
     * @return
     *
     * True if and only if the given lower coordinate bound is a possible 
     * lower coordinate bound for any coordinate system, and if the given 
     * lower coordinate bound matches with the upper coordinate bound of 
     * this coordinate system. If the old lower coordinate bound is not 
     * null, then the given lower coordinate bound must be less strict.
     *   | result ==
     *   |      (isPossibleLowerBound(lowerBound)
     *   |          &amp;&amp; (getLowerBound() == null
     *   |              || Coordinate.formsValidBoundingBox(lowerBound
     *   |                                                  getLowerBound())
     *   |          &amp;&amp; matchesLowerUpperBound(lowerBound, 
     *   |                                          getUpperBound()))
     */
    @Raw
    public boolean canHaveAsLowerBound(Coordinate lowerBound) {
        return isPossibleLowerBound(lowerBound)
                && (getLowerBound() == null 
                        || Coordinate.formsValidBoundingBox(lowerBound, 
                                                            getLowerBound()))
                && matchesLowerUpperBound(lowerBound, getUpperBound());
    }
    
    /**
     * Checks whether the given lower coordinate bound is a possible lower 
     * coordinate bound for any coordinate system.
     *
     * @param lowerBound
     * The lower coordinate bound to check.
     * @return
     * True if and only if the given lower coordinate bound is not null
     *   | result == (lowerBound != null)
     */
    public static boolean isPossibleLowerBound(Coordinate lowerBound) {
        return lowerBound != null;
    }
    
    /**
     * Variable registering the lower coordinate bound for this coordinate 
     * system.
     */
    private Coordinate lowerBound;


    /**
     * Return the upper coordinate bound for this coordinate system.
     */
    @Basic @Raw
    public Coordinate getUpperBound() {
        return upperBound;
    }
    
    /**
     * Set the upper coordinate bound for this coordinate system to the 
     * given upper coordinate bound.
     *
     * @param upperBound
     * The new upper coordinate bound for this coordinate system.
     * @post
     * The new upper coordinate bound for this coordinate system is equal 
     * to the given upper coordinate bound.
     *   | new.getUpperBound() == upperBound
     * @throws IllegalArgumentException
     * This coordinate system cannot have the given upper coordinate bound 
     * as its upper coordinate bound.
     *   | ! canHaveAsUpperBound(upperBound)
     */
    @Raw
    public void setUpperBound(Coordinate upperBound)
                                            throws IllegalArgumentException {
        if (!canHaveAsUpperBound(upperBound))
            throw new IllegalArgumentException();
        setUpperBoundRaw(upperBound);
    }

    /**
     * Set the upper coordinate bound for this coordinate system to the 
     * given upper coordinate bound, no matter what.
     *
     * @param upperBound
     * The new upper coordinate bound for this coordinate system.
     * @post
     * The new upper coordinate bound for this coordinate system is equal 
     * to the given upper coordinate bound.
     *   | new.getUpperBound() == upperBound
     */
    @Raw
    public void setUpperBoundRaw(Coordinate upperBound) {
        this.upperBound = upperBound;
    }
    
    /**
     * Checks whether this coordinate system can have the given upper 
     * coordinate bound as its upper coordinate bound.
     *
     * @param upperBound
     * The upper coordinate bound to check.
     * @return
     * True iff the given upper coordinate bound is a possible 
     * upper coordinate bound for any coordinate system, and if the given 
     * upper coordinate bound matches with the lower coordinate bound of 
     * this coordinate system. If the old upper coordinate bound is not 
     * null, then the given upper coordinate bound must be less strict.
     *   | result ==
     *   |      (isPossibleUpperBound(upperBound)
     *   |          &amp;&amp; (getUpperBound() == null
     *   |              || Coordinate.formsValidBoundingBox(getUpperBound()
     *   |                                                      upperbound)
     *   |          &amp;&amp; matchesLowerUpperBound(getLowerBound(), 
     *   |                                              upperBound))
     */
    @Raw
    public boolean canHaveAsUpperBound(Coordinate upperBound) {
        return isPossibleUpperBound(upperBound)
                && (getUpperBound() == null 
                        || Coordinate.formsValidBoundingBox(getUpperBound(),
                                                                upperBound))
                && matchesLowerUpperBound(getLowerBound(), upperBound);
    }
    
    /**
     * Checks whether the given upper coordinate bound is a possible upper 
     * coordinate bound for any coordinate system.
     *
     * @param upperBound
     * The upper coordinate bound to check.
     * @return
     * True if and only if the given upper coordinate bound is not null.
     *   | result == (upperBound != null)
     */
    public static boolean isPossibleUpperBound(Coordinate upperBound) {
        return upperBound != null;
    }
    
    /**
     * Variable registering the upper coordinate bound for this coordinate 
     * system.
     */
    private Coordinate upperBound;


    /** Check whether the given lower coordinate bound matches the given 
     * upper coordinate bound.
     * 
     * @param lowerBound
     * The lower coordinate bound to check.
     * @param upperBound
     * The upper coordinate bound to check.
     * @return True iff the given bounds form a valid bounding box.
     *   | result == Coordinate.formsValidBoundingBox(lowerBound, 
     *   upperBound)
     */
    public static boolean matchesLowerUpperBound(Coordinate lowerBound,
                                                Coordinate upperBound) {
        return Coordinate.formsValidBoundingBox(lowerBound, upperBound);
    }


    /** 
     * Checks whether this coordinate system contains the given coordinate.
     *
     * @param coordinate 
     * The coordinate to check.
     * @return 
     *   | result == (coordinate != null
     *   |      &amp;&amp; coordinate.isBoundedBy(getLowerBound(),
     *   |                                          getUpperBound()))
     */
    public boolean contains(Coordinate coordinate) {
        if (coordinate == null)
            return false;
        return coordinate.isBoundedBy(getLowerBound(), getUpperBound());
    }

    /** 
     * Returns an iterable of the neighbours of the given coordinate in 
     * this coordinate system.
     * 
     * @param coordinate 
     * The coordinate whose neighbours to generate.
     * @return
     * An iterable of the neighbours of the given coordinate in this 
     * coordinate system.
     * @throws IllegalArgumentException
     *   | coordinate == null
     */
    public Map<Direction, Coordinate> neighboursOf(Coordinate coordinate) 
                                            throws IllegalArgumentException {
        if (coordinate == null)
            throw new IllegalArgumentException();

        EnumMap<Direction, Coordinate> result =
                        new EnumMap<Direction, Coordinate>(Direction.class);
        for (Direction direction : Direction.values()) {
            Coordinate newCoord = coordinate.moveTo(direction);
            if (contains(newCoord))
                result.put(direction, newCoord);
        }
        return result;
    }

    /** 
     * Translate this coordinate system over the given offset.
     * 
     * @param offset 
     * The offset over which to translate this coordinate system.
     * @post
     *   | new.getLowerBound().equals(old.getLowerBound().add(offset))
     * @post
     *   | new.getUpperBound().equals(old.getUpperBound().add(offset))
     * @throws IllegalArgumentException
     *   | offset == null
     */
    public void translate(Coordinate offset) throws IllegalArgumentException {
        if (offset == null)
            throw new IllegalArgumentException();
        setLowerBoundRaw(getLowerBound().add(offset));
        setUpperBoundRaw(getUpperBound().add(offset));
    }

    /** 
     * Check whether this coordinate system fully contains the other 
     * coordinate system.
     * 
     * @param other 
     * The other coordinate system to check.
     * @pre
     *   | other != null
     * @return 
     *   | result == other.canHaveAsLowerBound(getLowerBound())
     *   |           &amp;&amp; other.canHaveAsUpperBound(getUpperBound());
     */
    public boolean contains(CoordinateSystem other) {
        assert other != null;
        return other.canHaveAsLowerBound(getLowerBound())
            && other.canHaveAsUpperBound(getUpperBound());
    }

    /** 
     * Checks wheter this coordinate system overlaps with the given 
     * coordinate system.
     * 
     * @param other 
     * The other coordinate system to check.
     * @pre
     *   | other != null;
     * @return 
     *   | result == (contains(other.getLowerBound())
     *   |         || contains(other.getUpperBound())
     *   |         || other.contains(getLowerBound())
     *   |         || other.contains(getUpperBound()))
     */
    public boolean overlaps(CoordinateSystem other) {
        assert other != null;
        return contains(other.getLowerBound()) || contains(other.getUpperBound())
            || other.contains(getLowerBound()) || other.contains(getUpperBound());
    }

    /** 
     * Check for equality between this coordinate system and a given object.
     *
     * @return
     * True iff the given object is an effective coordinate system with 
     * lower and upper bounds equal to the lower and upper bound of this 
     * coordinate system.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;
        CoordinateSystem otherCoordSyst = (CoordinateSystem) other;
        return this.getLowerBound().equals(otherCoordSyst.getLowerBound())
                && this.getUpperBound().equals(otherCoordSyst.getUpperBound());
    }

    @Override
    public int hashCode() {
        return getLowerBound().hashCode() ^ getUpperBound().hashCode();
    }

    /** 
     * Clone this coordinate system.
     * 
     * @return 
     *   | result == (CoordinateSystem) super.clone()
     */
    public CoordinateSystem clone() {
        try {
            return (CoordinateSystem) super.clone();
        } catch (CloneNotSupportedException e) {
            assert false;
            return null;
        }
    }

    /**
     * Return a string representation this coordinate system.
     */
    @Override
    public String toString() {
        return getLowerBound().toString() + "--" + getUpperBound().toString();
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

