import be.kuleuven.cs.som.annotate.*;


/**
 * A class of squares involving a temperature and a humidity.
 * XXX more info needed?
 *
 * @invar
 * The combination of the temperature witch the minimum and maximum 
 * temperature of each square is legal.
 *   | matchesMinTemperatureMax(getMinTemperature(), getTemperature(),
 *   | 									getMaxTemperature())
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
	 * The lower temperature limit for this new square.
	 * @param maxTemp 
	 * The upper temperature limit for this new square.
	 * @post
	 * The minimum temperature for this new square is equal to the 
	 * given minimum temperature.
	 *   | new.getMinTemperature() == minTemp
	 * @post
	 * The maximum temperature for this new square is equal to the 
	 * given maximum temperature.
	 *   | new.getMaxTemperature() == maxTemp
	 * @effect
	 * The temperature for this new square gets initialized to the given 
	 * temperature (after having set the temperature limits).
	 *   | setTemperature(temperature);
	 * @throws IllegalArgumentException
	 * One of the temperature limits is not effective.
	 *   | minTemp == null || maxTemp == null
	 *
	 * XXX eerst moeten de posts gebeuren alvorens je de effect tag kan/mag 
	 * gebruiken (omdat die de min en max temp nodig heeft!)
	 * effect kan ook worden uitgeschreven ifv een post en een throws 
	 * (indien !matchesMinTemperatureMax(...)), maar da's duplicatie van 
	 * specificaties...
	 *
	 *
	 *
	 * anderzijds:
	 * XXX *constructor* ifv van effecten van meerdere *sequentiele* 
	 * *mutators*(?)
	 * vb:
	 * @effect
	 *   | setTemperatureLimits(minTemp, maxTemp);
	 *   | setTemperature(temperature);
	 */
	@Raw
	public Square(Temperature temperature, Temperature minTemp,
				Temperature maxTemp) throws IllegalArgumentException {
		if (minTemp == null || maxTemp == null)
			throw new IllegalArgumentException();
		minTemperature = minTemp;
		maxTemperature = maxTemp;
		setTemperature(temperature);
	}

	/** 
	 * Initialize this new square to a square with the given temperature, 
	 * and temperature limits. 
	 * The minimum temperature gets initialized to -200C and the maximum 
	 * temperature to 5000C.
	 *
	 * @param temperature
	 * The temperature for this new square.
	 * @effect
	 * This new square is initialized with the given temperature as 
	 * its temperature, -200C as its minimum temperature and 5000C as 
	 * its maximum temperature.
	 *   | this(temperature, -200, 5000)
	 */
	@Raw
	public Square(Temperature temperature) throws IllegalArgumentException {
		this(temperature, new Temperature(-200), new Temperature(5000));
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
	 * True iff the given temperature is not strictly lower than the 
	 * minimum temperature for this square, and not strictly higher than 
	 * the maxmumum temperature for this square. 
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
	 * temperature limits and are effective.
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
	 * Check whether the given limits are valid temperature limits.
	 * @param min
	 * The lower temperature limit for this square.
	 * @param max
	 * The upper temperature limit for this square.
	 * @return
	 * True iff the lower limit is less or equal than the upper limit.
	 *   | result == (min != null
	 *   |			&amp;&amp; max != null
	 *   |			&amp;&amp; min.compareTo(max) &lt;= 0)
	 */
	public static boolean areValidTemperatureLimits(Temperature min,
													Temperature max) {
		return min != null && max != null && min.compareTo(max) <= 0;
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
	 *   |     result == (-5 - (int)getTemperature()) / 10
	 *   XXX can use cast in formal comment?
	 */
	public int coldDamage() {
//		if (getTemperature() > -5)
			return 0;
		//return 1 + (-5 - (int)getTemperature()) / 10;
	}
}

