package rpg;

import be.kuleuven.cs.som.annotate.*;

//TODO specs
public class TransparantSquare extends Square {

    /** 
	 * Check whether this transparant square can have the given border as 
	 * its border in the given direction.
     * 
     * @param direction 
     * The direction of the border.
     * @param border
     * The border to check.
     * @return
	 * True iff the given direction is valid and:
	 *   - this transparant square is terminated and the given border is 
	 *     null, or
	 *   - this transparant square is not terminated and the given border 
	 *     is a wall and not null nor terminated.
     *   | if (!isValidDirection(direction))
     *   |      then result == false
     *   | else if (isTerminated())
     *   |      then result == (border == null)
     *   | else
     *   |      result == (border != null 
     *   |                      &amp;&amp; !border.isTerminated()
     *   |                      &amp;&amp; border.isWall())
     */
    @Raw
	@Override
    public boolean canHaveAsBorderAt(Direction direction, Border border) {
        if (!isValidDirection(direction))
            return false;
        if (isTerminated())
            return border == null;
        return (border != null) 
					&& (!border.isTerminated())
					&& border.isWall();
    }



    /**
	 * Checks whether the borders of this transparant square satisfy the 
	 * constraints of the game.
     *
     * @return
     * True if this transparant square is terminated,
     *   | if isTerminated()
     *   |      then result == true
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
				//TODO need this constraint? how to interpret assignment??
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
