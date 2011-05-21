package rpg;

import rpg.exceptions.*;
import be.kuleuven.cs.som.annotate.*;
import java.util.Map;
import java.util.HashSet;

/**
 * A class representing a dungeon of squares.
 *
 * @invar
 *   | canHaveSquaresAtTheirCoordinates()
 * @invar
 *   | squaresSatisfyConstraints()
 * @invar
 *   | hasProperBorderingSquares()
 * @invar
 *   | canHaveAsCoordSyst(getCoordSyst())
 * @invar
 *   | getSquareMapping() != null
 * @invar
 *   | canHaveAsRootDungeon(getRootDungeon())
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
     *   | setRootDungeon(null)
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
        setRootDungeon(null);
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
     *   |              &amp;&amp; (getRootDungeon() == null
     *   |                          || getRootDungeon().canExpandSubDungeonTo(
     *   |                                                   this, coordSyst)))
     */
    @Raw
    public boolean canHaveAsPossibleCoordSyst(CoordinateSystem coordSyst) {
        if (coordSyst == null)
            return false;
        if (this.coordSyst != null  
                && !coordSyst.contains(this.coordSyst))
            return false;
        if (getRootDungeon() == null)
            return true;
        return getRootDungeon().canExpandSubDungeonTo(this, coordSyst);
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
     * @pre
     *   | canHaveAsCoordSyst(getCoordSyst())
     * @param offset 
     * The offset over which to translate the coordinate system of this 
     * dungeon.
     */
    @Raw
    protected void translateCoordSyst(Coordinate offset) {
        assert canHaveAsCoordSyst(coordSyst);
        coordSyst.translate(offset);
    }

    /** 
     * Variable referencing the coordinate system that belongs to this 
     * dungeon.
     */
    private CoordinateSystem coordSyst;

    /** 
     * Translate this dungeon over the given offset.
     * 
     * @param offset 
     * The offset over which to translate this dungeon.
     * @throws IllegalArgumentException 
     * The given offset is not effective.
     * @throws CoordinateConstraintsException
     * Translating this dungeon would cause a violation of the constraints 
     * on the coordinates of squares in dungeons, as enforced by
     * canHaveAsSquareAt().
     */
    abstract public void translate(Coordinate offset)
        throws IllegalArgumentException, CoordinateConstraintsException;


    /** 
     * Checks whether the given coordinate is a valid coordinate in this 
     * dungeon.
     *
     * @param coordinate 
     * The coordinate to check.
     * @return 
     * True iff the coordinate is a possible square coordinate for all 
     * dungeons, is contained within the coordinate system of this dungeon, 
     * and the coordinate values in all directions are not equal to each 
     * other.
     *   | result == (isPossibleSquareCoordinate(coordinate)
     *   |      &amp;&amp; getCoordSyst().contains(coordinate)
     *   |      &amp;&amp; (coordinate.x != coordinate.y
     *   |                  || coordinate.y != coordinate.z
     *   |                  || coordinate.z != coordinate.x))
     */
    public boolean isValidSquareCoordinate(Coordinate coordinate) {
        if (!isPossibleSquareCoordinate(coordinate))
            return false;
        if (!coordSyst.contains(coordinate))
            return false;
        return coordinate.x != coordinate.y
            || coordinate.y != coordinate.z
            || coordinate.z != coordinate.x;
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
    public static boolean isPossibleSquareCoordinate(Coordinate coordinate) {
        return coordinate != null;
    }

    /** 
     * Add the given square to this dungeon at the given coordinate.
     *
     * @param coordinate 
     * The coordinate to add the given square at.
     * @param square 
     * The square to add at the given coordinate.
     * @post
     *   | new.getSquareAt(coordinate) == square
     * @throws IllegalArgumentException
     *   | !canHaveAsSquareAt(coordinate, square)
     * @throws CoordinateOccupiedException
     *   | isOccupied(coordinate)
     * @throws DungeonConstraintsException
     * Adding the given square at the given coordinate would violate the 
     * constrainst as specified by squaresSatisfyConstraints().
     */
    abstract public void addSquareAt(Coordinate coordinate, S square) 
        throws IllegalArgumentException,
                          CoordinateOccupiedException,
                          DungeonConstraintsException;

    /** 
     * Checks whether this dungeon can have the given square at the given 
     * coordinate.
     * 
     * @param coordinate 
     * The coordinate to check.
     * @param square 
     * The square to check.
     * @return 
     *   | result == (square != null
     *   |              &amp;&amp; !square.isTerminated()
     *   |              &amp;&amp; isValidSquareCoordinate(coordinate)
     */
    @Raw
    public boolean canHaveAsSquareAt(Coordinate coordinate, S square) {
        return square != null
            && !square.isTerminated()
            && isValidSquareCoordinate(coordinate);
    }






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
     * Checks whether this dungeon has squares that properly border on 
     * neighbouring squares. 
     * 
     * @return
     * True iff every square of this dungeon borders on all its neighbours 
     * (as given by the root dungeon) in the correct direction.
     *   | result == 
     *   |  (for each ps in getPositionsAndSquares() :
     *   |      (for each dn in 
     *   |              getRootDungeon().getDirectionsAndNeighboursOf(
     *   |                                         Coordinate).entrySet() :
     *   |          ps.getValue().getBorderAt(dn.getKey()).bordersOnSquare(
     *   |                                               dn.getValue())))
     * @throws IllegalStateException
     *   | getPositionsAndSquares() == null
     */
    @Raw
    public boolean hasProperBorderingSquares() throws IllegalStateException {
        Dungeon<? super S> root = getRootDungeon();
        if (root == null)
            root = this;
        if (getPositionsAndSquares() == null)
            throw new IllegalStateException(
                    "Could not get positions and squares");
        for (Map.Entry<Coordinate, S> entry : getPositionsAndSquares()) {
            S square = entry.getValue();
            Coordinate coordinate = entry.getKey();

            for (Map.Entry<Direction, ? super S> neighbourEntry :
                    root.getDirectionsAndNeighboursOf(coordinate).entrySet()) {
                Square neighbour = (Square) neighbourEntry.getValue();
                Direction direction = neighbourEntry.getKey();
                if (!square.getBorderAt(direction).bordersOnSquare(neighbour))
                    return false;
            }
        }
        return true;
    }

    /** 
     * Returns the square at the given coordinate in this dungeon.
     * 
     * @param coordinate 
     * The coordinate of the square to return.
     * @throws IllegalArgumentException
     *   | !isPossibleSquareCoordinate(coordinate)
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
    abstract public boolean hasSquare(S square);

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
     *   | !isPossibleSquareCoordinate(coordinate)
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
     *   | !isPossibleSquareCoordinate(coordinate)
     */
    abstract public boolean isOccupied(Coordinate coordinate) 
                                        throws IllegalArgumentException;

    /** 
     * Checks whether all squares of this dungeon have valid coordinates.
     * 
     * @return 
     * True iff all squares of this dungeon have valid coordinates.
     *   | result == (for each e in getPositionsAndSquares() :
     *   |                  canHaveAsSquareAt(e.getKey(), e.getValue()))
     */
    public boolean canHaveSquaresAtTheirCoordinates() {
        for (Map.Entry<Coordinate, S> e : getPositionsAndSquares())
            if (!canHaveAsSquareAt(e.getKey(), e.getValue()))
                return false;
        return true;
    }

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
     *   result == {square in getSquares() | true : true}.size()
     */
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
    @Raw
    abstract public Map<Coordinate, S> getSquareMapping();

    /**
     * Return an iterable of the squares in this dungeon.
     *
     * @return
     * An iterable over the elements of getSquareMapping().values().
     */
    abstract public Iterable<S> getSquares();

    /**
     * Return an iterable of the squares and their position in this 
     * dungeon.
     *
     * @return
     * An iterable over the elements of getSquareMapping().entrySet().
     */
    abstract public Iterable<Map.Entry<Coordinate, S>> getPositionsAndSquares();


    /**
     * Return the root dungeon for this dungeon.
     */
    @Basic @Raw
    public CompositeDungeon<? super S> getRootDungeon() {
        return rootDungeon;
    }
    
    /**
     * Set the root dungeon for this dungeon to the given root dungeon.
     *
     * @param rootDungeon
     * The new root dungeon for this dungeon.
     * @post
     * The new root dungeon for this dungeon is equal to the given root 
     * dungeon.
     *   | new.getRootDungeon() == rootDungeon
     * @throws IllegalArgumentException
     * This dungeon cannot have the given root dungeon as its root dungeon.
     *   | ! canHaveAsRootDungeon(rootDungeon)
     */
    @Raw
    public void setRootDungeon(CompositeDungeon<? super S> rootDungeon)
                                            throws IllegalArgumentException {
        if (!canHaveAsRootDungeon(rootDungeon))
            throw new IllegalArgumentException();
        this.rootDungeon = rootDungeon;
    }
    
    /**
     * Checks whether this dungeon can have the given root dungeon as its 
     * root dungeon.
     *
     * @param rootDungeon
     * The root dungeon to check.
     * @return
     *   | if (isTerminated())
     *   |      then result == (rootDungeon == null)
     *   | else if (rootDungeon != null
     *   |              &amp;&amp; (!rootDungeon.isRootDungeon()
     *   |                      || rootDungeon.isTerminated())
     *   |      then result == false
     *   | else if (old.getRootDungeon() == null)
     *   |      then result == (rootDungeon == null
     *   |                          || rootDungeon.containsDungeon(this))
     *   | else result == (rootDungeon != null
     *   |                  &amp;&amp; rootDungeon.containsDungeon(this))
     */
    @Raw
    public boolean canHaveAsRootDungeon(@Raw CompositeDungeon<?> rootDungeon) {
        if (isTerminated())
            return rootDungeon == null;
        if (rootDungeon != null
                && (!rootDungeon.isRootDungeon()
                        || rootDungeon.isTerminated()))
            return false;
        if (getRootDungeon() == null)
            return rootDungeon == null
                || rootDungeon.containsDungeon(this);
        return rootDungeon != null
            //&& rootDungeon.containsDungeon(getRootDungeon()); //probs als de rootdungeon moet worden geterminered!
            && rootDungeon.containsDungeon(this);
    }

    /** 
     * Checks whether this dungeon is its own root dungeon. 
     * 
     * @return 
     *   | result == (getRootDungeon() == this)
     */
    public boolean isRootDungeon() {
        return getRootDungeon() == this;
    }
    
    /**
     * Variable registering the root dungeon for this dungeon.
     */
    private CompositeDungeon<? super S> rootDungeon;



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
        setRootDungeon(null);
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
     *   | !isPossibleSquareCoordinate(source)
     *   |          || !isPossibleSquareCoordinate(destination)
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
} 

// vim: ts=4:sw=4:expandtab:smarttab

