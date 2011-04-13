package rpg;

import be.kuleuven.cs.som.annotate.*;
import rpg.exceptions.*;

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
 * @invar
 * Each square has proper borders.
 *   | hasProperBorders()
 * @invar
 * The borders of each square satisfy the constraints of the game.
 *   | bordersSatisfyConstraints()
 * @invar
 * No square has duplicate borders.
 *   | hasNoDuplicateBorders()
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
	 * The borders of the square get initialized.
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
	 * minimum temperature and 5000C as its maximum temperature. The floor 
	 * is not slippery.
	 *   | this(temperature, new Temperature(-200), new Temperature(5000),
	 *   |													humidity, false)
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
	 * Checks whether the given temperature is valid for this square. 
	 * 
	 * @param temperature
	 * The temperature of this square.
	 * @return
	 * True iff the given temperature is effective, not strictly lower than 
	 * the minimum temperature for this square, and not strictly higher 
	 * than the maximum temperature for this square.
	 *   | result == matchesMinTemperatureMax(getMinTemperature(), 
	 *   | 								temperature, getMaxTemperature());
	 */
	public boolean canHaveAsTemperature(Temperature temperature){
		return matchesMinTemperatureMax(getMinTemperature(), temperature,
				getMaxTemperature());
	}

	/**
	 * Sets the temperature of this square.
	 *
	 * @param temperature
	 * The new temperature.
	 * @post
	 * The new temperature of this square is equal to the given temperature
	 *   | new.getTemperature().equal(temperature)
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
	 * Set the minimum temperature for this square. 
	 * 
	 * @param min
	 * The minimum temperature for this square.
	 * @post
	 * The new minimum temperature for this square is equal to the given 
	 * minimum temperature.
	 *   | new.getMinTemperature().equals(min)
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
	 * Variable referencing the minimum temperature of this square. 
	 */
	private Temperature minTemperature;



	/**
	 * Returns the maximum temperature for this square.
	 */
	@Basic @Raw
	public Temperature getMaxTemperature() {
		return maxTemperature;
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
	 * Set the maximum temperature for this square. 
	 * 
	 * @param max
	 * The maximum temperature for this square.
	 * @post
	 * The new maximum temperature for this square is equal to the given 
	 * maximum temperature.
	 *   | new.getMaxTemperature().equals(max)
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
	 * Variable referencing the maximum temperature of this square. 
	 */
	private Temperature maxTemperature;


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
	 * Returns the cold damage associated with this square.
	 * 
	 * @return
	 * The damage points. One point for every COLD_DAMAGE_STEP degrees 
	 * Celcius the temperature of this square is below 
	 * COLD_DAMAGE_THRESHOLD degrees Celcius, rounded below.
	 *   | if (getTemperature().temperature() &gt; COLD_DAMAGE_THRESHOLD)
	 *   |     then result == 0
	 *   | else
	 *   |     result == 1 + (int)((COLD_DAMAGE_THRESHOLD
	 *   |				- getTemperature().temperature()) / COLD_DAMAGE_STEP)
	 */
	public int coldDamage() {
		double temp = getTemperature().temperature();
		if (temp > COLD_DAMAGE_THRESHOLD)
			return 0;
		return 1 + (int)((COLD_DAMAGE_THRESHOLD - temp) / COLD_DAMAGE_STEP);
	}

	/** 
	 * Variable registering the cold damage temperature threshold.
	 */
	public static final double COLD_DAMAGE_THRESHOLD = -5;
	/** 
	 * Variable registering the cold damage temperature step.
	 */
	public static final double COLD_DAMAGE_STEP = 10;


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
	 * Checks whether the given heat damage threshold temperature is a 
	 * valid heat damage threshold temperature for all squares.
	 *
	 * @param heatDamageThreshold
	 * The heat damage threshold temperature to check.
	 * @return
	 * True iff the given heat damage threshold temperature is 
	 * effective.
	 *   | result == (heatDamageThreshold != null)
	 */
	public static boolean isValidHeatDamageThreshold(Temperature 
													heatDamageThreshold) {
		return heatDamageThreshold != null; 
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
	 *   | new.getHeatDamageThreshold().equals(heatDamageThreshold)
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
	 * Checks whether the given heat damage temperature step is a valid 
	 * heat damage temperature step for all squares.
	 *
	 * @param heatDamageStep
	 * The heat damage temperature step to check.
	 * @return
	 * True iff the given heat damage step is strictly positive.
	 *   | result == (heatDamageStep &gt; 0)
	 */
	public static boolean isValidHeatDamageStep(double heatDamageStep) {
		return heatDamageStep > 0;
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
	 * Variable registering the heat damage temperature step for this 
	 * square, expressed in a scale that is compatible to the Celcius or 
	 * Kelvin scale.
	 * Note that this is not an actual <i>temperature</i>, but merely a 
	 * temperature <i>difference</i>. Hence we use a double that is 
	 * compatible with the Celcius scale, and not an actual Temperature.
	 */
	/* We could abstract this further by defining a TemperatureDifference 
	 * analogous to Temperature to work with different temperature scales, 
	 * but that seems to be overkill for this assignment. */
	private static double heatDamageStep = 15;



	/**
	 * Return the humidity for this square.
	 */
	@Basic @Raw
	public int getHumidity() {
		return humidity;
	}

	/**
	 * Return the humidity for this square, in string form.
	 *
	 * @return
	 * A string representing the humidity percentage
	 *   | result == (getHumidity() / 100 + "." + getHumidity() % 100 + "%")
	 */
	private String getHumidityString() {
		int integerPart = getHumidity() / 100;
		int fractionalPart = getHumidity() % 100;
		return integerPart + "."
				+ (fractionalPart < 10 ? "0":"") + fractionalPart + "%";
	}

	/**
	 * Checks whether the given humidity is a valid humidity for a square.
	 *
	 * @param humidity
	 * The humidity to check.
	 * @return
	 * True iff the given value is not strictly less than 0 and not 
	 * strictly larger than 10000.
	 *   | result == (0 &lt;= humidity) &amp;&amp; (humidity &lt;= 10000);
	 */
	public static boolean isValidHumidity(int humidity) {
		return (0 <= humidity) && (humidity <= 10000);
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
	public void setHumidity(int humidity) {
		assert isValidHumidity(humidity);
		this.humidity = humidity;
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
	 *   | if (getHumidity() &lt; RUST_DAMAGE_THRESHOLD
	 *   |		then result == 0
	 *   |		else result == (getHumidity() - RUST_DAMAGE_THRESHOLD)
	 *   |										/ RUST_DAMAGE_STEP
	 */
	public int rustDamage() {
		if (getHumidity() < RUST_DAMAGE_THRESHOLD)
			return 0;
		return (getHumidity() - RUST_DAMAGE_THRESHOLD) / RUST_DAMAGE_STEP;
	}

	/** 
	 * Variable registering the humidity threshold for rust damage.
	 */

	public static final int RUST_DAMAGE_THRESHOLD = 3000;
	/** 
	 * Variable registering the humidity step for rust damage.
	 */
	public static final int RUST_DAMAGE_STEP = 700;



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
	 * Returns whether or not this square is slippery at the moment.
	 * 
	 * @return
	 * True iff this square has a slippery floor, is slippery because of 
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
	 * True iff the temperature is below 0C and the humidity is greater 
	 * than 10%.
	 *   | result == (getTemperature().temperature() &lt; 0 
	 *   |				&amp;&amp; getHumidity() &gt; 1000);
	 */
	public boolean isSlipperyBecauseOfTemperature() {
		return getTemperature().temperature() < 0 && getHumidity() > 1000;
	}



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
	 * Return the border of this square in the given direction.
	 * 
	 * @param direction 
	 * The direction of the border.
	 * @pre
	 * The direction is effective
	 *   | direction != null
	 * @pre
	 * This square is not terminated
	 *   | !isTerminated()
	 */
	@Basic
	public Border getBorderAt(Direction direction) {
		assert direction != null;
		assert !isTerminated();
		return borders.get(direction);
	}

	/** 
	 * Checks whether the given direction is a valid direction for a 
	 * border. 
	 * 
	 * @param direction
	 * The direction of the border.
	 * @return 
	 * True iff the given value is not null.
	 *   | result == (direction != null)
	 */
	public static boolean isValidDirection(Direction direction) {
		return direction != null;
	}

	/** 
	 * Check whether this square can have the given border as its border in 
	 * the given direction.
	 * 
	 * @param direction 
	 * The direction of the border.
	 * @param border
	 * The border to check.
	 * @return
	 * True iff this square is terminated and the given border is null; or 
	 * this square is not terminated and the given border is not null nor 
	 * terminated
	 *   | if (isTerminated())
	 *   |		then result == (border == null)
	 *   | else
	 *   |		result == (border != null 
	 *   |						&amp;&amp; !border.isTerminated())
	 */
	@Raw
	public boolean canHaveAsBorderAt(Direction direction, Border border) {
		if (isTerminated())
			return border == null;
		return (border != null) && (!border.isTerminated());
	}


	/**
	 * Checks whether the borders of this square satisfy the constraints of 
	 * the game.
	 *
	 * @return
	 * True iff this square is not terminated and has:
	 *   - no doors placed in ceilings or floors
	 *   - at least one wall or door
	 *   - no more than three doors
	 */
	@Raw
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
				/* A door can not be placed in the floor or ceiling */
				if (direction.equals(Direction.UP) 
									|| direction.equals(Direction.DOWN))
					return false;
			} else if (border.isWall())
				numWallsOrDoors++;
		}
		return (numWallsOrDoors >= 1  &&  numDoors <= 3);
	}

	/** 
	 * Checks whether this square has proper borders associated with it.
	 *
	 * @return
	 * True iff every border of this square is valid for this square in 
	 * that direction and every border borders on this square.
	 *   | for each direction in Direction.values()
	 *   |		canHaveAsBorderAt(direction, getBorderAt(direction))
	 *   |			&amp;&amp; getBorderAt(direction).bordersOnSquare(this)
	 */
	@Raw
	public boolean hasProperBorders() {
		for (Direction direction : Direction.values()){
			Border border = getBorderAt(direction);
			if (!canHaveAsBorderAt(direction, border)
									|| !border.bordersOnSquare(this))
				return false;
		}
		return true;
	}

	/** 
	 * Returns whether this square has no dublicate borders.
	 *
	 * @return 
	 * Whether this square has no duplicate borders.
	 *   | for all d1 in Direction.values() :
	 *   | 		{ d2 in Direction.values() |
	 *   |			true : getBorderAt(d2) == getBorderAt(d1) }.size() == 1
	 */
	@Raw
	public boolean hasNoDuplicateBorders() {
		return borders.size() 
				== (new java.util.HashSet<Border>(borders.values())).size();
	}


	/** 
	 * Change the border of this square for the given direction to the given 
	 * border.
	 * 
	 * @param direction 
	 * The direction of the border.
	 * @param border
	 * The new border.
	 * @pre
	 * If this square is not terminated, then the given border must border 
	 * on this square (and be effective).
	 *   | isTerminated()
	 *   |		|| (border != null &amp;&amp; border.bordersOnSquare(this))
	 * @post
	 * The new border in the given direction is equal to the given border.
	 *   | new.getBorderAt(direction).equals(border)
	 * @throws IllegalArgumentException
	 * This square can not have the given border as a border in the given 
	 * direction.
	 *   | !canHaveAsBorderAt(direction, border)
	 * @throws IllegalArgumentException
	 * This square already has the given non-null border as a border for 
	 * some direction.
	 *   | border != null
	 *   | 	&amp;&amp; for some direction in Direction.values() :
	 *   |			border.equals(getBorderAt(direction))
	 * @throws BorderConstraintsException
	 * If the border of this square were to be changed to the given border, 
	 * some border constraints would be violated.
	 *
	 * XXX I impose no restrictions on the possible other square of the 
	 * border, so that part too can be 'raw'.... (ok?)
	 */
	public void changeBorderAt(Direction direction, @Raw Border border) 
				throws IllegalArgumentException, BorderConstraintsException {
		assert isTerminated()
				|| (border != null  &&  border.bordersOnSquare(this));

		if (!canHaveAsBorderAt(direction, border))
			throw new IllegalArgumentException();

		if (border != null && borders.containsValue(border))
			throw new IllegalArgumentException();

		Border oldBorder = getBorderAt(direction);
		borders.put(direction, border);
		if (!bordersSatisfyConstraints()){
			borders.put(direction, oldBorder);
			throw new BorderConstraintsException(this, border, direction);
		}
		oldBorder.detatchFromSquare(this);
	}

	/** 
	 * Update the border of this square to the given border.
	 * 
	 * @param oldBorder
	 * The old border.
	 * @param newBorder
	 * The new border.
	 * @effect
	 * changeBorderAt(getDirectionOfBorder(oldBorder), newBorder)
	 */
	public void updateBorder(@Raw Border oldBorder, @Raw Border newBorder) 
				throws IllegalArgumentException, BorderConstraintsException {
		changeBorderAt(getDirectionOfBorder(oldBorder), newBorder);
	}

	/** 
	 * Returns the direction associated with the given border of this 
	 * square.
	 *
	 * @param border
	 * The border whose direction to search for.
	 * @return
	 * The directon of the given border of this square.
	 * @throws IllegalArgumentException
	 * The given border is null or does not border this square.
	 *   | border == null  ||  !border.bordersOnSquare(this)
	 */
	public Direction getDirectionOfBorder(@Raw Border border) 
										throws IllegalArgumentException {
		for (java.util.Map.Entry<Direction, Border> entry : borders.entrySet())
			if (entry.getValue().equals(border))
				return entry.getKey();
		throw new IllegalArgumentException();
	}

	/** 
	 * Check whether this square has the given border as its border. 
	 * 
	 * @param border 
	 * The border to check.
	 * @return 
	 * Whether this square has the given border as its border. 
	 *   | result == (for some direction in Direction.values() :
	 *   |					getBorderAt(direction).equals(border))
	 */
	public boolean hasBorder(Border border) {
		return borders.containsValue(border);
	}

	/** 
	 * Initialize the borders of this square.
	 *
	 * @post
	 * The new square has a non-slippery 'wall' as a floor, and has open 
	 * borders everywhere else.
	 */
	@Raw
	private void initializeBorders() {
		for (Direction direction : Direction.values()){
			Border border;
			if (direction.equals(Direction.DOWN))
				border = new Wall(this, false);
			else
				border = new OpenBorder(this);
			borders.put(direction, border);
		}
	}

	// /** 
	//  * Returns a string representation of the borders.
	//  */
	// private String bordersString() {
	// 	String result = "";
	// 	for (int i = 1; i <= NUM_BORDERS; i++)
	// 		result = result + (hasBorderAt(i) ? i : " ");
	// 	return result;
	// }


	/** 
	 * Variable referencing an array of borders of this square.
	 */
	private java.util.Map<Direction, Border> borders = 
		new java.util.EnumMap<Direction, Border>(Direction.class);



	/** 
	 * Merge this square with the given square in the given direction. 
	 * 
	 * @param other
	 * The square to merge with this one.
	 * @param direction
	 * The direction in which to merge the squares.
	 * @effect
	 * The borgers get merged.
	 *   | mergeBorders(others, direction)
	 * @effect
	 * The humidities get merged.
	 *   | mergeHumidities(other)
	 * @effect
	 * The temperatures get merged.
	 *   | mergeTemperatures(other)
	 * @throws IllegalArgumentException
	 * The given other square is not effective.
	 *   | other == null
	 */
	public void mergeWith(Square other, int direction)
		throws IllegalArgumentException {
		if (other == null)
			throw new IllegalArgumentException();

		////////mergeBorders(other, direction);
		//TODO
		mergeTemperatures(other);
		/* temperatures must be merged before humidities! */
		mergeHumidities(other);
	}

	// /** 
	//  * Merge the borders of this square with the given square. 
	//  *
	//  * @pre
	//  * The other square is effective
	//  *   | other != null
	//  * @post
	//  * Both new squares are not bordererd in the given direction.
	//  *   | !this.hasBorderAt(direction)
	//  *   | 		&amp;&amp; !other.hasBorderAt(direction)
	//  * @throws IllegalArgumentException
	//  * The given direction is not a valid one
	//  *   | !isValidDirection(direction)
	//  */
	// public void mergeBorders(Square other, int direction)
	// 								throws IllegalArgumentException {
	// 	assert other != null;
	// 	if (!isValidDirection(direction))
	// 		throw new IllegalArgumentException();
	// 	this.setBorderAt(direction, false);
   // 	other.setBorderAt(direction, false);
   // }
	
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
		assert other != null;
		int newHumididty = (this.getHumidity() + other.getHumidity() + 1) / 2;
													//+1 to round correctly
		this.setHumidity(newHumididty);
		other.setHumidity(newHumididty);
	}
	
	/** 
	 * Merge the temperatures of this square with the given square.
	 * The new temperature of both squares is a weighted average of the old 
	 * temperatures. The weights consist of a constant factor 
	 * 'getMinTemperature()' and an additional weight, proportional to the 
	 * humidity of the squares, to reach a total average weight of unity.
	 * 
	 * @pre
	 * The other square is effective
	 *   | other != null
	 * @post
	 * Both new squares have the same temperature.
	 *   | new.getTemperature().equals((new other).getTemperature())
	 * @post
	 * The new temperature lies between the old temperatures of the 
	 * squares.
	 *   | new.getTemperature()
	 *   |   &lt;= min(old.getTemperature(), (old other).getTemperature())
	 *   | &amp;&amp; new.getTemperature()
	 *   |   &gt;= max(old.getTemperature(), (old other).getTemperature())
	 */
	public void mergeTemperatures(Square other) {
		assert other != null;
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
		//XXX this can throw IllegalArgumentException if the temperature is 
		//out of bounds for one of the squares!
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
	 * Checks whether the given weight constant for merging temperatures is 
	 * a valid weight constant for merging temperatures for all squares.
	 *
	 * @param mergeTemperatureWeight
	 * The weight constant for merging temperatures to check.
	 * @return
	 * True iff the given value is not strictly smaller than 
	 * MIN_MERGE_TEMPERATURE_WEIGHT and not strictly larger than 
	 * MAX_MERGE_TEMPERATURE_WEIGHT.
	 *   | result == (MIN_MERGE_TEMPERATURE_WEIGHT &lt;= mergeTemperatureWeight
	 *   |			&amp;&amp; 
	 *   |			mergeTemperatureWeight &lt;= MAX_MERGE_TEMPERATURE_WEIGHT)
	 */
	public static boolean isValidMergeTemperatureWeight(
										double mergeTemperatureWeight) {
		return (MIN_MERGE_TEMPERATURE_WEIGHT <= mergeTemperatureWeight
					&& mergeTemperatureWeight <= MAX_MERGE_TEMPERATURE_WEIGHT);
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
	 * Variable registering the minimum weight constant for merging 
	 * temperatures.
	 */
	public static final double MIN_MERGE_TEMPERATURE_WEIGHT = 0.1;

	/** 
	 * Variable registering the maximum weight constant for merging 
	 * temperatures.
	 */
	public static final double MAX_MERGE_TEMPERATURE_WEIGHT = 0.4;

	/**
	 * Variable registering the weight constant for merging temperatures 
	 * that applies to all squares.
	 * This value sets a baseline weight when calculating the weighted 
	 * average of the temperatures of two squares that will be merged.
	 * The default value is 0.2.
	 */
	private static double mergeTemperatureWeight = 0.2;



	/**
	 * Return the termination status for this square.
	 */
	@Basic @Raw
	public boolean isTerminated() {
		return isTerminated;
	}
	
	public void terminate(){
		//TODO
		isTerminated = true;
	}
	
	/**
	 * Variable registering the termination status for this square.
	 */
	private boolean isTerminated = false;



	public String toString() {
		return  "Temperature:    " + getTemperature()
			+ "\nHumidity:       " + getHumidityString()
			+ "\nFloor:          " + (hasSlipperyFloor()?"":"not ")+"slippery"
			+ "\nSlippery:       " + isSlippery()
			+ "\nCold damage:    " + coldDamage()
			+ "\nHeat damage:    " + heatDamage()
			+ "\nRust damage:    " + rustDamage()
			+ "\nInhabitability: " + inhabitability()
			//+ "\nBorders:        " + bordersString();
			;
	}
}

