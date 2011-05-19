package rpg;

import be.kuleuven.cs.som.annotate.*;

public class Level<S extends Square> extends Dungeon<S> {

	/** 
	 * Create a new level with the given dimensions
	 * 
	 * @param xSize
	 * The size of the level in the x direction.
	 * @param ySize
	 * The size of the level in the y direction.
	 * @effect
	 *   | super(new Coordinate(xSize, ySize, 0))
	 */
	public Level(long xSize, long ySize) {
		super(new Coordinate(xSize, ySize, 0));
	}



    /** 
     * Check if this level can have the given origin as its origin.
     * 
     * @param origin 
     * The origin to check.
     * @return
     *   | result == (getCoordSyst().canHaveAsLowerBound(origin)
	 *   |				&amp;&amp; (old.getOrigin() == null 
	 *   |							|| old.getOrigin(),z == origin.z)
     */
    @Raw @Override
    public boolean canHaveAsOrigin(Coordinate origin) {
        return getCoordSyst().canHaveAsLowerBound(origin)
				&& (getOrigin() == null || getOrigin().z == origin.z);
        //TODO aan root vragen of dit niet overlapt!
    }


    /** 
     * Check if this level can have the given far corner as its far 
     * corner. 
     * 
     * @param farCorner 
     * The far corner to check.
     * @return
     *   | if !getCoordSyst().canHaveAsUpperBound(farCorner)
	 *   |				&amp;&amp; (old.getOrigin() == null 
	 *   |							|| old.getFarCorner(),z == farCorner.z)
     */
    @Raw
    public boolean canHaveAsFarCorner(Coordinate farCorner) {
        return getCoordSyst().canHaveAsUpperBound(farCorner)
				&& (getFarCorner() == null || getFarCorner().z == farCorner.z);
        //TODO aan root vragen of dit niet overlapt!
    }

}

// vim: ts=4:sw=4:expandtab:smarttab

