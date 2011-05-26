package rpg;

import be.kuleuven.cs.som.annotate.*;
import java.util.Arrays;

import rpg.util.Direction;
import rpg.util.Temperature;


public class TransparentSquare extends SquareImpl {
    /** Initialize this new transparent square to a transparent square with 
     * the given temperature, temperature limits and humidity.
     * 
     * @param temperature
     * The temperature for this new transparent square.
     * @param minTemp
     * The minimum temperature for this new transparent square.
     * @param maxTemp
     * The maximum temperature for this new transparent square.
     * @param humidity
     * The humidity for this new transparent square.
     * @param doorDirections
     * A list of directions in which to place open doors.
     * @effect
     *   | super(temperature, minTemp, maxTemp, humidity,
     *   |             new TransparentBorderInitializer(doorDirections));
     */
    @Raw
    public TransparentSquare(Temperature temperature,
                Temperature minTemp, Temperature maxTemp, int humidity,
                Direction... doorDirections)
                                        throws IllegalArgumentException {
        super(temperature, minTemp, maxTemp, humidity,
                new TransparentBorderInitializer(doorDirections));
    }

    /** Initialize this new transparent square to a transparent square with 
     * the given temperature and humidity and a non slippery floor. 
     *
     * @param temperature
     * The temperature for this new transparent square.
     * @param humidity
     * The humidity for this new transparent square.
     * @param doorDirections
     * A list of directions in which to place open doors.
     * @effect
     *   | super(temperature, humidity,
     *   |          new RegularBorderInitializer(doorDirections));
     */
    @Raw
    public TransparentSquare(Temperature temperature, int humidity,
                            Direction... doorDirections) 
                                    throws IllegalArgumentException {
        super(temperature, humidity, 
                    new TransparentBorderInitializer(doorDirections));
    }

    /** 
     * Initialize this new transparent square to a default transparent 
     * square with doors in the given directions. 
     *
     * @effect
     *   | super(new TransparentBorderInitializer(doorDirections))
     */
    @Raw
    public TransparentSquare(Direction... doorDirections) 
                                    throws IllegalArgumentException {
        super(new TransparentBorderInitializer(doorDirections));
    }
    /** 
     * Initialize this new transparent square to a default transparent square. 
     *
     * @effect
     *   | super(new TransparentBorderInitializer(Direction.NORTH, 
     *   |                                        Direction.SOUTH))
     */
    @Raw
    public TransparentSquare() {
        super(new TransparentBorderInitializer(Direction.NORTH,
                                            Direction.SOUTH));
    }

    /** 
     * Check whether this transparent square can have the given border as 
     * its border in the given direction.
     * 
     * @param direction 
     * The direction of the border.
     * @param border
     * The border to check.
     * @return
     *   | result == (super.canPossiblyHaveAsBorderAt(direction, border)
     *   |                  &amp;&amp; (border == null || !border.isWall()))
     */
    @Raw
    @Override
    public boolean canHaveAsBorderAt(Direction direction, Border border) {
        return super.canPossiblyHaveAsBorderAt(direction, border)
                            && (border == null || !border.isWall());
    }

    /**
     * Checks whether the borders of this transparent square satisfy the 
     * constraints of the game.
     *
     * @return
     * True iff this transparent square is terminated or it is not 
     * terminated and has:
     *   - no walls
     *   - no doors placed in ceilings or floors
     *   - at least one door, and no more than two doors
     *   - both doors in complementary directions if it has two doors
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


    /** 
     * A default border initializer for transparent squares.
     *
     * This border initializer initializes squares with open borders 
     * everywhere, except for the north and south direction. They get an 
     * open door as border. 
     */
    static class TransparentBorderInitializer implements BorderInitializer {
        
        /** 
         * Create a new transparent border initializer that places open 
         * doors in the given directions
         * 
         * @param doorDirections 
         * A list of directions in which to place open doors.
         * @throws IllegalArgumentException
         * There are strictly less than one or strictly more than two doors, or 
         * there are two doors that are not in complementary directions.
         */
        TransparentBorderInitializer(Direction... doorDirections) 
                                            throws IllegalArgumentException {
            if (doorDirections == null)
                throw new IllegalArgumentException(
                        "Non effective doorDirections");
            if (doorDirections.length < 1  ||  doorDirections.length > 2)
                throw new IllegalArgumentException(
                        "Transparent square can only have one or two doors");
            if (doorDirections.length == 2  &&  
                    !doorDirections[0].equals(doorDirections[1].complement()))
                throw new IllegalArgumentException(
                        "Transparent square must have doors in complementary "
                                                            + "directions");
            this.doorDirections = doorDirections;
        }
        
        /** 
         * Initialize the borders of the given square. 
         * 
         * @param square 
         * The square whose borders to initialize.
         * @post
         * The given square has open borders everywhere, except for the 
         * directions given at creation time of this transparent border 
         * initializer, which contain open doors.
         */
        public void initializeBorders(@Raw SquareImpl square) {
            for (Direction direction : Direction.values()){
                Border border;
                if (Arrays.asList(doorDirections).contains(direction))
                    border = new Door(square, true);
                else
                    border = new OpenBorder(square);
                square.setBorderAt(direction, border);
            }
        }
        private Direction doorDirections[];
    }

    /**
     * Check if this transparent square can be used as an endpoint in a 
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
     * Check whether this transparent square satisfies all its class 
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

