package rpg;

//import rpg.exceptions.*;
import be.kuleuven.cs.som.annotate.*;
import java.util.Set;
import java.util.HashSet;
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
 * @invar
 *   | hasProperSubDungeons()
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
     */
    public CompositeDungeon(CoordinateSystem coordSyst)
                                    throws IllegalArgumentException {
        super(coordSyst);
    }


    /** 
     * Return a mapping of directions to squares that represent all 
     * neighbouring squares of the given coordinate in this composite 
     * dungeon. 
     */
    @Raw
    @Override
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
     * The subdungeon to check. May be null, in which case only check if 
     * the given coordinate system fits in this composite dungeon without 
     * overlapping subdungeons or 'breaking out'.
     * @param coordSyst
     * The new dimensions of the subdungeon to check.
     * @pre
     *   | coordSyst != null
     * @return 
     *   | result == (
     *   |      getCoordSyst().contains(coordSyst)
     *   |      &amp;&amp; 
     *   |      (for all subDungeon in getSubDungeons() :
     *   |                subDungeon == dungeon 
     *   |                     || !subDungeon.overlaps(coordSyst)))
     */
    public boolean canExpandSubDungeonTo(Dungeon<?> dungeon, 
                                         CoordinateSystem coordSyst) {
        assert coordSyst != null;

        if (!getCoordSyst().contains(coordSyst))
            return false;
        for (Dungeon<?> subDungeon : getSubDungeons())
            if (subDungeon != dungeon  &&  subDungeon.overlaps(coordSyst))
                return false;
        return true;
    }


    /** 
     * Check whether the given coordinate lies within this composite dungeon.
     *
     * @return
     *   | result == (getSubDungeonContaining(coordinate) != null)
     */
    @Override
    public boolean containsCoordinate(Coordinate coordinate) {
        return getSubDungeonContaining(coordinate) != null;
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
    public Dungeon<? extends S> getSubDungeonContaining(
                                                Coordinate coordinate) {
        if (!getCoordSyst().contains(coordinate))
            return null;
        for (Dungeon<? extends S> subDungeon : getSubDungeons())
            if (subDungeon.containsCoordinate(coordinate))
                return subDungeon;
        return null;
    }


    /** 
     * Translate this composite dungeon over the given offset.
     *
     * @param offset 
     * The offset over which to translate this composite dungeon.
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
        Set<Dungeon<?>> translatedSubDungeons = new HashSet<Dungeon<?>>();
        for (Dungeon<? extends S> subDungeon : getSubDungeons()) {
            try {
                subDungeon.translate(offset);
                translatedSubDungeons.add(subDungeon);
            } catch (CoordinateConstraintsException cce) {
                //roll back everything that has been done already!
                for (Dungeon<?> translatedSubDungeon : translatedSubDungeons) {
                    try {
                        translatedSubDungeon.translate(offset.mirror());
                    } catch (CoordinateConstraintsException cce2) {
                        assert false;
                    }
                }
                translateCoordSyst(offset.mirror());
                throw cce;
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
        if (getSubDungeonContaining(coordinate) == null)
            throw new CoordinateNotOccupiedException(coordinate, this);
        return getSubDungeonContaining(coordinate).getSquareAt(coordinate);
	}


    /** 
     * Returns whether or not this composite dungeon contains the given 
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
     *   | getSubDungeonContaining(coordinate).deleteSquareAt(coordinate)
     * @throws CoordinateNotOccupiedException
     *   | !getSubDungeonContaining(coordinate) == null
     * @throws IllegalArgumentException
     *   | !isEffectiveCoordinate(coordinate)
     */
	@Override
	public void deleteSquareAt(Coordinate coordinate)
			throws IllegalArgumentException, CoordinateNotOccupiedException {
        if (!isEffectiveCoordinate(coordinate))
            throw new IllegalArgumentException();
        if (getSubDungeonContaining(coordinate) == null)
            throw new CoordinateNotOccupiedException(coordinate, this);
        getSubDungeonContaining(coordinate).deleteSquareAt(coordinate);
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
        if (getSubDungeonContaining(coordinate) == null)
            return false;
        return getSubDungeonContaining(coordinate).isOccupied(coordinate);
	}


	/** 
     * Return the number of squares in this composite dungeon.
	 */
	@Override
    @Raw
	public int getNbSquares() {
        int result = 0;
        if (getSubDungeons() == null)
            return 0;
        for (Dungeon<? extends S> subDungeon : getSubDungeons())
            result += subDungeon.getNbSquares();
        return result;
	}


    /**
     * Add the mapping of coordinates to squares of this composite dungeon 
     * to the given map.
     *
     * @effect
     *   | if (getSubDungeons() != null)
     *   | then for each subDungeon in getSubDungeons()
     *   |      subDungeon.addSquareMappingTo(map)
     */
	@Override
    @Raw
	public void addSquareMappingTo(Map<Coordinate, ? super S> map)
                                            throws IllegalStateException{
        if (getSubDungeons() == null)
            return;
        for (Dungeon<? extends S> subDungeon : getSubDungeons())
            subDungeon.addSquareMappingTo(map);
	}


    /**
     * Return an iterator of the squares in this composite dungeon that 
     * satisfy the conditions as imposed by the given filter.
     */
	@Override
	public Iterator<S> getFilteredSquareIterator(
                                        final SquareFilter squareFilter) {
        return new Iterator<S>() {
            {
                if (getSubDungeons() == null) {
                    next = null;
                } else {
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


    /**
     * Return an iterable of the squares and their position in this 
     * composite dungeon.
     */
	@Override @Raw
	public Iterable<Entry<Coordinate, S>> getPositionsAndSquares()
                                            throws IllegalStateException {

                                            
                                            
        throw new UnsupportedOperationException();






	}






    
    /**
     * Return the set of direct subdungeons for this composite dungeon.
     */
    @Basic @Raw
    public Set<Dungeon<? extends S>> getSubDungeons() {
        return subDungeons;
    }


    /** 
     * Check whether this composite dungeon has the given dungeon as its 
     * direct subdungeon.
     *
     * @param dungeon 
     * The dungeon to check.
     * @return 
     *   | if (getSubDungeons() == null)
     *   |      then result == false
     *   |      else result == (for some subDungeon in getSubDungeons():
     *   |                          subDungeon == dungeon)
     */
    @Raw
    public boolean hasAsSubDungeon(Dungeon<?> dungeon) {
        if (getSubDungeons() == null)
            return false;
        for (Dungeon<?> subDungeon : getSubDungeons())
            if (subDungeon == dungeon)
                return true;
        return false;
    }


    /** 
     * Add the given dungeon as a subdungeon in this composite dungeon at 
     * the given offset. 
     * 
     * @param subDungeon 
     * The dungeon to add as a subdungeon.
     * @param offset 
     * The offset at which to add the subdungeon.
     * @post
     *   | new.hasAsSubDungeon(subDungeon)
     * @effect
     *   | subDungeon.setParentDungeon(this)
     * @throws IllegalArgumentException
     *   | !isEffectiveCoordinate(offset) || subDungeon == null
     * @throws IllegalStateException
     *   | isTerminated() || subDungeon.isTerminated()
     * @throws DungeonAlreadyAssociatedException
     *   | subDungeon.getParentDungeon() != null
     * @throws SubDungeonDoesNotFitException
     * The given subdungeon does not fit in this composite dungeon at the 
     * given offset, or it overlaps other subdungeons of this composite 
     * dungeon.
     */
    public void addSubDungeonAt(Dungeon<? extends S> subDungeon,
                                Coordinate offset)
                    throws IllegalArgumentException,
                           IllegalStateException,
                           DungeonAlreadyAssociatedException,
                           SubDungeonDoesNotFitException {
        if (!isEffectiveCoordinate(offset))
            throw new IllegalArgumentException("Non-effective coordinate");
        if (subDungeon == null)
            throw new IllegalArgumentException("Non-effective subDungeon");
        if (isTerminated() || subDungeon.isTerminated())
            throw new IllegalStateException();
        if (subDungeon.getParentDungeon() != null)
            throw new DungeonAlreadyAssociatedException(subDungeon, this);
        CoordinateSystem translatedCoordSyst = subDungeon.getCoordSyst();
        translatedCoordSyst.translate(offset);
        if (!canExpandSubDungeonTo(null, translatedCoordSyst))
            throw new SubDungeonDoesNotFitException(subDungeon, this);

        subDungeon.translate(offset);
        subDungeons.add(subDungeon);
        subDungeon.setParentDungeon(this);
    }
    

    /**
     * Checks whether this composite dungeon has proper subdungeons.
     *
     * @return
     *   | if (getSubDungeons() == null)
     *   |      result == false
     *   |      else result == (for each subDungeon in getSubDungeons() :
     *   |                          isProperSubDungeon(subDungeon)
     */
    @Raw
    public boolean hasProperSubDungeons() {
        if (getSubDungeons() == null)
            return false;
        for (Dungeon<? extends S> subDungeon : getSubDungeons())
            if (!isProperSubDungeon(subDungeon))
                return false;
        return true;
    }

    
    /** 
     * Checks if the given subdungeon is, or would be, a proper subdungeon 
     * for this composite dungeon.
     * 
     * @param subDungeon 
     * The subdugeon to check.
     * @return 
     *   | (subDungeon != null
     *   |          &amp;&amp; subDungeon.getParentDungeon() == this)
     */
    @Raw
    public boolean isProperSubDungeon(@Raw Dungeon<? extends S> subDungeon) {
        if (subDungeon == null)
            return false;
        return subDungeon.getParentDungeon() == this;
    }

    
    /**
     * Variable registering the set of subdungeons for this composite dungeon.
     */
    private Set<Dungeon<? extends S>> subDungeons = 
                                        new HashSet<Dungeon<? extends S>>();


    /**
     * Check whether this composite dungeon is not raw.
     */
    @Raw
    @Override
    public boolean isNotRaw() {
        return super.isNotRaw()
                && squaresSatisfyConstraints()
                && canHaveAsCoordSyst(getCoordSyst())
                && getSquareMapping() != null
                && canHaveAsParentDungeon(getParentDungeon())
                && hasProperSubDungeons();
    }
}

// vim: ts=4:sw=4:expandtab:smarttab
