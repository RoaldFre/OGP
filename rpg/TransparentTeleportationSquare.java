package rpg;

import be.kuleuven.cs.som.annotate.*;
import java.util.Set;

import rpg.util.Temperature;

/**
 * @invar
 * Each transparent teleportation square can have its teleporter as its teleporter.
 *   | isValidTeleporter(getTeleporter())
 *
 * @author Roald Frederickx
 */
public class TransparentTeleportationSquare
							extends TransparentSquare
							implements TeleportationSquare {
    /** 
	 * Initialize this new transparent teleportation square to a 
	 * transparent teleportation square with the given temperature, 
	 * temperature limits, humidity and teleporter. 
     * 
     * @param temperature
	 * The temperature for this new transparent teleportation square.
	 * @param minTemp
	 * The minimum temperature for this new transparent teleportation square.
	 * @param maxTemp
	 * The maximum temperature for this new transparent teleportation square.
	 * @param humidity
	 * The humidity for this new transparent teleportation square.
	 * floor.
	 * @param teleporter
	 * The teleporter for this new transparent teleportation square.
     * @effect
     *   | super(temperature, minTemp, maxTemp, humidity)
	 * @effect
	 *   | setTeleporter(teleporter)
     */
    @Raw
    public TransparentTeleportationSquare(Temperature temperature,
                    Temperature minTemp, Temperature maxTemp,
                    int humidity, Teleporter teleporter)
                                            throws IllegalArgumentException {
        super(temperature, minTemp, maxTemp, humidity);
		setTeleporter(teleporter);
    }

    /** 
	 * Initialize this new transparent teleportation square to a 
	 * transparent teleportation square with the given temperature, 
	 * humidity and teleporter.
     *
     * @param temperature
     * The temperature for this new transparent teleportation square.
     * @param humidity
     * The humidity for this new transparent teleportation square.
	 * @param teleporter
	 * The teleporter for this new transparent teleportation square.
     * @effect
     *   | super(temperature, humidity)
	 * @effect
	 *   | setTeleporter(teleporter)
     */
    @Raw
    public TransparentTeleportationSquare(Temperature temperature, int humidity,
									Teleporter teleporter) 
                                    throws IllegalArgumentException {
        super(temperature, humidity);
		setTeleporter(teleporter);
    }

    /** 
	 * Initialize this new transparent teleportation square to a default 
	 * transparent teleportation square with the given teleporter.
     *
	 * @param teleporter
	 * The teleporter for this new transparent teleportation square.
     * @effect
     *   | super()
	 * @effect
	 *   | setTeleporter(teleporter)
     */
    @Raw
    public TransparentTeleportationSquare(Teleporter teleporter) {
        super();
		setTeleporter(teleporter);
    }


	/**
	 * Return the teleporter for this transparent teleportation square.
	 */
	@Basic @Raw
	public Teleporter getTeleporter() {
		return teleporter;
	}
	
	/**
	 * Set the teleporter for this transparent teleportation square to the 
	 * given teleporter.
	 *
	 * @param teleporter
	 * The new teleporter for this transparent teleportation square.
	 * @pre
	 * The given teleporter must be a valid teleporter for this transparent 
	 * teleportation square.
	 *   | isValidTeleporter(teleporter)
	 * @post
	 * The new teleporter for this transparent teleportation square is equal to 
	 * the given teleporter.
	 *   | new.getTeleporter() == teleporter
	 */
	@Raw
	public void setTeleporter(Teleporter teleporter) {
		assert isValidTeleporter(teleporter);
		this.teleporter = teleporter;
	}
	
	/**
	 * Checks whether the given teleporter is a valid teleporter for all 
	 * transparent teleportation squares.
	 *
	 * @param teleporter
	 * The teleporter to check.
	 * @return
	 * True iff the given teleporter is effective
	 *   | result == (teleporter != null)
	 */
	@Raw
	public static boolean isValidTeleporter(Teleporter teleporter) {
		return (teleporter != null);
	}
	
    /** 
     * Return a destination of this transparent teleportation square.
	 *
     * @return 
	 *   | result == (getTeleporter().getDestination())
     */
    public Square teleport() {
        return getTeleporter().teleport();
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
		result.addAll(getTeleporter().getDestinations());
		return result;
    }

	/**
	 * Variable registering the teleporter for this transparent teleportation 
	 * square.
	 */
	private Teleporter teleporter;	

    /**
	 * Check whether this transparent teleportation square satisfies all its 
	 * class invariants.
     */
    @Raw
    @Override
    public boolean isNotRaw() {
        return super.isNotRaw()
            && isValidMergeTemperatureWeight(getMergeTemperatureWeight()) 
            && isValidHeatDamageThreshold(getHeatDamageThreshold()) 
            && isValidHeatDamageStep(getHeatDamageStep()) 
            && matchesMinTemperatureMax(getMinTemperature(),
                                    getTemperature(), getMaxTemperature())
            && canHaveAsHumidity(getHumidity()) 
            && hasProperBorders()
            && bordersSatisfyConstraints()
            && hasNoDuplicateBorders()
            && myAreaIsEquilibrated()
 			&& isValidTeleporter(getTeleporter());
    }
}
