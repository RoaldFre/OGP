package rpg;

import rpg.exceptions.*;
import rpg.util.*;

import be.kuleuven.cs.som.annotate.*;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
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
     *   | getSquareMapping() != null
     *   |      &amp;&amp; getSquareMapping().isEmpty()
     * @post
     *   | !isTerminated()
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
        if (coordSyst == null)
            return null;
        return coordSyst.clone();
    }


    /**
     * Checks whether the given coordinate system is a valid coordinate 
     * system for this dungeon.
     *
     * @param coordSyst
     * The coordinate system to check.
     * @return
     *   | if !canHaveAsPossibleCoordSyst(coordSyst)
     *   |      then result == false
     */
    @Raw
    public boolean canHaveAsCoordSyst(CoordinateSystem coordSyst) {
        return canHaveAsPossibleCoordSyst(coordSyst);
    }

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
     *   |              &amp;&amp; (getParentDungeon() == null
     *   |                          || getParentDungeon().canExpandSubDungeonTo(
     *   |                                                   this, coordSyst)))
     */
    @Raw
    public boolean canHaveAsPossibleCoordSyst(CoordinateSystem coordSyst) {
        if (coordSyst == null)
            return false;
        if (this.coordSyst != null  
                && !coordSyst.contains(this.coordSyst))
            return false;
        if (getParentDungeon() == null)
            return true;
        return getParentDungeon().canExpandSubDungeonTo(this, coordSyst);
    }

    /** 
     * Checks wheter this dungeon overlaps with the given coordinate 
     * system.
     * 
     * @param coordSyst 
     * The coordinate system to check.
     * @pre
     *   | coordSyst != null
     * @return 
     *   | result == getCoordSyst().overlaps(other.getCoordSyst())
     */
    public boolean overlaps(CoordinateSystem coordSyst) {
        assert coordSyst != null;
        return this.coordSyst.overlaps(coordSyst);
    }

    /** 
     * Checks wheter this dungeon overlaps with the given dungeon.
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
        return overlaps(other.coordSyst);
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
        this.coordSyst = coordSyst;
    }

    /**
     * Translate the coordinate system of this dungeon.
     *
     * @effect
     *   | setCoordSyst(getCoordSyst.translate(offset))
     * @param offset 
     * The offset over which to translate the coordinate system of this 
     * dungeon.
     */
    protected void translateCoordSyst(Coordinate offset) 
                                        throws IllegalArgumentException {
        coordSyst.translate(offset);
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
     *   |      e.getValue() == getSquareAt(coordinate.moveTo(e.getKey()))
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
     * Returns wheter or not this dungeon contains the given square.
     * 
     * @param square 
     * The square to check.
     */
    @Raw
    abstract public boolean hasSquare(Square square);

    /** 
     * Deletes the square at the given coordinate and terminates it.
     *
     * @param coordinate 
     * The coordinate to remove the square at.
     * @post
     *   | !isOccupied(coordinate)
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
     * Return an iterable of the squares and their position in this 
     * dungeon.
     *
     * @return
     * An iterable over the elements of getSquareMapping().entrySet().
     */
    @Raw
    abstract public Iterable<Map.Entry<Coordinate,S>> getPositionsAndSquares()
                                                throws IllegalStateException;

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
     * Return an iterator of the squares in this dungeon.
     *
     * @return
     *   | result == getFilteredSquareIterator(acceptAllSquaresFilter)
     */
    public Iterator<S> getSquareIterator() {
        return getFilteredSquareIterator(acceptAllSquaresFilter);
    }

    public interface SquareFilter {
        public boolean filter(LeafDungeon<? extends Square> dungeon, Square square);
    }

    public final SquareFilter acceptAllSquaresFilter =
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
     *   | new.getParentDungeon() == parentDungeon
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
     *   |      then result == (old.getParentDungeon() == null
     *   |                    || !old.getParentDungeon.hasAsSubDungeon(this))
     *   | else if (parentDungeon.isTerminated())
     *   |      then result == false
     *   | else result == parentDungeon.hasAsSubDungeon(this)
     */
    @Raw
    public boolean canHaveAsParentDungeon(
                                    @Raw CompositeDungeon<?> parentDungeon) {
        if (isTerminated())
            return parentDungeon == null;
        if (parentDungeon == null)
            return getParentDungeon() == null
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
            parent = getParentDungeon();
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
     * Variable registering the parent dungeon for this dungeon.
     */
    private CompositeDungeon<? super S> parentDungeon;




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
     * This dungeon is terminated.
     *   | new.isTerminated()
     */
    @Model
    protected void terminate(){
        isTerminated = true;
        //getRootDungeon().deleteDungeon(this); //DIT ROEPT MSS ZELF AL TERMINATE AAN? -> in that case, gewoon een assert zetten dat !containsSubDungeon(this)
        setParentDungeon(null);
    }

    /**
     * Variable registering the termination status for this dungeon.
     */
    private boolean isTerminated = false;














    /** 
     * Checks whether the given destination coordinate can be reached, 
     * starting from the given source coordinate and only moving through 
     * open borders.
     *
     * The implementation does a depth-first traversal of the neighbouring 
     * squares of the source coordinate on a spanning tree of the graph of 
     * 'openly' (through open borders) connected squares (traversed in the 
     * order as iterated by Direction.values()).
     *
     * Its average time complexity is linear in the number of squares that 
     * are openly connected to the source square. Hence, in general this is 
     * linear in the number of squares in this dungeon.
     * 
     * Note that the <i>worst</i> case time complexity is superlinear 
     * (probably quadratic, depending on the implementation of java's 
     * HashSet). This happens in the pathological case where all 
     * coordinates hash to the same value.
     *
     * Also note that that if this dungeon were to enforce extra 
     * constraints (eg. open areas can be no larger than N squares), the 
     * time complexity would effectively reduce to constant-time. 
     *
     * Finally, note that smarter search strategies can be employed, 
     * where the distance between the coordinates can be used as a 
     * heuristic in, for example, the A* search algorithm. 
     *
     * 
     * @param source
     * The source coordinate to check.
     * @param destination
     * The destination coordinate to check.
     * @return
     * Whether or not the given destination coordinate can be reached from 
     * the given source coordinate, passing only through open borders.
     * @throws IllegalArgumentException
     *   | !isEffectiveCoordinate(source)
     *   |          || !isEffectiveCoordinate(destination)
     */
    public boolean canReach(Coordinate source, Coordinate destination) 
                                            throws IllegalArgumentException {
        if (!isOccupied(source) || !isOccupied(destination))
            return false;
        
        HashSet<Coordinate> visited = new HashSet<Coordinate>();
        return canReach(source, destination, visited);
    }


    /** 
     * Checks whether the given destination coordinate can be reached, 
     * starting from the given source coordinate and only moving through 
     * open borders, when all the coordinates in 'visited' are already 
     * looked at.
     *
     * @param source
     * The source coordinate to check.
     * @param destination
     * The destination coordinate to check.
     * @param visited
     * The coordinates that have already been visited.
     * @pre
     *   | source != null  &amp;&amp;  destination != null
     * @pre
     *   | visited != null
     * @pre
     *   | isOccupied(source) &amp;&amp; isOccupied(destination)
     * @pre
     *   | !visited.contains(destination)
     * @post
     *   | visited.contains(source)
     * @post
     * If this returns false, the set of visited coordinates is extended 
     * with all coordinates that can be reached from the source coordinate 
     * by passing open borders and by not passing any coordinates already 
     * in the set of visited coordinates.
     * @return
     * Whether or not the destination can be reached from the source, not 
     * passing any coordinates that have already been visited.
     */
    private boolean canReach(Coordinate source, Coordinate destination,
                                            HashSet<Coordinate> visited) {
        assert source != null;
        assert destination != null;
        assert visited != null;

        assert isOccupied(source);
        assert isOccupied(destination);

        assert !visited.contains(destination);

        if (source.equals(destination))
            return true;

        if (visited.contains(source))
            return false;

        visited.add(source);
        
        S sourceSquare = getSquareAt(source);

        for (Direction direction :
                            getDirectionsAndNeighboursOf(source).keySet())
            if (sourceSquare.getBorderAt(direction).isOpen()
                                    && canReach(source.moveTo(direction),
                                                        destination, visited))
                    return true;

        return false;
    }











    /** 
     * Checks whether or not the given coordinate is a possible square 
     * coordinate for all dungeons.
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
                && canHaveAsParentDungeon(getParentDungeon());
    }
} 

// vim: ts=4:sw=4:expandtab:smarttab

