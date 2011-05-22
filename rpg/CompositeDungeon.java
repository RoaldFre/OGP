package rpg;

//import rpg.exceptions.*;
import be.kuleuven.cs.som.annotate.*;
import java.util.Set;
import java.util.Map;
import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.NoSuchElementException;

import rpg.exceptions.*;
import rpg.util.*;


/**
 * A class representing a composite dungeon composed of other dungeons.
 *
 * @author Roald Frederickx
 */
public class CompositeDungeon<S extends Square> extends Dungeon<S>{

    /** 
     * Create a new composite dungeon with the given coordinate system as 
     * its coordinate system.
     *
     * @param coordSyst 
     *
     * @effect
     *   | super(coordSyst)
     * @effect
     *   | setParentDungeon(this)
     */
    public CompositeDungeon(CoordinateSystem coordSyst)
                                    throws IllegalArgumentException {
        super(coordSyst);
        setParentDungeon(this);
    }


    /** 
     * Return a mapping of directions to squares that represent all 
     * neighbouring squares of the given coordinate in this composite 
     * dungeon. 
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
    public Map<Direction,S> getDirectionsAndNeighboursOf(Coordinate coordinate)
                                            throws IllegalArgumentException {
        EnumMap<Direction, S> result =
                                new EnumMap<Direction, S>(Direction.class);
        for (Dungeon<? extends S> subDungeon : getSubDungeons()) {
            result.putAll(subDungeon.getDirectionsAndNeighboursOf(coordinate));
        }
        return result;
    }


    /** 
     * Check whether a given (possible) subdungeon of this composite 
     * dungeon can be expanded to the given coordinate system without 
     * overlapping other subdungeons of this composite dungeon or 'breaking 
     * out' of this composite dungeon.
     * 
     * @param dungeon
     * The subdungeon to check.
     * @param coordSyst
     * The new dimensions of the subdungeon to check.
     * @pre
     *   | coordSyst != null
     * @return 
     *   | result == (
     *   |      getCoordSyst().contains(dungeon.getCoordSyst())
     *   |      &amp;&amp; 
     *   |      (for all subDungeon in getSubDungeons() :
     *   |                subDungeon == dungeon 
     *   |                     || !subDungeon.overlaps(coordSyst)))
     */
    public boolean canExpandSubDungeonTo(Dungeon<?> dungeon, 
                                         CoordinateSystem coordSyst) {
        assert coordSyst != null;

        if (!getCoordSyst().contains(dungeon.getCoordSyst()))
            return false;
        for (Dungeon<?> subDungeon : getSubDungeons())
            if (subDungeon != dungeon  &&  subDungeon.overlaps(coordSyst))
                return false;
        return true;
    }


    /** 
     * Check wheter this composite dungeon has the given dungeon as its 
     * subdungeon.
     *
     * @param dungeon 
     * The dungeon to check.
     * @return 
     *   | result == (for some subDungeon in getSubDungeons():
     *   |              subDungeon == dungeon)
     */
    public boolean hasAsSubDungeon(Dungeon<?> dungeon) {
        for (Dungeon<?> subDungeon : getSubDungeons())
            if (subDungeon == dungeon)
                return true;
        return false;
    }

    public Set<Dungeon<? extends S>> getSubDungeons() {
        throw new UnsupportedOperationException();
        //lege iterator als nog geen dungeons!
    }


    /** 
     * Check whether the given coordinate lies within this composite dungeon.
     *
     * @return
     *   | result == (getDungeonContaining(coordinate) != null)
     */
    @Override
    public boolean containsCoordinate(Coordinate coordinate) {
        return getDungeonContaining(coordinate) != null;
    }

    /** 
     * Return the subdungeon of this composite dungeon that contains the 
     * given coordinate.
     * 
     * @param coordinate
     * The coordinate whose containing dungeon to get.
     * @return
     *   | if (coordinate == null
     *   |          || (for each subDungeon in getSubDungeons() :
     *   |                  !subDungeon.containsCoordinate(coordinate)))
     *   | then result == null
     *   | else hasAsSubDungeon(result)
     *   |      &amp;&amp; result.containsCoordinate(coordinate)
     */
    public Dungeon<? extends S> getDungeonContaining(Coordinate coordinate) {
        if (!getCoordSyst().contains(coordinate))
            return null;
        for (Dungeon<? extends S> subDungeon : getSubDungeons())
            if (subDungeon.containsCoordinate(coordinate))
                return subDungeon;
        return null;
    }



    /** 
     * Translate this leaf dungeon over the given offset.
     *
     * @param offset 
     * The offset over which to translate this leaf dungeon.
     * @effect
     *   | translateCoordSyst(offset)
     * @effect
     *   | for each subDungeon in getSubDungeons() :
     *   |      subDungeon.translate(offset)
     */
	@Override
	protected void translate(Coordinate offset) 
            throws IllegalArgumentException, CoordinateConstraintsException {
        translateCoordSyst(offset);
        for (Dungeon<? extends S> subDungeon : getSubDungeons()) {
            try {
                subDungeon.translate(offset);
            } catch (CoordinateConstraintsException cce) {
                //roll back everything that has been done already!
                for (Dungeon<? extends S> subDungeon2 : getSubDungeons()) {
                    if (subDungeon2 == subDungeon)
                        throw cce; //everything rolled back already
                    subDungeon2.translate(offset.mirror());
                }
            }
        }
    }


    /** 
     * Returns the square at the given coordinate in this composite dungeon.
     * 
     * @param coordinate 
     * The coordinate of the square to return.
     * @throws IllegalArgumentException
     *   | !isEffectiveCoordinate(coordinate)
     * @throws CoordinateNotOccupiedException
     *   | !isOccupied(coordinate)
     */
	@Override
	public S getSquareAt(Coordinate coordinate)
            throws IllegalArgumentException, CoordinateNotOccupiedException {
        if (!isEffectiveCoordinate(coordinate))
            throw new IllegalArgumentException();
        if (getDungeonContaining(coordinate) == null)
            throw new CoordinateNotOccupiedException(coordinate, this);
        return getDungeonContaining(coordinate).getSquareAt(coordinate);
	}

    /** 
     * Returns wheter or not this composite dungeon contains the given 
     * square.
     * 
     * @param square 
     * The square to check.
     * @return
     *   | result == (for some subDungeon in getSubDungeons() :
     *   |                              subDungeon.hasSquare(square))
     */
	@Override
	public boolean hasSquare(Square square) {
        for (Dungeon<? extends S> subDungeon : getSubDungeons())
            if (subDungeon.hasSquare(square))
                return true;
		return false;
	}


    /** 
     * Deletes the square at the given coordinate and terminates it.
     *
     * @param coordinate 
     * The coordinate to remove the square at.
     * @effect
     *   | getDungeonContaining(coordinate).deleteSquareAt(coordinate)
     * @throws CoordinateNotOccupiedException
     *   | !getDungeonContaining(coordinate) == null
     * @throws IllegalArgumentException
     *   | !isEffectiveCoordinate(coordinate)
     */
	@Override
	public void deleteSquareAt(Coordinate coordinate)
			throws IllegalArgumentException, CoordinateNotOccupiedException {
        if (!isEffectiveCoordinate(coordinate))
            throw new IllegalArgumentException();
        if (getDungeonContaining(coordinate) == null)
            throw new CoordinateNotOccupiedException(coordinate, this);
        getDungeonContaining(coordinate).deleteSquareAt(coordinate);
	}


    /** 
     * Returns whether or not the given coordinate is occupied in this 
     * composite dungeon.
     * 
     * @param coordinate 
     * The coordinate to check.
     * @throws IllegalArgumentException
     *   | !isEffectiveCoordinate(coordinate)
     */
	@Override
	public boolean isOccupied(Coordinate coordinate)
			throws IllegalArgumentException {
        if (!isEffectiveCoordinate(coordinate))
            throw new IllegalArgumentException();
        if (getDungeonContaining(coordinate) == null)
            return false;
        return getDungeonContaining(coordinate).isOccupied(coordinate);
	}


	
	/** 
     * Return the number of squares in this composite dungeon.
	 */
	@Override
	public int getNbSquares() {
        int result = 0;
        for (Dungeon<? extends S> subDungeon : getSubDungeons())
            result += subDungeon.getNbSquares();
        return result;
	}


    /**
     * Add the mapping of coordinates to squares of this composite dungeon 
     * to the given map.
     *
     * @param map 
     * The map of coordinates to squares to add the mapping of coordinates 
     * to squares of this dungeon to.
     * @effect
     *   | for each subDungeon in getSubDungeons()
     *   |      subDungeon.addSquareMappingTo(map)
     */
	@Override @Basic @Raw
	public void addSquareMappingTo(Map<Coordinate, ? super S> map)
                                            throws IllegalStateException{
        for (Dungeon<? extends S> subDungeon : getSubDungeons())
            subDungeon.addSquareMappingTo(map);
	}


	@Override
	public Iterator<S> getFilteredSquareIterator(
                                        final SquareFilter squareFilter) {
        return new Iterator<S>() {
            {
                Iterator<Dungeon<? extends S>> subDungeonIterator;
                subDungeonIterator = getSubDungeons().iterator();
                if (!subDungeonIterator.hasNext()) {
                    next = null;
                } else {
                    this.subDungeonIterator = subDungeonIterator;
                    this.squareIterator = subDungeonIterator.next().
                                    getFilteredSquareIterator(squareFilter);
                    this.next = getNextSquare();
                }
            }
           
            private S getNextSquare() {
                if (squareIterator.hasNext())
                    return squareIterator.next();
                if (subDungeonIterator.hasNext()) {
                    squareIterator = subDungeonIterator.next().
                                    getFilteredSquareIterator(squareFilter);
                    return getNextSquare();
                }
                return null;
            }

			public boolean hasNext() {
                return next != null;
			}

            public S next() throws NoSuchElementException {
                if (!hasNext())
                    throw new NoSuchElementException();
                S result = next;
                next = getNextSquare();
                return result;
			}

			public void remove() throws UnsupportedOperationException {
				throw new UnsupportedOperationException();
			}

			private Iterator<? extends S> squareIterator;
            private Iterator<Dungeon<? extends S>> subDungeonIterator;
            private S next;
		};
	}


	/* (non-Javadoc)
	 * @see rpg.Dungeon#getPositionsAndSquares()
	 */
	@Override
	public Iterable<Entry<Coordinate, S>> getPositionsAndSquares() {
        throw new UnsupportedOperationException();
	}



    //dungeons: Dungeon<? extends S> !!!
}

// vim: ts=4:sw=4:expandtab:smarttab
