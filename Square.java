/* 
 * Author     : Roald Frederickx
 * License    : GPL
 */


public class Square {

	//TODO: Farenheit?

	/**
	 * Returns the temperature of this Square.
	 *
	 * @return The temperature of this Square in degrees Celcius.
	 */
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
	 * True iff the given temperature is not strictly lower than the lower 
	 * temperature bound, and not strictly higher than the upper 
	 * temperature bound.
	 *   | return == (getMinTemperature() &lt;= temperature)
	 *   |			&amp;&amp; (temperature &lt;= getMaxTemperature());
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
	 * Set the temperature boundaries for this square. 
	 * 
	 * @param min
	 * The lower temperature bound for this Square in degrees Celcius.
	 * @param max
	 * The upper temperature bound for this Square in degrees Celcius.
	 * @throws IllegalArgumentException
	 * The given temperature boundaries are illegal,
	 *   | ! areValidTemperatureBoundaries(min, max)
	 */
	public void setTemperatureBoundaries(double min, double max)
			throws IllegalArgumentException {
		if (! areValidTemperatureBoundaries(min, max))
			throw new IllegalArgumentException();
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
	public boolean areValidTemperatureBoundaries(double min, double max){
		return min <= max;
	}

	/**
	 * Returns the minimum temperature for this square.
	 *
	 * @return The minimum temperature in degrees Celcius..
	 */
	public double getMinTemperature() {
		return minTemperature;
	}
	
	/**
	 * Returns the maximum temperature for this square.
	 *
	 * @return The maximum temperature in degrees Celcius..
	 */
	public double getMaxTemperature() {
		return maxTemperature;
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

}

