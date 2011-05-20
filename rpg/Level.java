package rpg;

import be.kuleuven.cs.som.annotate.*;

public class Level<S extends Square> extends Dungeon<S> {

	/** 
	 * Create a new level with the given origin and dimensions.
	 * 
     * @param origin
     * The origin of this new level.
	 * @param xSize
	 * The size of this new level in the x direction.
	 * @param ySize
	 * The size of this new level in the y direction.
	 * @effect
	 *   | super(origin, origin.add(new Coordinate(xSize - 1, ySize - 1, 0)));
	 */
	public Level(Coordinate origin, long xSize, long ySize) 
                                            throws IllegalArgumentException {
		super(origin, origin.add(new Coordinate(xSize - 1, ySize - 1, 0)));
	}

	/** 
	 * Create a new level with the given dimensions.
	 * 
	 * @param xSize
	 * The size of this new level in the x direction.
	 * @param ySize
	 * The size of this new level in the y direction.
	 * @effect
	 *   | this(new Coordinate(0, 0, 0), xSize, ySize)
	 */
	public Level(long xSize, long ySize) {
	    this(new Coordinate(0, 0, 0), xSize, ySize);
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

