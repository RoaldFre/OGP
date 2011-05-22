package rpg;

//import rpg.exceptions.*;
import be.kuleuven.cs.som.annotate.*;
import java.util.Map;
import java.util.EnumMap;

import rpg.util.Coordinate;
import rpg.util.CoordinateSystem;
import rpg.util.Direction;


/**
 * A class representing a composite dungeon composed of other dungeons.
 *
 * @author Roald Frederickx
 */
abstract public class CompositeDungeon<S extends Square> extends Dungeon<S>{
    public CompositeDungeon(CoordinateSystem coordSyst) {
        super(coordSyst);
        throw new UnsupportedOperationException();
    }

    public boolean containsDungeon(Dungeon<?> dungeon) {
        if (dungeon == this)
            return true;
        throw new UnsupportedOperationException();
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

    public Iterable<Dungeon<? extends S>> getSubDungeons() {
        throw new UnsupportedOperationException();
        //lege iterator als nog geen dungeons!
    }



    //dungeons: Dungeon<? extends S> !!!
}

// vim: ts=4:sw=4:expandtab:smarttab
