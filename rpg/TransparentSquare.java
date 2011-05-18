package rpg;

import be.kuleuven.cs.som.annotate.*;

//TODO specs
public class TransparentSquare extends Square {

    /** 
     * Check whether this transparent square can have the given border as 
     * its border in the given direction.
     * 
     * @param direction 
     * The direction of the border.
     * @param border
     * The border to check.
     * @return
     *   | result == (super.canPossiblyHaveAsBorderAt(direction, border)
     *   |                  &amp;&amp; (border == null || border.isWall()))
     */
    @Raw
    @Override
    public boolean canHaveAsBorderAt(Direction direction, Border border) {
        return super.canPossiblyHaveAsBorderAt(direction, border)
                                    && (border == null || border.isWall());
    }

    /**
     * Checks whether the borders of this transparent square satisfy the 
     * constraints of the game.
     *
     * @return
     * True iff this transparent square is not terminated and has:
     *   - no walls
     *   - no doors placed in ceilings or floors
     *   - at least one door, and no more than two doors
     *   - both doors in complementary directions if it has two doors
     */
    @Raw
    @Override
    public boolean bordersSatisfyConstraints() {
        if (isTerminated())
            return true;

        int numDoors = 0;
        Direction dirOf1stDoor = null; //initialize to make compiler happy
        for (Direction dir : Direction.values()){
            Border border = getBorderAt(dir);
            if (border.isWall())
                return false;
            if (border.isDoor()) {
                numDoors++;
                if (dir.equals(Direction.UP) 
                                    || dir.equals(Direction.DOWN))
                    return false;
                if (numDoors == 1)
                    dirOf1stDoor = dir;
                else if (numDoors > 2
                            || numDoors == 2  
                                && !dir.equals(dirOf1stDoor.complement()))
                    return false;
            }
        }
        return numDoors >= 1;
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

