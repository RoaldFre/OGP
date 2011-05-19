package rpg;

import be.kuleuven.cs.som.annotate.*;
import java.util.Set;

/**
 * @invar
 * Each regular teleportation square can have its teleporter as its teleporter.
 *   | canHaveAsTeleporter(getTeleporter())
 *
 * @author Roald Frederickx
 */
public class RegularTeleportationSquare
							extends RegularSquare
							implements Teleportable {
    /** 
	 * Initialize this new regular teleportation square to a regular 
	 * teleportation square with the given temperature, temperature limits, 
	 * humidity, slipperiness of the floor and teleporter. 
     * 
     * @param temperature
	 * The temperature for this new regular teleportation square.
	 * @param minTemp
	 * The minimum temperature for this new regular teleportation square.
	 * @param maxTemp
	 * The maximum temperature for this new regular teleportation square.
	 * @param humidity
	 * The humidity for this new regular teleportation square.
	 * @param hasSlipperyFloor
	 * Whether or not this new regular teleportation square has a slippery 
	 * floor.
	 * @param teleporter
	 * The teleporter for this new regular teleportation square.
     * @effect
     *   | super(temperature, minTemp, maxTemp, humidity, hasSlipperyFloor)
	 * @effect
	 *   | setTeleporter(teleporter)
     */
    @Raw
    public RegularTeleportationSquare(Temperature temperature,
                    Temperature minTemp, Temperature maxTemp,
                    int humidity, boolean hasSlipperyFloor,
					Teleporter teleporter)
                                            throws IllegalArgumentException {
        super(temperature, minTemp, maxTemp, humidity, hasSlipperyFloor);
		setTeleporter(teleporter);
    }

    /** 
	 * Initialize this new regular teleportation square to a regular 
	 * teleportation square with the given temperature and humidity and a 
	 * non slippery floor. 
     *
     * @param temperature
     * The temperature for this new regular teleportation square.
     * @param humidity
     * The humidity for this new regular teleportation square.
	 * @param teleporter
	 * The teleporter for this new regular teleportation square.
     * @effect
     *   | super(temperature, humidity)
	 * @effect
	 *   | setTeleporter(teleporter)
     */
    @Raw
    public RegularTeleportationSquare(Temperature temperature, int humidity,
									Teleporter teleporter) 
                                    throws IllegalArgumentException {
        super(temperature, humidity);
		setTeleporter(teleporter);
    }

    /** 
	 * Initialize this new regular teleportation square to a default 
	 * regular teleportation square. 
     *
	 * @param teleporter
	 * The teleporter for this new regular teleportation square.
     * @effect
     *   | super(new RegularBorderInitializer(false))
	 * @effect
	 *   | setTeleporter(teleporter)
     */
    @Raw
    public RegularTeleportationSquare(Teleporter teleporter) {
        super();
		setTeleporter(teleporter);
    }


	/**
	 * Return the teleporter for this regular teleportation square.
	 */
	@Basic @Raw
	public Teleporter getTeleporter() {
		return teleporter;
	}
	
	/**
	 * Set the teleporter for this regular teleportation square to the 
	 * given teleporter.
	 *
	 * @param teleporter
	 * The new teleporter for this regular teleportation square.
	 * @pre
	 * The given teleporter must be a valid teleporter for this regular 
	 * teleportation square.
	 *   | canHaveAsTeleporter(teleporter)
	 * @post
	 * The new teleporter for this regular teleportation square is equal to 
	 * the given teleporter.
	 *   | new.getTeleporter() == teleporter
	 */
	@Raw
	public void setTeleporter(Teleporter teleporter) {
		assert canHaveAsTeleporter(teleporter);
		this.teleporter = teleporter;
	}
	
	/**
	 * Checks whether this regular teleportation square can have the given 
	 * teleporter as its teleporter.
	 *
	 * @param teleporter
	 * The teleporter to check.
	 * @return
	 * True iff the given teleporter is effective
	 *   | result == (teleporter != null)
	 */
	@Raw
	public boolean canHaveAsTeleporter(Teleporter teleporter) {
		return (teleporter != null);
	}
	
    /** 
     * Return a destination of this regular teleportation square.
	 *
     * @return 
	 *   | result == (getTeleporter().getDestination())
     */
    public Square teleport() {
        return getTeleporter().getDestination();
	}

    /** 
     * Return a set of squares that can directly be navigated to 
     * from this square in a single step in one way or another.
     * 
     * @return 
	 * The result contains the teleportation destination
	 *   | result.contains(getTeleporter().getDestination())
     */
	@Override
    public Set<Square> getNavigatableSquares() {
		Set<Square> result = super.getNavigatableSquares();
		result.add(getTeleporter().getDestination());
		return result;
    }

	/**
	 * Variable registering the teleporter for this regular teleportation 
	 * square.
	 */
	private Teleporter teleporter;	
}
