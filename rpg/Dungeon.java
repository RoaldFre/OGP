package rpg;

import rpg.exceptions.*;
import be.kuleuven.cs.som.annotate.*;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.HashSet;

/**
 * A class representing a dungeon of squares.
 *
 * @invar
 *   | canHaveSquaresAtCoordinates()
 * @invar
 *   | squaresSatisfyConstraints()
 * @invar
 *   | hasProperBorderingSquares()
 *
 * @author Roald Frederickx
 */
public class Dungeon {

	/** 
	 * Create a new dungeon with the given far corner. 
	 * 
	 * @param farCorner
	 * The far corner for this new dungeon.
	 * @throws IllegalArgumentException
	 * The given far corner is not effective, or it doesn't form a valid 
	 * far corner (not all components are positive).
	 *   | farCorner == null
	 *   |		|| !Coordinate.formsValidBoundingBox(ORIGIN, farCorner)
	 */
	public Dungeon(Coordinate farCorner) throws IllegalArgumentException {
		coordSyst = new CoordinateSystem(ORIGIN, farCorner);
	}

	/**
	 * Return the far corner for this dungeon.
	 */
	@Basic @Raw
	public Coordinate getFarCorner() {
		return coordSyst.getUpperBound();
	}
	
	/**
	 * Set the far corner for this dungeon to the given far corner.
	 *
	 * @param farCorner
	 * The new far corner for this dungeon.
	 * @post
	 * The new far corner for this dungeon is equal to the given far corner.
	 *   | new.getFarCorner().equals(farCorner)
	 * @throws IllegalArgumentException
	 * This dungeon cannot have the given far corner as its far corner. 
	 * This means it is either not effective, or it is a stricter bound 
	 * than the previous far corner.
	 *   | farCorner == null
	 *   |		|| ((old.getFarCorner() != null) &amp;&amp; 
	 *   |				Coordinate.formsValidBoundingBox(old.getFarCorner(),
	 *   |														farCorner))
	 *   |		|| !Coordinate.formsValidBoundingBox(ORIGIN, farCorner)
	 */
	@Raw
	public void setFarCorner(Coordinate farCorner)
										throws IllegalArgumentException {
		coordSyst.setUpperBound(farCorner);
	}

	/** 
	 * Checks whether the given coordinate is a valid coordinate in this 
	 * dungeon.
	 *
	 * @param coordinate 
	 * The coordinate to check.
	 * @return 
	 * True iff the coordinate is effective, bounded by the origin and the 
	 * far corner, and the coordinate values in all directions are not 
	 * equal to each other.
	 *   | result == (isPossibleSquareCoordinate(coordinate)
	 *   |		&amp;&amp; coordinate.isBoundedBy(ORIGIN, getFarCorner())
	 *   |		&amp;&amp; (coordinate.x != coordinate.y
	 *   |					|| coordinate.y != coordinate.z
	 *   |					|| coordinate.z != coordinate.x))
	 */
	public boolean isValidSquareCoordinate(Coordinate coordinate) {
		if (!isPossibleSquareCoordinate(coordinate))
			return false;
		if (!coordSyst.isValidCoordinate(coordinate))
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
	 * Variable referencing the coordinate of the origin of this dungeon.
	 */
	public final Coordinate ORIGIN = new Coordinate(0, 0, 0);

	/** 
	 * Variable referencing the coordinate system that belongs to this 
	 * dungeon.
	 */
	private CoordinateSystem coordSyst;

	/** 
	 * Add the given square to this dungeon at the given coordinate.
	 *
	 * @param coordinate 
	 * The coordinate to add the given square at.
	 * @param square 
	 * The square to add at the given coordinate.
	 * @effect
	 * The squares that border the given coordinate in this dungeon get 
	 * merged with the given square in the appropriate direction.
	 *   | for each e in getDirectionsAndNeighboursOf(coordinate).entrySet() :
	 *   |		square.mergeWith(e.getValue()), e.getKey()
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
	public void addSquareAt(Coordinate coordinate, Square square) 
										throws IllegalArgumentException,
												CoordinateOccupiedException,
												DungeonConstraintsException {
		if (!canHaveAsSquareAt(coordinate, square))
			throw new IllegalArgumentException();
		if (isOccupied(coordinate))
			throw new CoordinateOccupiedException(coordinate, this);

		squares.put(coordinate, square);

		if (!squaresSatisfyConstraints()){
			squares.remove(coordinate);
			throw new DungeonConstraintsException(square, this);
		}

		for (Map.Entry<Direction, Square> neighbourEntry :
						getDirectionsAndNeighboursOf(coordinate).entrySet()){
			Square neighbour = neighbourEntry.getValue();
			Direction neighbourDirection = neighbourEntry.getKey();
			square.mergeWith(neighbour, neighbourDirection);
		}
	}

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
	 *   |				&amp;&amp; !square.isTerminated()
	 *   |				&amp;&amp; isValidSquareCoordinate(coordinate)
	 */
	public boolean canHaveAsSquareAt(Coordinate coordinate, Square square) {
		return square != null
					&& !square.isTerminated()
					&& isValidSquareCoordinate(coordinate);
	}

	/** 
	 * Return a mapping of coordinates to squares that represent all 
	 * neighbouring squares of the given coordinate in this dungeon. 
	 * 
	 * @param coordinate 
	 * The coordinate whose neighbours to return.
	 * @return
	 * A mapping of coordinates to squares that represent all neighbouring 
	 * squares of the given coordinate in this dungeon. 
	 * @throws IllegalArgumentException
	 *   | coordinate == null
	 */
	public Map<Coordinate, Square> getCoordinatesAndNeighboursOf(
														Coordinate coordinate)
											throws IllegalArgumentException {
		if (coordinate == null)
			throw new IllegalArgumentException();

		HashMap<Coordinate, Square> result =
								new HashMap<Coordinate, Square>();

		for (Coordinate neighbourCoordinate : 
								coordSyst.neighboursOf(coordinate).values()) {
			if (!isOccupied(neighbourCoordinate))
				continue;

			Square neighbour = getSquareAt(neighbourCoordinate);
			result.put(neighbourCoordinate, neighbour);
		}

		return result;
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
	 *   |		e.getValue() == getSquareAt(coordinate.moveTo(e.getKey()))
	 * @throws IllegalArgumentException
	 *   | coordinate == null
	 */
	public Map<Direction, Square> getDirectionsAndNeighboursOf(
														Coordinate coordinate)
											throws IllegalArgumentException {
		if (coordinate == null)
			throw new IllegalArgumentException();

		EnumMap<Direction, Square> result =
					new EnumMap<Direction, Square>(Direction.class);

		for (Map.Entry<Direction, Coordinate> neighbourEntry :
							coordSyst.neighboursOf(coordinate).entrySet()) {
			Coordinate neighbourCoordinate = neighbourEntry.getValue();
			Direction neighbourDirection = neighbourEntry.getKey();
			Square neighbour = squares.get(neighbourCoordinate);
			if (neighbour != null)
				result.put(neighbourDirection, neighbour);
		}

		return result;
	}


	/** 
	 * Checks whether this dungeon has squares that properly border on 
	 * neighbouing squares. 
	 * 
	 * @return
	 * True iff every square of this dungeon borders on its neighbours in 
	 * this dungeon in the correct direction.
	 *   | result == 
	 *   |		(for each e in 
	 *   |				getDirectionsAndNeighboursOf(Coordinate).entrySet() :
	 *   |			square.getBorderAt(e.getKey()).bordersOnSquare(
	 *   |													e.getValue()))
	 */
	public boolean hasProperBorderingSquares() {
		for (Map.Entry<Coordinate, Square> entry : getPositionsAndSquares()) {
			Square square = entry.getValue();
			Coordinate coordinate = entry.getKey();
			
			for (Map.Entry<Direction, Square> neighbourEntry :
						getDirectionsAndNeighboursOf(coordinate).entrySet()) {
				Square neighbour = neighbourEntry.getValue();
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
	@Basic @Raw
	public Square getSquareAt(Coordinate coordinate) 
									throws IllegalArgumentException,
											CoordinateNotOccupiedException {
		if (!isPossibleSquareCoordinate(coordinate))
			throw new IllegalArgumentException();

		Square result = squares.get(coordinate);
		if (result == null)
			throw new CoordinateNotOccupiedException(coordinate, this);

		return result;
	}

	/** 
	 * Returns wheter or not this dungeon contains the given square.
	 * 
	 * @param square 
	 * The square to check.
	 */
	@Basic @Raw
	public boolean hasSquare(Square square) {
		return squares.containsValue(square);
	}

	/** 
	 * Deletes the square at the given coordinate and terminates it.
	 *
	 * @param coordinate 
	 * The coordinate to remove the square at.
	 * @post
	 *   | !isOccupied(coordinate)
	 * @effect
	 *   | old.getSquareAt(coordinate).terminate()
	 */
	public void deleteSquareAt(Coordinate coordinate) 
									throws IllegalArgumentException,
											CoordinateNotOccupiedException {
		Square square = getSquareAt(coordinate);
		square.terminate(); //detatches all neighbouring squares too
		squares.remove(coordinate);
	}

	/** 
	 * Returns whether or not the given coordinate is occupied in this 
	 * dungeon.
	 * 
	 * @param coordinate 
	 * The coordinate to check.
	 * @throws IllegalArgumentException
	 *   | !isPossibleSquareCoordinate(coordinate)
	 */
	@Basic
	public boolean isOccupied(Coordinate coordinate) {
		if (!isPossibleSquareCoordinate(coordinate))
			throw new IllegalArgumentException();
		return squares.containsKey(coordinate);
	}

	/** 
	 * Checks whether all squares of this dungeon have valid coordinates.
	 * 
	 * @return 
	 * True iff all squares of this dungeon have valid coordinates.
	 *   | result == (for each e in getPositionsAndSquares() :
	 *   |					canHaveAsSquareAt(e.getKey(), e.getValue()))
	 */
	public boolean canHaveSquaresAtCoordinates() {
		for (Map.Entry<Coordinate, Square> e : getPositionsAndSquares())
			if (!canHaveAsSquareAt(e.getKey(), e.getValue()))
				return false;
		return true;
	}


	/** 
	 * Checks whether the squares of this dungeon satisfy the constraints 
	 * on squares of a dungeon.
	 * 
	 * @return
	 * True iff not more than 20% of the squares have a slippery floor.
	 *   |  result == (getNbSlipperySquares() * 5 &lt;= getNbSquares())
	 */
	public boolean squaresSatisfyConstraints() {
		return (getNbSlipperySquares() * 5 <= getNbSquares());
	}

	/** 
	 * Return the number of squares in this dungeon.
	 */
	@Basic
	public int getNbSquares() {
		return squares.size();
	}

	/** 
	 * Return the number of squares in this dungeon that have a slippery 
	 * floor.
	 *
	 * @return
	 * The number of squares in this dungeon that have a slippery 
	 * floor.
	 *   | ({ square in getSquares() | true :
	 *   |					square.hasSlipperyFloor()}.size() == result)
	 */
	public int getNbSlipperySquares() {
		int nbSlipperySquares = 0;
		for (Square square : getSquares())
			if (square.hasSlipperyFloor())
				nbSlipperySquares++;
		return nbSlipperySquares;
	}

	/**
	 * Return an iterable of the squares in this dungeon.
	 */
	@Basic
	public Iterable<Square> getSquares() {
		return squares.values();
	}

	/**
	 * Return an iterable of the squares and their position in this dungeon.
	 */
	@Basic
	public Iterable<Map.Entry<Coordinate, Square>> getPositionsAndSquares() {
		return squares.entrySet();
	}

	/**
	 * Variable referencing a map of the squares of this dungeon
	 */
	private Map<Coordinate, Square> squares = 
										new HashMap<Coordinate, Square>();

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
	 *   |			|| !isPossibleSquareCoordinate(destination)
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
		
		Square sourceSquare = getSquareAt(source);

		for (Direction direction :
							getDirectionsAndNeighboursOf(source).keySet())
			if (sourceSquare.getBorderAt(direction).isOpen()
									&& canReach(source.moveTo(direction),
														destination, visited))
					return true;

		return false;
	}
}
