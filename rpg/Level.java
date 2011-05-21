package rpg;

import be.kuleuven.cs.som.annotate.*;

public class Level<S extends Square> extends LeafDungeon<S> {

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
	 *   | super(new CoordinateSystem(origin,
     *   |          origin.add(new Coordinate(xSize - 1, ySize - 1, 0))));
	 */
	public Level(Coordinate origin, long xSize, long ySize) 
                                            throws IllegalArgumentException {
		super(new CoordinateSystem(origin, 
                    origin.add(new Coordinate(xSize - 1, ySize - 1, 0))));
	}

	/** 
     * Create a new level with the given dimensions and default origin.
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
     * Checks whether the given coordinate system is a valid coordinate 
     * system for this level.
     *
     * @param coordSyst
     * The coordinate system to check.
     * @return
     *   | result == (canHaveAsPossibleCoordSyst(coordSyst)
     *   |      &amp;&amp; (coordSyst.getLowerBound().z ==
     *   |                  coordSyst.getUpperBound().z)
     *   |      &amp;&amp; (old.getCoordSyst() == null
     *   |                  || coordSyst.getLowerBound().z
     *   |                      == old.getCoordSyst().getLowerBound().z))
     */
    @Raw
    public boolean canHaveAsCoordSyst(CoordinateSystem coordSyst) {
        if (!canHaveAsPossibleCoordSyst(coordSyst))
            return false;
        if (coordSyst.getLowerBound().z != coordSyst.getUpperBound().z)
            return false;
        CoordinateSystem oldCoordSyst = getCoordSyst();
        if (oldCoordSyst == null)
            return true;
        return coordSyst.getLowerBound().z == oldCoordSyst.getLowerBound().z;
    }



}

// vim: ts=4:sw=4:expandtab:smarttab

