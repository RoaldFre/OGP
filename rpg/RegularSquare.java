package rpg;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of regular squares involving a temperature, a humidity and a set 
 * of borders.
 * Each square also has as assocated cold damage, heat damage, rust damage, 
 * slipperiness and inhabitability.
 *
 * @author Roald Frederickx
 */

public class RegularSquare extends Square {
    /** 
     * Initialize this new regular square to a regular square with the 
     * given temperature, temperature limits, humidity and slipperiness of 
     * the floor. 
     * 
     * @param temperature
     * The temperature for this new regular square.
     * @param minTemp
     * The minimum temperature for this new regular square.
     * @param maxTemp
     * The maximum temperature for this new regular square.
     * @param humidity
     * The humidity for this new regular square.
     * @param hasSlipperyFloor
     * Whether or not this new regular square has a slippery floor.
     * @effect
     *   | super(temperature, minTemp, maxTemp, humidity, hasSlipperyFloor);
     */
    @Raw
    public RegularSquare(Temperature temperature,
                    Temperature minTemp, Temperature maxTemp,
                    int humidity, boolean hasSlipperyFloor)
                                            throws IllegalArgumentException {
        super(temperature, minTemp, maxTemp, humidity, hasSlipperyFloor);
    }

    /** 
     * Initialize this new regular square to a regular square with the 
     * given temperature and humidity. 
     *
     * @param temperature
     * The temperature for this new regular square.
     * @param humidity
     * The humidity for this new regular square.
     * @effect
     *   | super(temperature, humidity)
     */
    @Raw
    public RegularSquare(Temperature temperature, int humidity) 
                                    throws IllegalArgumentException {
        super(temperature, humidity);
    }

    /** 
     * Initialize this new regular square to a default regular square. 
     *
     * @effect
     *   | super();
     */
    @Raw
    public RegularSquare() {
        super();
    }
}


// vim: ts=4:sw=4:expandtab:smarttab

