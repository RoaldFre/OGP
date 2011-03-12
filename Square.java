import be.kuleuven.cs.som.annotate.*;

/**
 * A class of squares involving a temperature and a humidity.
 * XXX more info needed?
 *
 * @invar
 * The temperature boundaries of each square are legal.
 *   | areValidTemperatureBoundaries(getMinTemperature(), 
 *                                   getMaxTemperature())
 * @invar
 * The temperature of each square is legal for that square.
 *   | canHaveAsTemperature(getTemperature())
 *
 *
 * @author Roald Frederickx
 */

public class Square {

	/** 
	 * Initialize this new square to a square with the given temperature, 
	 * and temperature boundaries. 
	 * 
	 * XXX don't expose this one yet, as it isn't explicitly stated in 
	 * assignment? -- make private?
	 *
	 * @param temperature
	 * The temperature for this new square in degrees Celcius.
	 * @effect
	 * This new square is initialized with the given temperature as 
	 * its temperature, minTemp as its minimum temperature and maxTemp as 
	 * its maximum temperature.
	 *   | setTemperatureBoundaries(minTemp, maxTemp);
	 *   | setTemperature(temperature);
	 *
	 * XXX *constructor* ifv van effecten van meerdere *sequentiele* 
	 * *mutators*(?)
	 */
	@Raw
	public Square(double temperature, double minTemp, double maxTemp) 
			throws IllegalArgumentException {
		setTemperatureBoundaries(minTemp, maxTemp);
		setTemperature(temperature);
	}

	/** 
	 * Initialize this new square to a square with the given temperature, 
	 * and temperature boundaries. 
	 * The minimum temperature gets initialized to -200 and the maximum 
	 * temperature to 5000.
	 *
	 * @param temperature
	 * The temperature for this new square in degrees Celcius.
	 * @param min
	 * The lower temperature bound for this new square in degrees Celcius.
	 * @param max
	 * The upper temperature bound for this new square in degrees Celcius.
	 * @effect
	 * This new square is initialized with the given temperature as 
	 * its temperature, -200 as its minimum temperature and 5000 as 
	 * its maximum temperature.
	 *   | this(temperature, -200, 5000)
	 */
	@Raw
	public Square(double temperature) throws IllegalArgumentException {
		this(temperature, -200, 5000);
			//XXX cfr coding advice 9 p 68: toch bij declaratie 
			//initialiseren?
	}


	//TODO: Farenheit?

	/**
	 * Returns the temperature of this Square in degrees Celcius.
	 */
	@Basic @Raw
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Sets the temperature of this Square.
	 *
	 * @param temperature
	 * The new temperature in degrees Celcius.
	 * @post
	 * The new temperature of this square is equal to the given temperature
	 *   | new.getTemperature() == temperature
	 * @throws IllegalArgumentException
	 * This square can not have the given temperature.
	 *   | !canHaveAsTemperature(temperature)
	 */
	//XXX NOT @Raw -> OK?
	public void setTemperature(double temperature)
			throws IllegalArgumentException {
		if (! canHaveAsTemperature(temperature))
			throw new IllegalArgumentException();
		this.temperature = temperature;
	}

	/** 
	 * Checks whether the given temperature is valid for this Square. 
	 * 
	 * @param temperature
	 * The temperature of this square in degrees Celcius.
	 * @return
	 * True iff the given temperature is not strictly lower than the 
	 * minimum temperature for this square, and not strictly higher than 
	 * the maxmumum temperature for this square. 
	 *   | return == (getMinTemperature() &lt;= temperature)
	 *   |          &amp;&amp; (temperature &lt;= getMaxTemperature());
	 */
	public boolean canHaveAsTemperature(double temperature){
		return (getMinTemperature() <= temperature)
				&& (temperature <= getMaxTemperature());
	}
	
	/** 
	 * Variable registering the current temperature of this square.
	 */
	private double temperature;



	/**
	 * Returns the minimum temperature for this square in degrees Celcius.
	 */
	@Basic @Raw
	public double getMinTemperature() {
		return minTemperature;
	}
	
	/**
	 * Returns the maximum temperature for this square in degrees Celcius.
	 */
	@Basic @Raw
	public double getMaxTemperature() {
		return maxTemperature;
	}

	/** 
	 * Set the temperature boundaries for this square. 
	 * 
	 * @param min
	 * The lower temperature bound for this Square in degrees Celcius.
	 * @param max
	 * The upper temperature bound for this Square in degrees Celcius.
	 * @post
	 * The new maximum temperature for this Square is equal to the given 
	 * maximum.
	 *   | new.getMaxTemperature() == max
	 * @post
	 * The new minimum temperature for this Square is equal to the given 
	 * minimum.
	 *   | new.getMinTemperature() == min
	 * @throws IllegalArgumentException
	 * The given temperature boundaries are illegal for this square.
	 *   | ! areValidTemperatureBoundaries(min, max)
	 *
	 *   XXX current temperature can be out of bounds for the new boundaries!
	 *   well, we're raw anyway, so not that much of a deal?
	 *   or throw exception if boundaries not consistent with new 
	 *   temperature...?
	 */
	@Raw
	public void setTemperatureBoundaries(double min, double max)
			throws IllegalArgumentException {
		if (! areValidTemperatureBoundaries(min, max))
			throw new IllegalArgumentException();
		minTemperature = min;
		maxTemperature = max;
	}

	/** 
	 * Check whether the given boundaries are valid temperature boundaries.
	 * @param min
	 * The lower temperature bound for this Square in degrees Celcius.
	 * @param max
	 * The upper temperature bound for this Square in degrees Celcius.
	 * @return
	 * True iff the lower bound is less or equal than the upper bound.
	 *   | result == (min &lt;= max)
	 */
	public static boolean areValidTemperatureBoundaries(double min, double max){
		return min <= max;
	}

	/** 
	 * Variable registering the lower limit of the temperature of this 
	 * square. 
	 */
	private double minTemperature;
	/** 
	 * Variable registering the upper limit of the temperature of this 
	 * square. 
	 */
	private double maxTemperature;


	
	/** 
	 * Returns the cold damage associated with this square.
	 * 
	 * @return The damage points. One point for every 10 degrees the 
	 * temperature of this square is below -5 degrees Celcius, rounded 
	 * below.
	 *   | if (getTemperature() &gt; -5)
	 *   |     then return == 0
	 *   | else
	 *   |     return == (-5 - (int)getTemperature()) / 10
	 *   XXX can use cast in formal comment?
	 */
	public int coldDamage() {
		if (getTemperature() > -5)
			return 0;
		return 1 + (-5 - (int)getTemperature()) / 10;
	}
}

