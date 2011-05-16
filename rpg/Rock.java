package rpg;

import be.kuleuven.cs.som.annotate.*;

//INVAR: temp is *steeds* gemiddelde van omringende vakjes! (of nul)

//TODO specs
public class Rock extends Square {

    public Rock(Temperature minTemp, Temperature maxTemp) {
        initializeBorders();
        setHumidity(0);
        setMinTemperatureRaw(minTemp);
        setMaxTemperatureRaw(maxTemp);
        //TODO: temperature
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
     *   |                      &amp;&amp; !border.isTerminated()
     *   |                      &amp;&amp; border.isWall())
     */
    @Raw
	@Override
    public boolean canHaveAsBorderAt(Direction direction, Border border) {
        if (!isValidDirection(direction))
            return false;
        if (isTerminated())
            return border == null;
        return (border != null) 
					&& (!border.isTerminated())
					&& border.isWall();
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
	
	//TODO Temperature




    /** 
     * Initialize the borders of this square.
     *
     * @post
     * The new rock has non slippery walls all around.
     */
    @Raw @Model
    private void initializeBorders() {
        for (Direction direction : Direction.values())
            setBorderAt(direction, new Wall(this, false));
    }


}
