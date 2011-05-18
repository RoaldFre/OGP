package rpg;

import be.kuleuven.cs.som.annotate.*;
import java.util.Collection;

public class Rock extends Square {

    /** 
     * Initialize this new rock with the given temperature boundaries, a 
     * temperature of 0 degrees C and a humidity of 0%.
     * 
     * @param minTemp
     * The mimimum temperature for this new rock.
     * @param maxTemp
     * The maximum temperature for this new rock.
     * @effect
     *   | super(new Temperature(0), minTemp, maxTemp, 0,
     *   |                                  new RockBorderInitializer());
     */
    public Rock(Temperature minTemp, Temperature maxTemp) 
                                            throws IllegalArgumentException {
        super(new Temperature(0), minTemp, maxTemp, 0, 
                                            new RockBorderInitializer());
    }

    /** 
     * Initialize this new rock with the default temperature boundaries, a 
     * temperature of 0 degrees C and a humidity of 0%.
     * 
     * @param minTemp
     * The mimimum temperature for this new rock.
     * @param maxTemp
     * The maximum temperature for this new rock.
     * @effect
     *   | super(new Temperature(0), 0, new RockBorderInitializer());
     */
    public Rock() {
        super(new Temperature(0), 0, new RockBorderInitializer());
    }


    /** 
     * Check whether this rock can have the given border as its border in 
     * the given direction.
     * 
     * @param direction 
     * The direction of the border.
     * @param border
     * The border to check.
     * @return
	 * True iff the given direction is valid and:
	 *   - this rock is terminated and the given border is null;
	 *   or
	 *   - this rock is not terminated and the given border is a wall and 
	 *   not null nor terminated.
     *   | if (!isValidDirection(direction))
     *   |      then result == false
     *   | else if (isTerminated())
     *   |      then result == (border == null)
     *   | else
     *   |      result == (border != null 
     *   |                  &amp;&amp; !border.isTerminated()
     *   |                  &amp;&amp; (border == null || border.isWall()))
     */
    @Raw
	@Override
    public boolean canHaveAsBorderAt(Direction direction, Border border) {
        return super.canPossiblyHaveAsBorderAt(direction, border)
                                && (border == null || border.isWall());
    }


    /** 
     * Checks whether the borders of this rock satisfy the constraints for 
     * rocks.
     * 
     * @return 
     *   | result == true
     */
    @Override
    public boolean bordersSatisfyConstraints() {
        return true;
    }


    /** 
     * Checks whether this rock can have the given humidity as its 
     * humidity. 
     * 
     * @param humidity
     * The humidity to check.
     * @return 
     * True iff the given humidity is equal to 0.
     *   | result == (humidity == 0)
     */
	@Override
    public boolean canHaveAsHumidity(int humidity) {
        return humidity == 0;
    }
	
    /** 
     * Checks whether the given temperature is valid for this square. 
     * 
     * @param temperature
     * The temperature of this square.
     * @return
     * True iff the given temperature is the proper temperature for this 
     * rock.
     *   | result == temperature.equals(getProperTemperature())
     */
    @Override
    public boolean canHaveAsTemperature(Temperature temperature){
        return temperature.equals(getProperTemperature());
    }


    /** 
     * Function signalling that one of the neighbours of this rock has 
     * changed its temperature or humidity.
     * This function will <i>only</i> be called if this rock is bordering 
     * an area that has just been equilibrated. It will <i>not</i> get 
     * called for every temperature and/or humidity change of the squares 
     * in an area that get equilibrated.
     *
     * @effect
     *   | setProperTemperature()
     */
	@Override @Raw
    protected void neighbourHasChangedTemperatureOrHumidity() {
        setProperTemperature();
    }

    /** 
     * Sets the proper temperature of this rock. 
     *
     * @effect
     *   | setTemperatureRaw(getProperTemperature())
     */
    protected void setProperTemperature() {
        setTemperatureRaw(getProperTemperature());
    }

    /** 
     * Get the proper temperature of this rock.
     *
     * @return
     * The average temperature of each neighbouring non-rock, coerced to 
     * the temperature limits of this rock.
     */
    protected Temperature getProperTemperature() {
        Collection<Square> neighbours = 
                getFilteredNeighbours(acceptNonRockNeighbourFilter).values();
        if (neighbours.isEmpty())
            return new Temperature(0);
            
        double temperatureSum = 0;
        for (Square neighbour : neighbours)
            temperatureSum += neighbour.getTemperature().temperature();
        Temperature newTemperature = new Temperature(temperatureSum
                                                        / neighbours.size());
        return newTemperature.coerce(getMinTemperature(), getMaxTemperature());
    }
        
    static final protected NeighbourFilter acceptNonRockNeighbourFilter =
                new NeighbourFilter() {
                    public boolean filter(Square s, Border b, Square neighb){
                        return !(neighb instanceof Rock);
                    }
                };

    /** 
     * A default border initializer for rocks.
     *
     * This border initializer initializes squares with non-slippery walls 
     * as borders in every direction.
     */
    static class RockBorderInitializer implements BorderInitializer {
        public void initializeBorders(@Raw Square square) {
            for (Direction direction : Direction.values())
                square.setBorderAt(direction, new Wall(square, false));
        }
    }

    


}

// vim: ts=4:sw=4:expandtab:smarttab

