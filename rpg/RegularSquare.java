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
        super(temperature, minTemp, maxTemp, humidity, 
                            new RegularBorderInitializer(hasSlipperyFloor));
    }

    /** 
     * Initialize this new regular square to a regular square with the 
     * given temperature and humidity and a non slippery floor. 
     *
     * @param temperature
     * The temperature for this new regular square.
     * @param humidity
     * The humidity for this new regular square.
     * @effect
     *   | super(temperature, humidity, false)
     */
    @Raw
    public RegularSquare(Temperature temperature, int humidity) 
                                    throws IllegalArgumentException {
        super(temperature, humidity, new RegularBorderInitializer(false));
    }

    /** 
     * Initialize this new regular square to a default regular square. 
     *
     * @effect
     *   | this(new Temperature(20), 5000);
     */
    @Raw
    public RegularSquare() {
        this(new Temperature(20), 5000);
    }


    /** 
     * A default border initializer for regular squares.
     *
     * This border initializer initializes squares with a 'wall' as a floor 
     * with the given 'slipperiness', and open borders everywhere else.
     */
    static class RegularBorderInitializer implements BorderInitializer {
        RegularBorderInitializer(boolean hasSlipperyFloor) {
            this.hasSlipperyFloor = hasSlipperyFloor;
        }
        public void initializeBorders(@Raw Square square) {
            for (Direction direction : Direction.values()){
                Border border;
                if (direction.equals(Direction.DOWN))
                    border = new Wall(square, hasSlipperyFloor);
                else
                    border = new OpenBorder(square);
                square.setBorderAt(direction, border);
            }
        }
        private boolean hasSlipperyFloor;
    }
}


// vim: ts=4:sw=4:expandtab:smarttab

