import be.kuleuven.cs.som.annotate.*;

/**
 * A class of squares involving a temperature, a humidity and a set of 
 * borders.
 * Each square also has as assocated cold damage, heat damage, rust damage, 
 * slipperiness and inhabitability.
 *
 * @invar
 * The combination of the temperature with the minimum and maximum 
 * temperature of each square is legal.
 *   | matchesMinTemperatureMax(getMinTemperature(), getTemperature(),
 *   | 										getMaxTemperature())
 * @invar
 * The heat damage threshold temperature that applies to all squares 
 * must be a valid heat damage threshold temperature.
 *   | isValidHeatDamageThreshold(getHeatDamageThreshold()) 
 * @invar
 * The heat damage temperature step that applies to all squares must be 
 * a valid heat damage temperature step.
 *   | isValidHeatDamageStep(getHeatDamageStep()) 
 * @invar
 * Each square has a valid humidity.
 *   | isValidHumidity(getHumidity()) 
 * @invar
 * The weight constant for merging temperatures that applies to all 
 * squares must be a valid weight constant for merging temperatures.
 *   | isValidMergeTemperatureWeight(getMergeTemperatureWeight()) 
 *
 * @author Roald Frederickx
 */

public class Square {
	/** 
	 * Initialize this new square to a square with the given temperature,
	 * temperature limits, humidity and slipperiness of the floor. 
	 * 
	 * @param temperature
	 * The temperature for this new square.
	 * @param minTemp 
	 * The minimum temperature for this new square.
	 * @param maxTemp 
	 * The maximum temperature for this new square.
	 * @param humidity
	 * The humidity for this new square.
	 * @param hasSlipperyFloor
	 * Whether or not this new square has a slippery floor.
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
	 * The humidity for this new square gets initialized to the given 
	 * humidity.
	 *   | setHumidity(humidity)
	 * @effect
	 * The slipperiness of the floor for this new square gets initialized 
	 * to the given slipperiness.
	 *   | setHasSlipperyFloor(hasSlipperyFloor)
	 * @effect
	 * The new square is bordered in all directions.
	 *   | initializeBorders();
	 * @throws IllegalArgumentException
	 * Some of the given temperatures values are not effective, or the 
	 * given temperature does not match with the given temperature limits.
	 *   | ! matchesMinTemperatureMax(minTemp, temperature, maxTemp)
	 */
	@Raw
	public Square(Temperature temperature,
					Temperature minTemp, Temperature maxTemp,
					int humidity, boolean hasSlipperyFloor)
											throws IllegalArgumentException {
		if (minTemp == null || maxTemp == null)
			throw new IllegalArgumentException();
		minTemperature = minTemp;
		maxTemperature = maxTemp;
		setTemperature(temperature);
		setHumidity(humidity);
		setHasSlipperyFloor(hasSlipperyFloor);
		initializeBorders();
	}

	/** 
	 * Initialize this new square to a square with the given temperature, 
	 * and humidity. 
	 *
	 * @param temperature
	 * The temperature for this new square.
	 * @param humidity
	 * The humidity for this new square.
	 * @effect
	 * This new square is initialized with the given temperature as 
	 * its temperature, the given humidity as its humidity,  -200C as its 
	 * minimum temperature and 5000C as its maximum temperature. The heat 
	 * damage threshold and step are 35C and 15C, respectively. The floor 
	 * is not slippery.
	 *   | this(temperature, new Temperature(-200), new Temperature(5000),
	 *   |		new Temperature(35), 15.0, humidity, false)
	 */
	@Raw
	public Square(Temperature temperature, int humidity) 
									throws IllegalArgumentException {
		this(temperature, new Temperature(-200), new Temperature(5000),
														humidity, false);
	}

	/** 
	 * Initialize this new square to a default square. 
	 *
	 * @effect
	 * This new square is initialized as a square with a temperature of 20C 
	 * and a humidity of 50%.
	 *   | this(new Temperature(20), 5000);
	 */
	@Raw
	public Square() {
		this(new Temperature(20), 5000);
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
	 * @return
	 * The damage points. One point for every 10 degrees the 
	 * temperature of this square is below -5 degrees Celcius, rounded 
	 * below.
	 *   | if (getTemperature().temperature() &gt; -5)
	 *   |     then result == 0
	 *   | else
	 *   |     result == 1 + (int)((-5 - getTemperature().temperature()) / 10)
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
	 * @return
	 * The damage points. One point for every "heat damage step" 
	 * degrees the temperature of this square is above the heat damage 
	 * threshold, rounded below.
	 *   | if getTemperature().compareTo(getHeatDamageThreshold()) &lt; 0
	 *   | 		then result == 0
	 *   | else
	 *   | 		result == 1 + (int)((getTemperature().temperature()
	 *   |					- getHeatDamageThreshold().temperature())
	 *   |									/ getHeatDamageStep())
	 */
	public int heatDamage() {
		if (getTemperature().compareTo(getHeatDamageThreshold()) < 0)
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
	private static Temperature heatDamageThreshold = new Temperature(35);






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
	private static double heatDamageStep = 15;





	/**
	 * Return the humidity for this square.
	 */
	@Basic @Raw
	public int getHumidity() {
		return humidity;
	}

	/**
	 * Set the humidity for this square to the given humidity.
	 *
	 * @param humidity
	 * The new humidity for this square.
	 * @pre
	 * The given humidity must be a valid humidity for this square.
	 *   | isValidHumidity(humidity) 
	 * @post
	 * The new humidity for this square is equal to the given humidity.
	 *   | new.getHumidity() == humidity
	 */
	@Raw
	public void setHumidity(int humidity) throws IllegalArgumentException {
		this.humidity = humidity;
	}
	
	/**
	 * Checks whether the given humidity is a valid humidity for a square.
	 *
	 * @param humidity
	 * The humidity to check.
	 * @return
	 * True if and only if the given value is not strictly less than 0 and not 
	 * strictly larger than 10000.
	 *   | result == (0 &lt;= humidity) &amp;&amp; (humidity &lt;= 10000);
	 */
	public static boolean isValidHumidity(int humidity) {
		return (0 <= humidity) && (humidity <= 10000);
	}
	
	/**
	 * Variable registering the humidity for this square, expressed in 
	 * hundredths of percent.
	 * Zero denotes 0% humidity, 10000 denotes 100% humidity.
	 */
	private int humidity;


	/** 
	 * Returns the rust damage associated with this square.
	 * 
	 * @return
	 * The rust damage associated with this square.
	 *   | if (getHumidity() &lt; 3000
	 *   |		then result == 0
	 *   |		else result == (getHumidity() - 3000) / 700
	 */
	public int rustDamage() {
		if (getHumidity() < 3000)
			return 0;
		return (getHumidity() - 3000) / 700;
	}

	/**
	 * Returns whether or not this square is slippery at the moment.
	 * 
	 * @return
	 * True iff this square has as slippery floor, is slippery because of 
	 * its humidity of is slippery because of its temperature.
	 *   | result == (hasSlipperyFloor()
	 *   |				|| isSlipperyBecauseOfTemperature()
	 *   |				|| isSlipperyBecauseOfHumidity()
	 */
	public boolean isSlippery() {
		return hasSlipperyFloor()
				|| isSlipperyBecauseOfTemperature()
				|| isSlipperyBecauseOfHumidity();
	}

	/** 
	 * Returns whether or not this square is slippery at the moment because 
	 * of humidity.
	 * 
	 * @return 
	 * True iff the humidity is 100% and the temperature is positive (in 
	 * degrees Celcius).
	 *   | result == (getHumidity() == 10000
	 *   |				&amp;&amp; getTemperature().temperature() &gt; 0)
	 */
	public boolean isSlipperyBecauseOfHumidity() {
		return getHumidity() == 10000 && getTemperature().temperature() > 0;
	}

	/** 
	 * Returns whether or not this square is slippery at the moment because 
	 * of temperature.
	 * 
	 * @return
	 * True iff the temperature is below 0 (in degrees Celcius) and the 
	 * humidity is higher than 10%.
	 *   | result == (getTemperature().temperature() &lt; 0 
	 *   |				&amp;&amp; getHumidity() &gt; 1000);
	 */
	public boolean isSlipperyBecauseOfTemperature() {
		return getTemperature().temperature() < 0 && getHumidity() > 1000;
	}

	/**
	 * Return the slipperiness of the floor for this square.
	 */
	@Basic @Raw
	public boolean hasSlipperyFloor() {
		return hasSlipperyFloor;
	}
	
	/**
	 * Set the slipperiness of the floor for this square.
	 *
	 * @param hasSlipperyFloor
	 * The new slipperiness of the floor for this square.
	 * @post
	 * The new slipperiness of the floor for this square is equal to the 
	 * given slipperiness of the floor.
	 *   | new.hasSlipperyFloor() == hasSlipperyFloor
	 */
	@Raw
	public void setHasSlipperyFloor(boolean hasSlipperyFloor) {
		this.hasSlipperyFloor = hasSlipperyFloor;
	}
	
	/**
	 * Variable registering the slipperiness of the floor for this square.
	 */
	private boolean hasSlipperyFloor;




	/** 
	 * Return the inhabitability associated with this square. 
	 * 
	 * @return
	 * The inhabitability associated with this square.
	 *   | result == -1 * Math.sqrt(
	 *   |				heatDamage() * heatDamage() * heatDamage()
	 *   |								/ (101 - getHumidity()/100.0))
	 *   |			- Math.sqrt(coldDamage())
	 */
	public double inhabitability() {
		double heatDam = heatDamage();
		double heatDamCubed = heatDam * heatDam * heatDam;
		double coldDam = coldDamage();
		double humidityPercent = getHumidity() / 100.0;

		return -1 * Math.sqrt(heatDamCubed / (101 - humidityPercent))
					- Math.sqrt(coldDam);
	}




	
	/** 
	 * Return whether or not this square is borderd in the given direction.
	 * 
	 * @param direction 
	 * The direction to check for a border.
	 */
	@Basic
	public boolean hasBorderAt(int direction) {
		if (!isValidDirection(direction))
			return false;
		return borders[direction - 1];
	}

	/** 
	 * Set the border of this square for the given direction to the given 
	 * 'boundedness'.
	 * 
	 * @param direction 
	 * The direction of the border.
	 * @param bordered
	 * The new 'boundedness' of the border.
	 */
	public void setBorderAt(int direction, boolean bordered) {
		if (!isValidDirection(direction))
			return;
		borders[direction - 1] = bordered;
	}

	/** 
	 * Checks whether the given direction is a valid direction for a 
	 * border. 
	 * 
	 * @param direction
	 * The direction of the border.
	 * @return 
	 * True iff the given value is not strictly less that 1 and not 
	 * strictly larger than 6.
	 *   | result == (1 &lt;= direction &amp;&amp; direction &lt;= 6)
	 */
	public static boolean isValidDirection(int direction) {
		return 1 <= direction && direction <= 6;
	}

	/** 
	 * Initialize the square to have borders in every direction. 
	 *
	 * @post
	 * The new square is bordered in all directions.
	 *   | for each direction in 1..6
	 *   | 		hasBorderAt(direction)
	 */
	private void initializeBorders() {
		for (int i = 1; i <= 6; i++)
			setBorderAt(i, true);
	}

	/** 
	 * Variable referencing an array of borders of this square.
	 */
	private boolean[] borders = new boolean[6];



	/** 
	 * Merge this square with the given square in the given direction. 
	 * 
	 * @param other
	 * The square to merge with this one.
	 * @param direction
	 * The direction in which to merge the squares.
	 * @effect
	 * The borgers get merged
	 *   | mergeBorders(others, direction)
	 * @effect
	 * The humidities get merges
	 *   | mergeHumidities(other)
	 * @effect
	 * The temperature get merged
	 *   | mergeTemperatures(other)
	 * @throws IllegalArgumentException
	 * The given other square is not effective.
	 *   | other == null
	 */
	public void mergeWith(Square other, int direction)
							throws IllegalArgumentException {
		if (other == null)
			throw new IllegalArgumentException();

		mergeBorders(other, direction);
		mergeTemperatures(other);
		/* temperatures must be merged before humidities! */
		mergeHumidities(other);
	}

	/** 
	 * Merge the borders of this square with the given square. 
	 *
	 * @pre
	 * The other square is effective
	 *   | other != null
	 * @post
	 * Both new squares are not bordererd in the given direction.
	 *   | !this.hasBorderAt(direction)
	 *   | 		&amp;&amp; !other.hasBorderAt(direction)
	 * @throws IllegalArgumentException
	 * The given direction is not a valid one
	 *   | !isValidDirection(direction)
	 */
	public void mergeBorders(Square other, int direction)
									throws IllegalArgumentException {
		if (!isValidDirection(direction))
			throw new IllegalArgumentException();
		this.setBorderAt(direction, false);
		other.setBorderAt(direction, false);
	}
	
	/** 
	 * Merge the humidities of this square with the given square. 
	 *
	 * @pre
	 * The other square is effective
	 *   | other != null
	 * @post
	 * New humidity of both squares is average of humidity of the old
	 * squares.
	 *   | (new this).getHumidity() == 
	 *   |		((old this).getHumidity() + (old other).getHumidity() + 1) / 2
	 *   | &amp;&amp;
	 *   | (new other).getHumidity() == 
	 *   |		((old this).getHumidity() + (old other).getHumidity() + 1) / 2
	 */
	public void mergeHumidities(Square other) {
		int newHumididty = (this.getHumidity() + other.getHumidity() + 1) / 2;
													//+1 to round correctly
		this.setHumidity(newHumididty);
		other.setHumidity(newHumididty);
	}
	
	/** 
	 * Merge the temperatures of this square with the given square.
	 * 
	 * @pre
	 * The other square is effective
	 *   | other != null
	 * @post
	 * New temperature of both squares is a weighted average of the old 
	 * temperatures. The weights consist of a constant factor 
	 * 'getMinTemperature()' and an additional weight, proportional to the 
	 * humidity of the squares, to reach a total average weight of unity.
	 *   | See implementation.
	 */
	public void mergeTemperatures(Square other) {
		double thisTemp = this.getTemperature().temperature();
		double otherTemp = other.getTemperature().temperature();

		double averageHumidity = (getHumidity() + other.getHumidity()) / 2.0;

		double weightOffset = getMergeTemperatureWeight();
		double baseWeight = 1 - weightOffset;
		double thisWeight = weightOffset 
							+ baseWeight * getHumidity() / averageHumidity;
		double otherWeight = 2 - thisWeight;

		double newTempValue = ((thisWeight) * thisTemp
								+ (otherWeight) * otherTemp) / 2.0;
		Temperature newTemp = new Temperature(newTempValue);

		this.setTemperature(newTemp);
		other.setTemperature(newTemp);
	}


	

	/**
	 * Returns the weight constant for merging temperatures that applies to 
	 * all squares.
	 */
	@Basic @Raw
	public static double getMergeTemperatureWeight() {
		return mergeTemperatureWeight;
	}
	
	/**
	 * Set the weight constant for merging temperatures that applies to all 
	 * squares to the given weight constant for merging temperatures.
	 *
	 * @param mergeTemperatureWeight
	 * The new weight constant for merging temperatures for all squares. 
	 * @post
	 * The new weight constant for merging temperatures for all squares is 
	 * equal to the given weight constant for merging temperatures.
	 *   | new.getMergeTemperatureWeight() == mergeTemperatureWeight
	 * @throws IllegalArgumentException
	 * The given weight constant for merging temperatures is not valid 
	 * weight constant for merging temperatures for a square
	 *   | ! isValidMergeTemperatureWeight(mergeTemperatureWeight) 
	 */
	@Raw
	public static void setMergeTemperatureWeight(double mergeTemperatureWeight)
										throws IllegalArgumentException {
		if (!isValidMergeTemperatureWeight(mergeTemperatureWeight))
			throw new IllegalArgumentException();
		Square.mergeTemperatureWeight = mergeTemperatureWeight;
	}
	
	/**
	 * Checks whether the given weight constant for merging temperatures is 
	 * a valid weight constant for merging temperatures for all squares.
	 *
	 * @param mergeTemperatureWeight
	 * The weight constant for merging temperatures to check.
	 * @return
	 * True if and only if the given value is not strictly smaller than 0.1 
	 * and not strictly larger than 0.4.
	 *   | result == (0.1 &lt;= mergeTemperatureWeight
	 *   |				&amp;&amp; mergeTemperatureWeight &lt;= 0.4)
	 */
	public static boolean isValidMergeTemperatureWeight(
										double mergeTemperatureWeight) {
		return (0.1 <= mergeTemperatureWeight
					&& mergeTemperatureWeight <= 0.4);
	}
	
	/**
	 * Variable registering the weight constant for merging temperatures 
	 * that applies to all squares.
	 * This value sets a baseline weight when calculating the weighted 
	 * average of the temperatures of two squares that will be merged.
	 * The default value is 0.2.
	 */
	private static double mergeTemperatureWeight = 0.2;
}

