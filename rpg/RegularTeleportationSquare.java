package rpg;

import be.kuleuven.cs.som.annotate.*;
import java.util.Set;

import rpg.util.Temperature;

/**
 * @invar
 * Each regular teleportation square can have its teleporter as its teleporter.
 *   | isValidTeleporter(getTeleporter())
 *
 * @author Roald Frederickx
 */
public class RegularTeleportationSquare
                            extends RegularSquare
                            implements TeleportationSquare {
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
     * teleportation square with the given temperature, humidity and 
     * teleporter.
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
     * regular teleportation square with the given teleporter.
     *
     * @param teleporter
     * The teleporter for this new regular teleportation square.
     * @effect
     *   | super()
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
     *   | isValidTeleporter(teleporter)
     * @post
     * The new teleporter for this regular teleportation square is equal to 
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
     * regular teleportation squares.
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
     * Return a destination of this regular teleportation square.
     *
     * @return 
     *   | result == (getTeleporter().teleport())
     */
    public Square teleport() {
        return getTeleporter().teleport();
    }

    /** 
     * Return a set of squares that can directly be navigated to 
     * from this square in a single step in one way or another.
     * 
     * @return 
     * The result contains the teleportation destinations
     *   | for each destination in getTeleporter().getDestinations() :
     *   |      result.contains(destination)
     */
    @Override
    public Set<Square> getNavigatableSquares() {
        Set<Square> result = super.getNavigatableSquares();
        result.addAll(getTeleporter().getDestinations());
        return result;
    }

    /**
     * Variable registering the teleporter for this regular teleportation 
     * square.
     */
    private Teleporter teleporter;  

    /**
     * Check whether this regular teleportation square satisfies all its 
     * class invariants.
     */
    @Raw
    @Override
    public boolean isNotRaw() {
        return super.isNotRaw()
            && isValidTeleporter(getTeleporter());
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

