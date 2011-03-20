import be.kuleuven.cs.som.annotate.*;


/**
 * A class of squares involving a temperature and a humidity.
 * XXX more info needed?
 *
 * @invar
 * The combination of the temperature with the minimum and maximum 
 * temperature of each square is legal.
 *   | matchesMinTemperatureMax(getMinTemperature(), getTemperature(),
 *   | 										getMaxTemperature())
 * @invar
 * Each square can have its heat damage threshold temperature as its heat 
 * damage threshold temperature.
 *   | canHaveAsHeatDamageThreshold(getHeatDamageThreshold()) 
 * @invar
 * The heat damage threshold temperature that applies to all squares 
 * must be a valid heat damage threshold temperature.
 *   | isValidHeatDamageThreshold(getHeatDamageThreshold()) 
 * @invar
 * The heat damage temperature step that applies to all squares must be 
 * a valid heat damage temperature step.
 *   | isValidHeatDamageStep(getHeatDamageStep()) 
 *
 * @author Roald Frederickx
 */

public class Square {
	//TODO: check op null zijn van temperatures

	/** 
	 * Initialize this new square to a square with the given temperature, 
	 * and temperature limits. 
	 * 
	 * XXX don't expose this one yet, as it isn't explicitly stated in 
	 * assignment? -- make private? (can we do that?)
	 *
	 * @param temperature
	 * The temperature for this new square.
	 * @param minTemp 
	 * The minimum temperature for this new square.
	 * @param maxTemp 
	 * The maximum temperature for this new square.
	 * @param heatDamageThreshold
	 * The heat damage threshold for this new square.
	 * @param heatDamageStep
	 * The heat damage step for this new square.
	 * @post
	 * The minimum temperature for this new square is equal to the 
	 * given minimum temperature.
	 *   | new.getMinTemperature() == minTemp
	 * @post
	 * The temperature for this new square is equal to the given 
	 * temperature.
	 *   | new.getTemperature() == temperature
	 * @post
	 * The maximum temperature for this new square is equal to the 
	 * given maximum temperature.
	 *   | new.getMaxTemperature() == maxTemp
	 * @effect
	 * The heat damage threshold for this new square gets initialized to 
	 * the given heat damage threshold.
	 *   | setHeatDamageThreshold(heatDamageThreshold)
	 * @effect
	 * The heat damage step for this new square gets initialized to 
	 * the given heat damage step.
	 *   | setHeatDamageStep(heatDamageStep)
	 * @throws IllegalArgumentException
	 * Some of the given temperatures values are not effective, or the 
	 * given temperature does not match with the given temperature limits.
	 *   | ! matchesMinTemperatureMax(minTemp, temperature, maxTemp)
	 */
	@Raw
	public Square(Temperature temperature,
				Temperature minTemp, Temperature maxTemp,
				Temperature heatDamageThreshold, double heatDamageStep)
						throws IllegalArgumentException {
		if (minTemp == null || maxTemp == null)
			throw new IllegalArgumentException();
		minTemperature = minTemp;
		maxTemperature = maxTemp;
		setTemperature(temperature);
		setHeatDamageThreshold(heatDamageThreshold);
		setHeatDamageStep(heatDamageStep);
	}

	/** 
	 * Initialize this new square to a square with the given temperature, 
	 * and temperature limits. 
	 *
	 * @param temperature
	 * The temperature for this new square.
	 * @effect
	 * This new square is initialized with the given temperature as 
	 * its temperature, -200C as its minimum temperature and 5000C as 
	 * its maximum temperature. The heat damage threshold and step are 
	 * 35C and 15C, respectively.
	 *   | this(temperature, new Temperature(-200), new Temperature(5000),
	 *   |		new Temperature(35), 15)
	 */
	@Raw
	public Square(Temperature temperature) throws IllegalArgumentException {
		this(temperature, new Temperature(-200), new Temperature(5000),
				new Temperature(35), 15);
	}

	/**
	 * Returns the temperature of this square.
	 */
	@Basic @Raw
	public Temperature getTemperature() {
		return temperature;
	}

	/**
	 * Sets the temperature of this square.
	 *
	 * @param temperature
	 * The new temperature.
	 * @post
	 * The new temperature of this square is equal to the given temperature
	 *   | new.getTemperature() == temperature
	 * @throws IllegalArgumentException
	 * This square can not have the given temperature.
	 *   | !canHaveAsTemperature(temperature)
	 */
	@Raw
	public void setTemperature(Temperature temperature)
			throws IllegalArgumentException {
		if (! canHaveAsTemperature(temperature))
			throw new IllegalArgumentException();
		this.temperature = temperature;
	}

	/** 
	 * Checks whether the given temperature is valid for this square. 
	 * 
	 * @param temperature
	 * The temperature of this square.
	 * @return
	 * True iff the given temperature is effective, not strictly lower than 
	 * the minimum temperature for this square, and not strictly higher 
	 * than the maxmumum temperature for this square.
	 *   | result == matchesMinTemperatureMax(getMinTemperature(), 
	 *   | 								temperature, getMaxTemperature());
	 */
	public boolean canHaveAsTemperature(Temperature temperature){
		return matchesMinTemperatureMax(getMinTemperature(), temperature,
										getMaxTemperature());
	}
	
	/** 
	 * Variable registering the current temperature of this square.
	 */
	private Temperature temperature;



	/**
	 * Returns the minimum temperature for this square.
	 */
	@Basic @Raw
	public Temperature getMinTemperature() {
		return minTemperature;
	}
	
	/**
	 * Returns the maximum temperature for this square.
	 */
	@Basic @Raw
	public Temperature getMaxTemperature() {
		return maxTemperature;
	}

	/** 
	 * Set the minimum temperature for this square. 
	 * 
	 * @param min
	 * The minimum temperature for this square.
	 * @post
	 * The new minimum temperature for this square is equal to the given 
	 * minimum.
	 *   | new.getMinTemperature() == min
	 * @throws IllegalArgumentException
	 * The given minimum temperature is illegal for this square.
	 *   | ! canHaveAsMinTemperature(min)
	 */
	@Raw
	public void setMinTemperature(Temperature min)
			throws IllegalArgumentException {
		if (! canHaveAsMinTemperature(min))
			throw new IllegalArgumentException();
		minTemperature = min;
	}

	/** 
	 * Set the maximum temperature for this square. 
	 * 
	 * @param max
	 * The maximum temperature for this square.
	 * @post
	 * The new maximum temperature for this square is equal to the given 
	 * maximum.
	 *   | new.getMaxTemperature() == max
	 * @throws IllegalArgumentException
	 * The given maximum temperature is illegal for this square.
	 *   | ! canHaveAsMaxTemperature(max)
	 */
	@Raw
	public void setMaxTemperature(Temperature max)
			throws IllegalArgumentException {
		if (! canHaveAsMaxTemperature(max))
			throw new IllegalArgumentException();
		maxTemperature = max;
	}


	/**
	 * Checks whether this square can have the given minimum temperature as 
	 * its minimum temperature.
	 * @param min
	 * The minimum temperature.
	 * @return 
	 * True iff the given minimum temperature is consistent with the 
	 * current temperature and the current maximum temperature.
	 *   | result == matchesMinTemperatureMax(min, getTemperature(), 
	 *   | 								getMaxTemperature())
	 */
	public boolean canHaveAsMinTemperature(Temperature min) {
		return matchesMinTemperatureMax(min, getTemperature(),
											getMaxTemperature());
	}

	/**
	 * Checks whether this square can have the given maximum temperature as 
	 * its maximum temperature.
	 * @param max
	 * The maximum temperature.
	 * @return 
	 * True iff the given maximum temperature is consistent with the 
	 * current temperature and the current maximum temperature.
	 *   | result == matchesMinTemperatureMax(getMinTemperature(),
	 *   | 								getTemperature(), min)
	 */
	public boolean canHaveAsMaxTemperature(Temperature max) {
		return matchesMinTemperatureMax(getMinTemperature(),
										getTemperature(), max);
	}

	/**
	 * Check whether the given temperature matches with the given 
	 * temperature limits and all given values are effective.
	 *
	 * @param minTemperature 
	 * The minimum temperature.
	 * @param temperature 
	 * The acutual temperature.
	 * @param maxTemperature 
	 * The maximum temperature.
	 * @return 
	 * True iff all temperatures are effective and the given temperature 
	 * lays between the given temperature limits.
	 *   | result == minTemperature != null 
	 *   |			&amp;&amp; temperature != null
	 *   |			&amp;&amp; maxTemperature != null
	 *   |			&amp;&amp; minTemperature.compareTo(temperature) &lt;= 0
	 *   |			&amp;&amp; temperature.compareTo(maxTemperature) &lt;= 0;
	 */
	public static boolean matchesMinTemperatureMax(Temperature minTemperature,
					Temperature temperature, Temperature maxTemperature) {
		return minTemperature != null 
				&& temperature != null
				&& maxTemperature != null
				&& minTemperature.compareTo(temperature) <= 0
				&& temperature.compareTo(maxTemperature) <= 0;
	}

	/** 
	 * Variable referencing the minimum temperature of this square. 
	 */
	private Temperature minTemperature;
	/** 
	 * Variable referencing the maximum temperature of this square. 
	 */
	private Temperature maxTemperature;


	
	/** 
	 * Returns the cold damage associated with this square.
	 * 
	 * @return The damage points. One point for every 10 degrees the 
	 * temperature of this square is below -5 degrees Celcius, rounded 
	 * below.
	 *   | if (getTemperature() &gt; -5)
	 *   |     then result == 0
	 *   | else
	 *   |     result == 1 + (int)((-5 - getTemperature()) / 10)
	 *   XXX can use cast in formal comment?
	 */
	public int coldDamage() {
		double temp = getTemperature().temperature();
		if (temp > -5)
			return 0;
		return 1 + (int)((-5 - temp) / 10);
	}


	/** 
	 * Returns the heat damage associated with this square.
	 * 
	 * @return The damage points. One point for every "heat damage step" 
	 * degrees the temperature of this square is above the heat damage 
	 * threshold, rounded below.
	 *   XXX formal definition...
	 */
	public int heatDamage() {
		if (getTemperature().compareTo(heatDamageThreshold) < 0)
			return 0;
		double temp = getTemperature().temperature();
		double threshold = getHeatDamageThreshold().temperature();
		return 1 + (int)((temp - threshold) / getHeatDamageStep());
	}



	/**
	 * Returns the heat damage threshold temperature that applies to all 
	 * squares.
	 */
	@Basic @Raw
	public static Temperature getHeatDamageThreshold() {
		return heatDamageThreshold;
	}
	
	/**
	 * Set the heat damage threshold temperature that applies to all 
	 * squares to the given heat damage threshold temperature.
	 *
	 * @param heatDamageThreshold
	 * The new heat damage threshold temperature for all squares. 
	 * @post
	 * The new heat damage threshold temperature for all squares is equal 
	 * to the given heat damage threshold temperature.
	 *   | new.getHeatDamageThreshold() == heatDamageThreshold
	 * @throws IllegalArgumentException
	 * The given heat damage threshold temperature is not valid heat damage 
	 * threshold temperature for a square
	 *   | ! isValidHeatDamageThreshold(heatDamageThreshold) 
	 */
	@Raw
	public static void setHeatDamageThreshold(Temperature heatDamageThreshold) 
										throws IllegalArgumentException {
		if (!isValidHeatDamageThreshold(heatDamageThreshold))
			throw new IllegalArgumentException();
		Square.heatDamageThreshold = heatDamageThreshold;
	}
	
	/**
	 * Checks whether the given heat damage threshold temperature is a 
	 * valid heat damage threshold temperature for all squares.
	 *
	 * @param heatDamageThreshold
	 * The heat damage threshold temperature to check.
	 * @return
	 * True if and only if the given heat damage threshold temperature is 
	 * effective.
	 *   | result == (heatDamageThreshold != null)
	 */
	public static boolean isValidHeatDamageThreshold(Temperature 
												heatDamageThreshold) {
		return heatDamageThreshold != null; 
	}
	
	/**
	 * Variable registering the heat damage threshold temperature that 
	 * applies to all squares.
	 */
	private static Temperature heatDamageThreshold;







	/**
	 * Returns the heat damage temperature step that applies to all squares.
	 */
	@Basic @Raw
	public static double getHeatDamageStep() {
		return heatDamageStep;
	}
	
	/**
	 * Set the heat damage temperature step that applies to all squares to 
	 * the given heat damage temperature step.
	 *
	 * @param heatDamageStep
	 * The new heat damage temperature step for all squares. 
	 * @post
	 * The new heat damage temperature step for all squares is equal to the 
	 * given heat damage temperature step.
	 *   | new.getHeatDamageStep() == heatDamageStep
	 * @throws IllegalArgumentException
	 * The given heat damage temperature step is not valid heat damage 
	 * temperature step for a square
	 *   | ! isValidHeatDamageStep(heatDamageStep) 
	 */
	@Raw
	public static void setHeatDamageStep(double heatDamageStep) 
				throws IllegalArgumentException {
		if (!isValidHeatDamageStep(heatDamageStep))
			throw new IllegalArgumentException();
		Square.heatDamageStep = heatDamageStep;
	}
	
	/**
	 * Checks whether the given heat damage temperature step is a valid 
	 * heat damage temperature step for all squares.
	 *
	 * @param heatDamageStep
	 * The heat damage temperature step to check.
	 * @return
	 * True if and only if the given heat damage step is strictly positive.
	 *   | result == (heatDamageStep &gt; 0)
	 */
	public static boolean isValidHeatDamageStep(double heatDamageStep) {
		return heatDamageStep > 0;
	}
	
	/**
	 * Variable registering the heat damage temperature step for this 
	 * square, expressed in a scale that is compatible to the Celcius or 
	 * Kelvin scale.
	 * Note that this is not an actual <i>temperature</i>, but merely a 
	 * temperature <i>difference</i>. Hence we use a double that is 
	 * compatible with the Celcius scale, and not an actual Temperature.
	 */
	//We could abstract this further by defining a TemperatureDifference 
	//analogous to Temperature to work with different temperature scales, 
	//but that seems to be overkill for this assignment.
	private static double heatDamageStep;
}

