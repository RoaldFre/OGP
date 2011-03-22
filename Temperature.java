import be.kuleuven.cs.som.annotate.*;

/** 
 * A class representing a temperature in a variety of temperature scales.
 *
 * @author Roald Frederickx
 */
public class Temperature {

	/**
	 * Enumeration of all the supported scales for all temperatures.
	 */
	public static enum Scale {
		CELCIUS, FAHRENHEIT, KELVIN
	};

	/**
	 * Initialize this new temperature to a temperature with the given 
	 * temperature value (in degrees Celcius).
	 *
	 * @param temperature 
	 * The value for this new temperature in degrees Celcius.
	 * @effect
	 * The temperature value for this temperature gets Initialized to the 
	 * given temperature value interpreted as degrees Celcius.
	 *   | this(temperature, Scale.CELCIUS);
	 */
	public Temperature(double temperature) {
		this(temperature, Scale.CELCIUS);
	}

	/**
	 * Initialize this new temperature to a temperature with the given 
	 * temperature value, in the given temperature scale.
	 *
	 * @param temperature 
	 * The value for this new temperature.
	 * @param scale 
	 * The temperature scale of the given temperature value.
	 * @post
	 * The temperature value for this temperature gets Initialized to the 
	 * given temperature.
	 *   | new.temperature(scale) == temperature
	 * @throws IllegalArgumentException
	 * The given temperature scale is invalid or unknown.
	 */
	public Temperature(double temperature, Scale scale) {
		switch (scale) {
			case CELCIUS:
				this.temperature = temperature;
				break;
			case KELVIN:
				this.temperature = temperature - 273.15;
				break;
			case FAHRENHEIT:
				this.temperature = (temperature - 32) * 5/9;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}

	/**
	 * Return the temperature value in degrees Celcius for this temperature.
	 */
	@Basic @Raw
	public double temperature() {
		return temperature;
	}

	/**
	 * Return the temperature value for this temperature in 
	 * the given scale.
	 * @param scale
	 * The temperature scale in which to return the temperature value.
	 * @throws IllegalArgumentException
	 * The given temperature scale is invalid or unknown.
	 */
	@Basic @Raw
	public double temperature(Scale scale) throws IllegalArgumentException {
		switch (scale) {
			case CELCIUS:
				return temperature;
			case KELVIN:
				return temperature + 273.15;
			case FAHRENHEIT:
				return temperature * 9/5 + 32;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Variable registering the temperature (in degrees Celcius) for this 
	 * temperature.
	 */
	private double temperature;	


	/** 
	 * Compare this temperature with a given temperatures.
	 * 
	 * @param other
	 * The temperature to compare this temperature to.
	 * @return
	 * -1 if the other temperature is strictly larger (on the Celcius 
	 *  scale), 0 if they are equal and 1 otherwise.
	 *    | if (temperature() &lt; other.temperature())
	 *    |     result == -1
	 *    | else if (temperature() &gt; other.temperature())
	 *    |     result == 1
	 *    | else result == 0
	 */
	public int compareTo(Temperature other) {
		if (temperature() < other.temperature())
			return -1;
		if (temperature() > other.temperature())
			return 1;
		return 0;
	}

	/** 
	 * Check for equality between this temperature and a given temperatures.
	 * Note: this allows a relative error of 1e-8 in order to compensate 
	 * for rounding errors when comparing different initial temperature scales.
	 * 
	 * @param other
	 * The temperature to compare this temperature to.
	 * @return
	 * True iff this temperature is equal to (up to a relative error of 
	 * EQUALS_EPSILON) to the given temperature.
	 *   | result == (Math.abs(Temperature() - other.temperature())
	 *   |				&lt;= temperature() * EQUALS_EPSILON)
	 */
	public boolean equals(Temperature other) {
		double allowedError = temperature() * EQUALS_EPSILON;
		return Math.abs(temperature() - other.temperature()) <= allowedError;
	}

	public static final double EQUALS_EPSILON = 1e-6;
}
