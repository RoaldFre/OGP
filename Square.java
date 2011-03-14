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

	/** 
	 * Initialize this new square to a square with the given temperature, 
	 * and temperature limits. 
	 * 
	 * XXX don't expose this one yet, as it isn't explicitly stated in 
	 * assignment? -- make private? (can we do that?)
	 *
	 * @param temperature
	 * The temperature for this new square in degrees Celcius.
	 * @param minTemp 
	 * The lower temperature limit for this new square in degrees Celcius.
	 * @param maxTemp 
	 * The upper temperature limit for this new square in degrees Celcius.
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
	 * temperature.
	 *   | setTemperature(temperature);
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
	public Square(double temperature, double minTemp, double maxTemp) 
			throws IllegalArgumentException {
		minTemperature = minTemp;
		maxTemperature = maxTemp;
		setTemperature(temperature);
	}

	/** 
	 * Initialize this new square to a square with the given temperature, 
	 * and temperature limits. 
	 * The minimum temperature gets initialized to -200 and the maximum 
	 * temperature to 5000.
	 *
	 * @param temperature
	 * The temperature for this new square in degrees Celcius.
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
	//enum

	/**
	 * Returns the temperature of this square in degrees Celcius.
	 */
	@Basic @Raw
	public double getTemperature() {
		return temperature;
	}

	/**
	 * Sets the temperature of this square.
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
	@Raw
	public void setTemperature(double temperature)
			throws IllegalArgumentException {
		if (! canHaveAsTemperature(temperature))
			throw new IllegalArgumentException();
		this.temperature = temperature;
	}

	/** 
	 * Checks whether the given temperature is valid for this square. 
	 * 
	 * @param temperature
	 * The temperature of this square in degrees Celcius.
	 * @return
	 * True iff the given temperature is not strictly lower than the 
	 * minimum temperature for this square, and not strictly higher than 
	 * the maxmumum temperature for this square. 
	 *   | result == matchesMinTemperatureMax(getMinTemperature(), 
	 *   | 								temperature, getMaxTemperature());
	 */
	public boolean canHaveAsTemperature(double temperature){
		return matchesMinTemperatureMax(getMinTemperature(), temperature,
										getMaxTemperature());
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
	 * Set the minimum temperature for this square. 
	 * 
	 * @param min
	 * The minimum temperature for this square in degrees Celcius.
	 * @post
	 * The new minimum temperature for this square is equal to the given 
	 * minimum.
	 *   | new.getMinTemperature() == min
	 * @throws IllegalArgumentException
	 * The given minimum temperature is illegal for this square.
	 *   | ! canHaveAsMinTemperature(min)
	 */
	@Raw
	public void setMinTemperature(double min)
			throws IllegalArgumentException {
		if (! canHaveAsMinTemperature(min))
			throw new IllegalArgumentException();
		minTemperature = min;
	}

	/** 
	 * Set the maximum temperature for this square. 
	 * 
	 * @param max
	 * The maximum temperature for this square in degrees Celcius.
	 * @post
	 * The new maximum temperature for this square is equal to the given 
	 * maximum.
	 *   | new.getMaxTemperature() == max
	 * @throws IllegalArgumentException
	 * The given maximum temperature is illegal for this square.
	 *   | ! canHaveAsMaxTemperature(max)
	 */
	@Raw
	public void setMaxTemperature(double max)
			throws IllegalArgumentException {
		if (! canHaveAsMaxTemperature(max))
			throw new IllegalArgumentException();
		maxTemperature = max;
	}


	/**
	 * Checks whether this square can have the given minimum temperature as 
	 * its minimum temperature.
	 * @param min
	 * The minimum temperature in degrees Celcius.
	 * @return 
	 * True iff the given minimum temperature is consistent with the 
	 * current temperature and the current maximum temperature.
	 *   | result == matchesMinTemperatureMax(min, getTemperature(), 
	 *   | 								getMaxTemperature())
	 */
	public boolean canHaveAsMinTemperature(double min) {
		return matchesMinTemperatureMax(min, getTemperature(),
											getMaxTemperature());
	}

	/**
	 * Checks whether this square can have the given maximum temperature as 
	 * its maximum temperature.
	 * @param max
	 * The maximum temperature in degrees Celcius.
	 * @return 
	 * True iff the given maximum temperature is consistent with the 
	 * current temperature and the current maximum temperature.
	 *   | result == matchesMinTemperatureMax(getMinTemperature(),
	 *   | 								getTemperature(), min)
	 */
	public boolean canHaveAsMaxTemperature(double max) {
		return matchesMinTemperatureMax(getMinTemperature(),
										getTemperature(), max);
	}

	/**
	 * Check whether the given temperature matches with the given 
	 * temperature limits.
	 *
	 * @param minTemperature 
	 * The minimum temperature limit in degrees Celcius.
	 * @param temperature 
	 * The acutual temperature in degrees Celcius.
	 * @param maxTemperature 
	 * The maximum temperature limit in degrees Celcius.
	 * @return 
	 * True iff the given temperature lays between the given temperature 
	 * limits.
	 *   | result == (minTemperature &lt;= temperature)
	 *   | 			&amp;&amp; (temperature &lt;= maxTemperature);
	 */
	public static boolean matchesMinTemperatureMax(double minTemperature,
				double temperature, double maxTemperature) {
		return (minTemperature <= temperature)
				&& (temperature <= maxTemperature);
	}

	/** 
	 * Check whether the given limits are valid temperature limits.
	 * @param min
	 * The lower temperature limit for this square in degrees Celcius.
	 * @param max
	 * The upper temperature limit for this square in degrees Celcius.
	 * @return
	 * True iff the lower limit is less or equal than the upper limit.
	 *   | result == (min &lt;= max)
	 */
	public static boolean areValidTemperatureLimits(double min, double max) {
		return min <= max;
	}


	/** 
	 * Variable registering the minimum temperature of this square. 
	 */
	private double minTemperature;
	/** 
	 * Variable registering the maximum temperature of this square. 
	 */
	private double maxTemperature;


	
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
		if (getTemperature() > -5)
			return 0;
		return 1 + (-5 - (int)getTemperature()) / 10;
	}
}

