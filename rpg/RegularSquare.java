package rpg;

import rpg.util.Direction;
import rpg.util.Temperature;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class of regular squares involving a temperature, a humidity and a set 
 * of borders.
 * Each square also has as assocated cold damage, heat damage, rust damage, 
 * slipperiness and inhabitability.
 *
 * @author Roald Frederickx
 */

public class RegularSquare extends SquareImpl {
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
     *   | super(temperature, minTemp, maxTemp, humidity, 
     *   |              new RegularBorderInitializer(hasSlipperyFloor));
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
     *   | super(temperature, humidity, net RegularBorderInitializer(false))
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
     *   | super(new RegularBorderInitializer(false))
     */
    @Raw
    public RegularSquare() {
        super(new RegularBorderInitializer(false));
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
        public void initializeBorders(@Raw SquareImpl square) {
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


    /** 
     * Check whether this regular square can have the given border as its 
     * border in the given direction.
     * 
     * @param direction 
     * The direction of the border.
     * @param border
     * The border to check.
     * @return
     *   | result == super.canPossiblyHaveAsBorderAt(direction, border)
     */
    @Override
    public boolean canHaveAsBorderAt(Direction direction, Border border) {
        return super.canPossiblyHaveAsBorderAt(direction, border);
    }


    /**
     * Checks whether the borders of this regular square satisfy the 
     * constraints for regular squares.
     *
     * @return
     * True iff this regular square is not terminated and has:
     *   - no doors placed in ceilings or floors
     *   - at least one wall or door
     *   - no more than three doors
     */
    @Raw
    @Override
    public boolean bordersSatisfyConstraints() {
        if (isTerminated())
            return true;

        int numWallsOrDoors = 0;
        int numDoors = 0;
        for (Direction direction : Direction.values()){
            Border border = getBorderAt(direction);
            if (border.isDoor()) {
                numWallsOrDoors++;
                numDoors++;
                if (direction.equals(Direction.UP) 
                                    || direction.equals(Direction.DOWN))
                    return false;
            } else if (border.isWall())
                numWallsOrDoors++;
        }
        return (numWallsOrDoors >= 1  &&  numDoors <= 3);
    }

    /**
     * Check if this regular square can be used as an endpoint in a 
     * teleporter.
     *
     * @return
     *   | result == !isTerminated()
     */
    @Override
    public boolean isPossibleTeleportationEndPoint() {
        return !isTerminated();
    }

    /**
     * Check whether this regular square satisfies all its class 
     * invariants.
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
            && myAreaIsEquilibrated();
    }

}


// vim: ts=4:sw=4:expandtab:smarttab

