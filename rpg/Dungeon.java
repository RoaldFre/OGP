package rpg;

import rpg.exceptions.*;
import rpg.util.*;

import be.kuleuven.cs.som.annotate.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

/**
 * A class representing a dungeon of squares.
 *
 * @invar
 *   | squaresSatisfyConstraints()
 * @invar
 *   | canHaveAsCoordSyst(getCoordSyst())
 * @invar
 *   | getSquareMapping() != null
 * @invar
 *   | canHaveAsParentDungeon(getParentDungeon())
 * @invar
 *   | squaresBorderProperlyOnTheirNeighbours()
 *
 * @author Roald Frederickx
 */
public abstract class Dungeon<S extends Square> {

    /** 
     * Create a new empty dungeon with the given coordinate system as its 
     * coordinate system. 
     * 
     * @param coordinateSystem
     * The coordinate system of this new dungeon.
     * @effect
     *   | setCoordSyst(coordinateSystem)
     * @effect
     *   | setParentDungeon(null)
     * @post
     *   | new.getSquareMapping() != null
     *   |      &amp;&amp; new.getSquareMapping().isEmpty()
     * @post
     *   | !new.isTerminated()
     */
    @Raw @Model
    protected Dungeon(CoordinateSystem coordinateSystem)
                                    throws IllegalArgumentException {
        setCoordSyst(coordinateSystem);
        setParentDungeon(null);
    }

    /**
     * Return the coordinate system for this dungeon.
     */
    @Basic @Raw
    public CoordinateSystem getCoordSyst() {
        if (getCoordSystRaw() == null)
            return null;
        return getCoordSystRaw().clone();
    }

    /**
     * Checks whether the given coordinate system is a valid coordinate 
     * system for this dungeon.
     *
     * @param coordSyst
     * The coordinate system to check.
     * @return
     *   | if !canPossiblyHaveAsCoordSyst(coordSyst)
     *   |      then result == false
     */
    @Raw
    public abstract boolean canHaveAsCoordSyst(CoordinateSystem coordSyst);

    /**
     * Checks whether the given coordinate system is a possible valid 
     * coordinate system for this dungeon.
     *
     * @param coordSyst
     * The coordinate system to check.
     * @return
     *   | result == (coordSyst != null
     *   |              &amp;&amp; (getCoordSyst() == null  
     *   |                          || coordSyst.contains(getCoordSyst()))
     *   |              &amp;&amp; (!hasParentDungeon()
     *   |                          || getParentDungeon().canExpandSubDungeonTo(
     *   |                                                   this, coordSyst)))
     */
    @Raw
    public boolean canPossiblyHaveAsCoordSyst(CoordinateSystem coordSyst) {
        if (coordSyst == null)
            return false;
        if (getCoordSystRaw() != null  
                && !coordSyst.contains(getCoordSystRaw()))
            return false;
        if (!hasParentDungeon())
            return true;
        return getParentDungeon().canExpandSubDungeonTo(this, coordSyst);
    }

    /** 
     * Checks whether this dungeon overlaps with the given coordinate 
     * system.
     * 
     * @param coordSyst 
     * The coordinate system to check.
     * @pre
     *   | coordSyst != null
     * @return 
     *   | result == getCoordSyst().overlaps(coordSyst)
     */
    public boolean overlaps(CoordinateSystem coordSyst) {
        assert coordSyst != null;
        return getCoordSystRaw().overlaps(coordSyst);
    }

    /** 
     * Checks whether this dungeon overlaps with the given dungeon.
     * 
     * @param other 
     * The other dungeon to check.
     * @pre
     *   | other != null
     * @return 
     *   | result == overlaps(other.getCoordSyst())
     */
    public boolean overlaps(Dungeon<?> other) {
        assert other != null;
        return overlaps(other.getCoordSystRaw());
    }

    /**
     * Translate the coordinate system of this dungeon.
     *
     * @post
     *   | new.getCoordSyst().equals(old.getCoordSyst().translate(offset))
     * @param offset 
     * The offset over which to translate the coordinate system of this 
     * dungeon.
     */
    protected void translateCoordSyst(Coordinate offset) 
                                        throws IllegalArgumentException {
        CoordinateSystem coordSyst = getCoordSystRaw();
        coordSyst.translate(offset);
        setCoordSystRaw(coordSyst);
    }

    /**
     * Set the coordinate system for this dungeon to the given coordinate 
     * system.
     *
     * @param coordSyst
     * The new coordinate system for this dungeon.
     * @post
     * The new coordinate system for this dungeon is equal to the given 
     * coordinate system.
     *   | new.getCoordSyst().equals(coordSyst)
     * @throws IllegalArgumentException
     * This dungeon cannot have the given coordinate system as its 
     * coordinate system.
     *   | !canHaveAsCoordSyst(coordSyst)
     */
    @Raw @Model
    private void setCoordSyst(CoordinateSystem coordSyst)
                                        throws IllegalArgumentException {
        if (!canHaveAsCoordSyst(coordSyst))
            throw new IllegalArgumentException();
        setCoordSystRaw(coordSyst.clone());
    }

    /**
     * Return the raw coordinate system for this dungeon.
     *
     * @note
     * Never expose this result to the user, use getCoordSyst() instead.
     * @note
     * After making changes to the returned result, you still *have* to 
     * commit them with setCoordSystRaw().
     */
    @Basic @Raw
    private CoordinateSystem getCoordSystRaw() {
        return coordSyst;
    }

    /**
     * Set the raw coordinate system for this dungeon.
     */
    @Basic @Raw
    private void setCoordSystRaw(CoordinateSystem coordSyst) {
        this.coordSyst = coordSyst;
    }

    /** 
     * Variable referencing the coordinate system that belongs to this 
     * dungeon.
     */
    private CoordinateSystem coordSyst;

    /** 
     * Translate this dungeon over the given offset.
     * This only affects the mapping of coordinates to squares. It will not 
     * affect the squares of this dungeon itself, nor wil it affect their 
     * borderes.
     * 
     * @param offset 
     * The offset over which to translate this dungeon.
     * @effect
     *   | translateCoordSyst(offset)
     * @throws CoordinateConstraintsException
     * Translating this dungeon would cause a violation of the constraints 
     * on the coordinates of squares in dungeons, as enforced by
     * canHaveAsSquareAt().
     */
    abstract protected void translate(Coordinate offset)
        throws IllegalArgumentException, CoordinateConstraintsException;

    /** 
     * Check whether the given coordinate lies within this dungeon.
     */
    abstract public boolean containsCoordinate(Coordinate coordinate);

    /** 
     * Return a mapping of directions to squares that represent all 
     * neighbouring squares of the given coordinate in this dungeon. 
     * 
     * @param coordinate 
     * The coordinate whose neighbours to return.
     * @return
     * A mapping of directions to squares that represent all neighbouring 
     * squares of the given coordinate in this dungeon. 
     *   | for each e in result.entrySet() :
     *   |    e.getValue().equals(getSquareAt(coordinate.moveTo(e.getKey())))
     * @throws IllegalArgumentException
     *   | coordinate == null
     */
    @Raw
    abstract public Map<Direction, S> getDirectionsAndNeighboursOf(
                                            Coordinate coordinate)
                                            throws IllegalArgumentException;

    /** 
     * Returns the square at the given coordinate in this dungeon.
     * 
     * @param coordinate 
     * The coordinate of the square to return.
     * @throws IllegalArgumentException
     *   | !isEffectiveCoordinate(coordinate)
     * @throws CoordinateNotOccupiedException
     *   | !isOccupied(coordinate)
     */
    @Raw
    abstract public S getSquareAt(Coordinate coordinate) 
            throws IllegalArgumentException, CoordinateNotOccupiedException;

    /** 
     * Returns whether or not this dungeon contains the given square.
     * 
     * @param square 
     * The square to check.
     */
    @Raw
    abstract public boolean hasSquare(Square square);

    /** 
     * Check if the given square at the given coordinate borders properly 
     * on all the squares that are its neighbours, as given by the root 
     * dungeon.
     * 
     * @param square 
     * The square to check.
     * @param coordinate 
     * The coordinate of the square to check.
     * @pre
     *   | getSquareAt(coordinate).equals(square)
     * @return 
     *   | result ==
     *   |  (for each dn in 
     *   |           getRootDungeon().getDirectionsAndNeighboursOf(
     *   |                                         coordinate).entrySet() :
     *   |     square.getBorderAt(dn.getKey()).bordersOnSquare(dn.getValue()))
     */
    @Raw @Model
    protected boolean squareBordersProperlyOnItsNeighbours(S square,
                                                    Coordinate coordinate) {
        assert getSquareAt(coordinate).equals(square);
        Dungeon<? super S> root = getRootDungeon();
        for (Map.Entry<Direction, ? super S> neighbourEntry :
                root.getDirectionsAndNeighboursOf(coordinate).entrySet()) {
            Square neighbour = (Square) neighbourEntry.getValue();
            Direction direction = neighbourEntry.getKey();
            if (!square.getBorderAt(direction).bordersOnSquare(neighbour))
                return false;
        }
        return true;
    }

    /** 
     * Checks whether this dungeon has squares that properly border on 
     * their neighbouring squares.
     * 
     * @return
     * True iff every square of this dungeon borders properly on all its 
     * neighbours.
     *   | result == 
     *   |  (for each ps in getPositionsAndSquares() :
     *   |          squareBordersProperlyOnItsNeighbours(ps.getValue(),
     *   |                                               ps.getKey()))
     */
    @Raw
    public boolean squaresBorderProperlyOnTheirNeighbours()
                                            throws IllegalStateException {
        for (Map.Entry<Coordinate, S> ps : getPositionsAndSquares())
            if (!squareBordersProperlyOnItsNeighbours(ps.getValue(),
                                                      ps.getKey()))
                return false;
        return true;
    }

    /**
     * Merge the given square at the given coordinate with its neighbours 
     * in the dungeon complex of this dungeon, as given by the root 
     * dungeon.
     *
     * @param square 
     * The square to merge.
     * @param coordinate 
     * The coordinate of the square to merge.
     * @effect
     *   | for each dn in 
     *   |           getRootDungeon().getDirectionsAndNeighboursOf(
     *   |                                         coordinate).entrySet() :
     *   |     square.mergeWith(dn.getValue(), dn.getKey())
     * @post
     *   | squareBordersProperlyOnItsNeighbours(square, coordinate)
     */
    @Raw @Model
    protected void mergeSquareWithNeighbours(Square square,
                                                    Coordinate coordinate) {
        Dungeon<? super S> root = getRootDungeon();
        for (Map.Entry<Direction, ? super S> neighbourEntry :
                root.getDirectionsAndNeighboursOf(coordinate).entrySet()) {
            Square neighbour = (Square) neighbourEntry.getValue();
            Direction neighbourDirection = neighbourEntry.getKey();
            square.mergeWith(neighbour, neighbourDirection);
        }
    }

    /** 
     * Deletes the square at the given coordinate and terminates it.
     *
     * @param coordinate 
     * The coordinate to remove the square at.
     * @post
     *   | !new.isOccupied(coordinate)
     * @post
     *   | !new.hasSquare(old.getSquareAt(coordinate))
     * @post
     *   | old.getSquareAt(coordinate).isTerminated()
     * @throws CoordinateNotOccupiedException
     *   | !isOccupied(coordinate)
     * @throws IllegalArgumentException
     *   | !isEffectiveCoordinate(coordinate)
     */
    abstract public void deleteSquareAt(Coordinate coordinate) 
        throws IllegalArgumentException, CoordinateNotOccupiedException; 

    /** 
     * Returns whether or not the given coordinate is occupied in this 
     * dungeon.
     * 
     * @param coordinate 
     * The coordinate to check.
     * @throws IllegalArgumentException
     *   | !isEffectiveCoordinate(coordinate)
     */
    abstract public boolean isOccupied(Coordinate coordinate) 
        throws IllegalArgumentException;
    
    /** 
     * Checks whether the squares of this dungeon satisfy the constraints 
     * on squares of a dungeon.
     * 
     * @return
     * True iff not more than 20% of the squares of this dungeon have a 
     * slippery floor.
     *   |  result == (getNbIntrinsicallySlipperySquares() * 5 &lt;= getNbSquares())
     */
    public boolean squaresSatisfyConstraints() {
        return (getNbIntrinsicallySlipperySquares() * 5 <= getNbSquares());
    }

    /** 
     * Return the number of squares in this dungeon.
     *
     * @return
     *   | if (getSquares() == null)
     *   |      then result == null
     *   |      else result == {square in getSquares() | true : true}.size()
     */
    @Raw
    abstract public int getNbSquares();

    /** 
     * Return the number of squares in this dungeon that have a slippery 
     * floor.
     *
     * @return
     * The number of squares in this dungeon that have a slippery 
     * floor.
     *   | ({ square in getSquares() | true :
     *   |                  square.hasSlipperyFloor() }.size() == result)
     */
    public int getNbIntrinsicallySlipperySquares() {
        int nbSlipperySquares = 0;
        for (S square : getSquares())
            if (square.hasSlipperyFloor())
                nbSlipperySquares++;
        return nbSlipperySquares;
    }

    /**
     * Return a mapping of coordinates to squares of this dungeon.
     */
    @Basic @Raw
    public Map<Coordinate, S> getSquareMapping() {
        Map<Coordinate, S> result = new HashMap<Coordinate, S>();
        try {
            addSquareMappingTo(result);
        } catch (IllegalStateException e) {
            return null;
        }
        return result;
    }

    /** 
     * Add the mapping of coordinates to squares of this dungeon to the 
     * given map.
     * 
     * @param map 
     * The map of coordinates to squares to add the mapping of coordinates 
     * to squares of this dungeon to.
     * @throws IllegalStateException
     * This dungeon is not in a valid state for this operation.
     */
    @Raw
    abstract protected void addSquareMappingTo(Map<Coordinate, ? super S> map)
                                                throws IllegalStateException;

    /**
     * Return a set of the squares and their positions in this dungeon.
     *
     * @return
     *   | result == getSquareMapping().entrySet()
     * @throws IllegalStateException
     *   | getSquareMapping() == null
     */
    @Raw
    public Set<Map.Entry<Coordinate,S>> getPositionsAndSquares() 
                                                throws IllegalStateException {
        if (getSquareMapping() == null)
            throw new IllegalStateException();
        return getSquareMapping().entrySet();
    }

    /**
     * Return an iterable of the squares in this dungeon that satisfy the 
     * condition as imposed by the given filter.
     *
     * @param squareFilter
     * The filter used to select which squares of this dungeon to return.
     * @return
     * An iterable that has getSquareIterator(squareFilter) as its 
     * iterator.
     */
    public Iterable<S> getFilteredSquares(final SquareFilter squareFilter) {
        return new Iterable<S>() {
            public Iterator<S> iterator() {
                return getFilteredSquareIterator(squareFilter);
            }
        };
    }

    /**
     * Return an iterable of the squares in this dungeon.
     *
     * @return
     *   result == getFilteredSquares(acceptAllSquaresFilter)
     */
    public Iterable<S> getSquares() {
        return getFilteredSquares(acceptAllSquaresFilter);
    }

    /** 
     * Return an iterable of all rocks in this dungeon that have a 
     * temperature greater than 200C.
     * 
     * @return 
     * An iterable of all rocks in this dungeon that have a temperature 
     * greater than 200C.
     */
    public Iterable<S> getHotRocks() {
        return getFilteredSquares(
            new SquareFilter() {
                public boolean filter(LeafDungeon<? extends Square> d, Square s) {
                    return (s instanceof Rock)
                        && (s.getTemperature().compareTo(
                                                new Temperature(200)) >= 0);
                }
            }
        );
    }

    /**
     * Return an iterator of the squares in this dungeon that satisfy the 
     * conditions as imposed by the given filter.
     *
     * @param squareFilter
     * The filter used to select which squares of this dungeon to return.
     * @return
     * An iterator over the elements of getSquareMapping().values() that 
     * satisfy the given square filter.
     */
    abstract public Iterator<S> getFilteredSquareIterator(
                                            SquareFilter squareFilter);

    /** 
     * An interface that specifies a filter on squares of a dungeon.
     */
    public static interface SquareFilter {
        /** 
         * Check whether or not the given square from the given dungeon 
         * satisfies the conditions imposed by this square filter.
         * 
         * @param dungeon
         * The dungeon of the given square. This is equal to Dungeon.this.
         * @param square
         * The square to check.
         * @pre
         *   | dungeon == Dungeon.this
         * @pre
         *   | dungeon.hasSquare(square)
         */
        @Basic
        public boolean filter(LeafDungeon<? extends Square> dungeon, Square square);
    }

    /**
     * Constant referencing a square filter that accepts all squares.
     */
    public static final SquareFilter acceptAllSquaresFilter =
            new SquareFilter() {
                public boolean filter(LeafDungeon<? extends Square> d, Square s) {
                    return true;
                }
            };

    /**
     * Return the parent dungeon for this dungeon.
     */
    @Basic @Raw
    public CompositeDungeon<? super S> getParentDungeon() {
        return parentDungeon;
    }

    /**
     * Set the parent dungeon for this dungeon to the given parent dungeon.
     *
     * @param parentDungeon
     * The new parent dungeon for this dungeon.
     * @post
     * The new parent dungeon for this dungeon is equal to the given parent 
     * dungeon.
     *   | new.getParentDungeon().equals(parentDungeon)
     * @throws IllegalArgumentException
     * This dungeon cannot have the given parent dungeon as its parent dungeon.
     *   | ! canHaveAsParentDungeon(parentDungeon)
     */
    @Raw @Model
    protected void setParentDungeon(
                            @Raw CompositeDungeon<? super S> parentDungeon)
                                            throws IllegalArgumentException {
        if (!canHaveAsParentDungeon(parentDungeon))
            throw new IllegalArgumentException();
        this.parentDungeon = parentDungeon;
    }
    
    /**
     * Checks whether this dungeon can have the given parent dungeon as its 
     * parent dungeon.
     *
     * @param parentDungeon
     * The parent dungeon to check.
     * @return
     *   | if (isTerminated())
     *   |      then result == (parentDungeon == null)
     *   | else if (parentDungeon == null)
     *   |      then result == (!hasParentDungeon()
     *   |                    || !old.getParentDungeon.hasAsSubDungeon(this))
     *   | else if (parentDungeon.isTerminated())
     *   |      then result == false
     *   | else result == parentDungeon.hasAsSubDungeon(this)
     */
    @Raw
    public boolean canHaveAsParentDungeon(
                                @Raw CompositeDungeon<? super S> parentDungeon) {
        if (isTerminated())
            return parentDungeon == null;
        if (parentDungeon == null)
            return !hasParentDungeon()
                    || !getParentDungeon().hasAsSubDungeon(this);
        if (parentDungeon.isTerminated())
            return false;
        return parentDungeon.hasAsSubDungeon(this);
    }

    /** 
     * Checks whether this dungeon has a parent dungeon.
     * 
     * @return 
     *   | result == (getParentDungeon() != null)
     */
    public boolean hasParentDungeon() {
        return getParentDungeon() != null;
    }

    /**
     * Variable registering the parent dungeon for this dungeon.
     */
    private CompositeDungeon<? super S> parentDungeon;

    /** 
     * Returns the root composite dungeon of this dungeon. 
     * 
     * @return 
     * Null if this dungeon has no parents, or the root composite dungen 
     * otherwise.
     */
    public CompositeDungeon<? super S> getRootCompositeDungeon() {
        CompositeDungeon<? super S> parent = getParentDungeon();
        if (parent == null)
            return null;
        while (parent.hasParentDungeon())
            parent = parent.getParentDungeon();
        return parent;
    }

    /** 
     * Returns the root dungeon of this dungeon. 
     * 
     * @return 
     *   | if getRootCompositeDungeon() != null
     *   |      then result == getRootCompositeDungeon()
     *   |      else result == this
     */
    public Dungeon<? super S> getRootDungeon() {
        Dungeon<? super S> rootComposite = getRootCompositeDungeon();
        if (rootComposite != null)
            return rootComposite;
        return this;
    }

    /**
     * Return a set of all containing leaf dungeons (this dungeon included, 
     * if applicable).
     *
     * @return
     * A set that holds all containing leaf dungeons.
     * @return
     *   | result != null
     */
    abstract public Set<LeafDungeon<? extends S>> getContainingLeafDungeons();

    /** 
     * Checks whether or not the given coordinate is an effective square 
     * coordinate.
     * 
     * @param coordinate
     * The coordinate to check.
     * @return
     *   | result == (coordinate != null)
     */
    public static boolean isEffectiveCoordinate(Coordinate coordinate) {
        return coordinate != null;
    }

    /**
     * Return the termination status for this dungeon.
     */
    @Basic @Raw
    public boolean isTerminated() {
        return isTerminated;
    }

    /** 
     * Terminate this dungeon.
     *
     * @post
     * This new dungeon is terminated.
     *   | new.isTerminated()
     * @effect
     *   | if (hasParentDungeon())
     *   |      then getParentDungeon().deleteSubDungeon(this)
     * @effect
     *   | for each coordinate in getSquareMapping().keySet() :
     *   |      deleteSquareAt(coordinate)
     */
    abstract public void terminate();

    /** 
     * Set the termination status of this dungeon to true.
     * 
     * @post
     *   | isTerminated()
     */
    @Raw
    protected void setIsTerminated() {
        isTerminated = true;
    }

    /**
     * Variable registering the termination status for this dungeon.
     */
    private boolean isTerminated = false;

    /**
     * Check whether this dungeon is not raw.
     *
     * @return
     * True iff this dungeon satisfies all its class invariants.
     */
    @Raw
    public boolean isNotRaw() {
        return true
                && squaresSatisfyConstraints()
                && canHaveAsCoordSyst(getCoordSyst())
                && getSquareMapping() != null
                && canHaveAsParentDungeon(getParentDungeon())
                && squaresBorderProperlyOnTheirNeighbours();
    }
} 

// vim: ts=4:sw=4:expandtab:smarttab

