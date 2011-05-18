package rpg;

import be.kuleuven.cs.som.annotate.*;
import rpg.exceptions.*;
import java.util.Collection;
import java.util.Map;
//import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;

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
 *   |                                      getMaxTemperature())
 * @invar
 * The heat damage threshold temperature that applies to all squares 
 * must be a valid heat damage threshold temperature.
 *   | isValidHeatDamageThreshold(getHeatDamageThreshold()) 
 * @invar
 * The heat damage temperature step that applies to all squares must be 
 * a valid heat damage temperature step.
 *   | isValidHeatDamageStep(getHeatDamageStep()) 
 * @invar
 * Each square has a valid humidity for that square.
 *   | canHaveAsHumidity(getHumidity()) 
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
 * @invar
 * area................................
 *
 * @author Roald Frederickx
 */

abstract public class Square {
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
     * @param borderInitializer
     * The borderInitializer to use for this new square.
     * @post
     * The minimum temperature for this new square is equal to the 
     * given minimum temperature.
     *   | new.getMinTemperature().equals(minTemp)
     * @post
     * The temperature for this new square is equal to the given 
     * temperature.
     *   | new.getTemperature().equals(temperature)
     * @post
     * The maximum temperature for this new square is equal to the 
     * given maximum temperature.
     *   | new.getMaxTemperature().equals(maxTemp)
     * @effect
     * The humidity for this new square gets initialized to the given 
     * humidity.
     *   | setHumidity(humidity)
     * @effect
     * The borders of the square get initialized.
     *   | initializeBorders(borderInitializer);
     * @throws IllegalArgumentException
     * Some of the given temperatures values are not effective, or the 
     * given temperature does not match with the given temperature limits.
     *   | !matchesMinTemperatureMax(minTemp, temperature, maxTemp)
     */
    @Raw @Model
    protected Square(Temperature temperature,
                    Temperature minTemp, Temperature maxTemp,
                    int humidity, BorderInitializer borderInitializer)
                                            throws IllegalArgumentException {
        if(!matchesMinTemperatureMax(minTemp, temperature, maxTemp))
            throw new IllegalArgumentException();
        minTemperature = minTemp;
        maxTemperature = maxTemp;
        setTemperatureRaw(temperature);
        setHumidity(humidity);
        initializeBorders(borderInitializer);
    }

    /** 
     * Initialize this new square to a square with the given temperature, 
     * humidity and borders.
     *
     * @param temperature
     * The temperature for this new square.
     * @param humidity
     * The humidity for this new square.
     * @param borderInitializer
     * The borderInitializer to use for this new square.
     * @effect
     * This new square is initialized with the given temperature as 
     * its temperature, the given humidity as its humidity,  -200C as its 
     * minimum temperature and 5000C as its maximum temperature and the 
     * given borders as its borders.
     *   | this(temperature, new Temperature(-200), new Temperature(5000),
     *   |                                               humidity, borders)
     */
    @Raw
    public Square(Temperature temperature, int humidity,
                            BorderInitializer borderInitializer)
                                            throws IllegalArgumentException {
        this(temperature, new Temperature(-200), new Temperature(5000),
                                                humidity, borderInitializer);
    }


    /** 
     * Initialize this new square to a square with the given borders.
     *
     * @param borderInitializer
     * The borderInitializer to use for this new square.
     * @effect
     * This new square is initialized with the given borders, 20C as its 
     * temperature and 50% humidity.
     *   | this(new Temperature(20), 5000, borderInitializer)
     */
    @Raw
    public Square(BorderInitializer borderInitializer)
                                            throws IllegalArgumentException {
        this(new Temperature(20), 5000, borderInitializer);
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
     * False if the given temperature is not effective, strictly lower than 
     * the minimum temperature for this square, or strictly higher 
     * than the maximum temperature for this square.
     *   | if !matchesMinTemperatureMax(getMinTemperature(), 
     *   |                              temperature, getMaxTemperature())
     *   |      then result == false
     */
    public boolean canHaveAsTemperature(Temperature temperature){
        return matchesMinTemperatureMax(getMinTemperature(), temperature,
                getMaxTemperature());
    }

    /**
     * Sets the temperature of this square and equilibrate its area 
     * afterwards.
     *
     * @param temperature
     * The new temperature.
     * @effect
     * The new temperature of this square is set to the given temperature 
     * and then the area of this square is equilibrated.
     *   | setTemperatureRaw(temperature); equilibrateMyArea()
     * @throws IllegalArgumentException
     * This square can not have the given temperature.
     *   | !canHaveAsTemperature(temperature)
     * @throws EquilibratingSquaresViolatesLimitsException
     * Equilibrating the area of this square after having set its 
     * temperature to the given temperature, violates some temperature or 
     * humidities constraints of a square in said area.
     */
    @Raw
    public void setTemperature(Temperature temperature)
                        throws IllegalArgumentException,
                                EquilibratingSquaresViolatesLimitsException {
        if (! canHaveAsTemperature(temperature))
            throw new IllegalArgumentException();
        Temperature oldTemperature = getTemperature();
        setTemperatureRaw(temperature);
        try {
            equilibrateMyArea();
        } catch (EquilibratingSquaresViolatesLimitsException e) {
            setTemperatureRaw(oldTemperature);
            throw e;
        }
    }

    /**
     * Sets the temperature of this square.
     *
     * @param temperature
     * The new temperature.
     * @pre
     * This square can have the given temperature as its temperature.
     *   | canHaveAsTemperature(temperature)
     * @post
     * The new temperature of this square is equal to the given temperature
     *   | new.getTemperature().equals(temperature)
     * @throws IllegalArgumentException
     * This square can not have the given temperature.
     */
    @Raw @Model
    public void setTemperatureRaw(Temperature temperature)
                                            throws IllegalArgumentException {
        assert canHaveAsTemperature(temperature);
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
     *   |                              getMaxTemperature())
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
        setMinTemperatureRaw(min);
    }

    /** 
     * Set the minimum temperature for this square, no matter what.
     * 
     * @param min
     * The minimum temperature for this square.
     * @post
     * The new minimum temperature for this square is equal to the given 
     * minimum temperature.
     *   | new.getMinTemperature().equals(min)
     */
    @Raw
    protected void setMinTemperatureRaw(Temperature min) {
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
     *   |                              getTemperature(), min)
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
     * Set the maximum temperature for this square, no matter what.
     * 
     * @param max
     * The maximum temperature for this square.
     * @post
     * The new maximum temperature for this square is equal to the given 
     * maximum temperature.
     *   | new.getMaxTemperature().equals(max)
     */
    @Raw
    protected void setMaxTemperatureRaw(Temperature max) {
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
     *   |          &amp;&amp; temperature != null
     *   |          &amp;&amp; maxTemperature != null
     *   |          &amp;&amp; minTemperature.compareTo(temperature) &lt;= 0
     *   |          &amp;&amp; temperature.compareTo(maxTemperature) &lt;= 0;
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
     *   |              - getTemperature().temperature()) / COLD_DAMAGE_STEP)
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
     *   |      then result == 0
     *   | else
     *   |      result == 1 + (int)((getTemperature().temperature()
     *   |                  - getHeatDamageThreshold().temperature())
     *   |                                  / getHeatDamageStep())
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
     * Checks whether this square can have the given humidity as its 
     * humidity. 
     * 
     * @param humidity
     * The humidity to check.
     * @return 
     * False if the given humidity is not a valid humidity for a square.
     *   | if (!isValidHumidity(humidity))
     *   |      then result == false
     */
    public boolean canHaveAsHumidity(int humidity) {
        return isValidHumidity(humidity);
    }

    /**
     * Set the humidity for this square to the given humidity.
     *
     * @param humidity
     * The new humidity for this square.
     * @pre
     * This square can have the given humidity as its humidity.
     *   | canHaveAsHumidity(humidity) 
     * @post
     * The new humidity for this square is equal to the given humidity.
     *   | new.getHumidity() == humidity
     */
    @Raw
    public void setHumidity(int humidity) {
        assert canHaveAsHumidity(humidity);
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
     *   |      then result == 0
     *   |      else result == (getHumidity() - RUST_DAMAGE_THRESHOLD)
     *   |                                      / RUST_DAMAGE_STEP
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
     * 
     * @pre
     * This square is not terminated.
     *   | !isTerminated()
     * @return
     * The slipperiness of the floor.
     *   | result == getBorderAt(Direction.DOWN).isSlippery()
     */
    public boolean hasSlipperyFloor() {
        assert !isTerminated();
        return getBorderAt(Direction.DOWN).isSlippery();
    }
    

    /**
     * Returns whether or not this square is slippery at the moment.
     * 
     * @return
     * True iff this square has a slippery floor, is slippery because of 
     * its humidity of is slippery because of its temperature.
     *   | result == (hasSlipperyFloor()
     *   |              || isSlipperyBecauseOfTemperature()
     *   |              || isSlipperyBecauseOfHumidity()
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
     *   |              &amp;&amp; getTemperature().temperature() &gt; 0)
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
     *   |              &amp;&amp; getHumidity() &gt; 1000);
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
     *   |              heatDamage() * heatDamage() * heatDamage()
     *   |                              / (101 - getHumidity()/100.0))
     *   |          - Math.sqrt(coldDamage())
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
     * The direction is valid
     *   | isValidDirection(direction)
     */
    @Basic @Raw
    public Border getBorderAt(Direction direction) {
        assert isValidDirection(direction);
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
     * Check whether this square can possibly have the given border as its 
     * border in the given direction, not taking into account specific 
     * rules for specific types of squares.
     * 
     * @param direction 
     * The direction of the border.
     * @param border
     * The border to check.
     * @return
     * True iff the given direction is valid and:
     *   - this square is terminated and the given border is null;
     *   or 
     *   - this square is not terminated and the given border is not null 
     *   nor terminated.
     *   | if (!isValidDirection(direction))
     *   |      then result == false
     *   | else if (isTerminated())
     *   |      then result == (border == null)
     *   | else
     *   |      result == (border != null 
     *   |                      &amp;&amp; !border.isTerminated())
     */
    @Raw
    public boolean canPossiblyHaveAsBorderAt(Direction direction, Border border) {
        if (!isValidDirection(direction))
            return false;
        if (isTerminated())
            return border == null;
        return (border != null) && (!border.isTerminated());
    }

    /** 
     * Check whether this square can have the given border as its border in 
     * the given direction, taking into account specific rules for specific 
     * types of squares.
     * 
     * @param direction 
     * The direction of the border.
     * @param border
     * The border to check.
     */
    @Raw
    abstract public boolean canHaveAsBorderAt(Direction direction, Border border);

    /**
     * Checks whether the borders of this square satisfy the specific 
     * constraints of the game for this type of square.
     *
     * @pre
     * This square has prober, non-duplicated borders
     *   | hasProperBorders() &amp;&amp; hasNoDuplicateBorders()
     * @return
     * True if this square is terminated.
     *   | if (isTerminated())
     *   |      then result == true
     */
    @Raw
    abstract public boolean bordersSatisfyConstraints();

    /** 
     * Check whether the given border is a proper border for the given 
     * direction.
     * 
     * @param direction
     * The direction of the border.
     * @param border
     * The border to check.
     * @return 
     * True iff this square can have the given border as a border in the 
     * given direction and the given border is either null or it borders on 
     * this square.
     *   | result ==
     *   |      (canHaveAsBorderAt(direction, border)
     *   |          &amp;&amp;
     *   |          (border == null  ||  border.bordersOnSquare(this)))
     */
    @Raw
    public boolean isProperBorderAt(Direction direction, Border border) {
        return (canHaveAsBorderAt(direction, border) &&
                (border == null  ||  border.bordersOnSquare(this)));
    }

    /** 
     * Checks whether this square has proper borders associated with it.
     *
     * @return
     * True iff every border of this square is a proper border for this 
     * square in its direction.
     *   | result ==
     *   |      for each direction in Direction.values() : 
     *   |              isProperBorderAt(direction, getBorderAt(direction)
     */
    @Raw
    public boolean hasProperBorders() {
        for (Direction direction : Direction.values()){
            if (!isProperBorderAt(direction, getBorderAt(direction)))
                return false;
        }
        return true;
    }

    /** 
     * Returns whether this square has no duplicate borders.
     *
     * @return 
     * Whether this square has no duplicate borders, or true if this square 
     * is terminated
     *   | if (isTerminated())
     *   |      then result == true
     *   | else
     *   |      result == (
     *   |          for all d1 in Direction.values() :
     *   |              { d2 in Direction.values() | true :
     *   |                  getBorderAt(d2) == getBorderAt(d1) }.size() == 1)
     */
    @Raw
    public boolean hasNoDuplicateBorders() {
        if (isTerminated())
            return true;
        return borders.size() 
                == (new HashSet<Border>(borders.values())).size();
    }


    /** 
     * Change the border of this square for the given direction to the given 
     * border.
     * 
     * @param direction 
     * The direction of the border.
     * @param border
     * The new border.
     * @post
     * If this square is not terminated, then the new border in the given 
     * direction is equal to the given border.
     *   | if (!isTerminated())
     *   |      then new.getBorderAt(direction).equals(border)
     * @effect
     * If the old border in the given direction is not null, then that old 
     * border gets detatched from this square.
     *   | if (old.getBorderAt(direction) != null)
     *   |      old.getBorderAt(direction).detatchFromSquare(this);
     * @throws IllegalArgumentException
     * This square can not have the given border as a proper border in the 
     * given direction.
     *   | !isProperBorderAt(direction, border)
     * @throws IllegalArgumentException
     * This square already has the given non-null border as a border for 
     * some direction.
     *   | hasBorder(border)
     * @throws BorderConstraintsException
     * If the border of this square were to be changed to the given border, 
     * some border constraints would be violated.
     */
    void changeBorderAt(Direction direction, @Raw Border border) 
                throws IllegalArgumentException, BorderConstraintsException {
        if (!isProperBorderAt(direction, border)  ||  hasBorder(border))
            throw new IllegalArgumentException();

        Border oldBorder = getBorderAt(direction);
        setBorderAt(direction, border);
        if (!bordersSatisfyConstraints()){
            setBorderAt(direction, oldBorder);
            throw new BorderConstraintsException(this, border, direction);
        }

        if (oldBorder == null)
            assert isTerminated();
        else
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
     * The border of this square in the given direction gets changed to the 
     * given border.
     *   | changeBorderAt(getDirectionOfBorder(oldBorder), newBorder)
     */
    void updateBorder(@Raw Border oldBorder, @Raw Border newBorder) 
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
     * @throws IllegalStateException
     * This square is terminated.
     *   | isTerminated()
     */
    public Direction getDirectionOfBorder(@Raw Border border) 
                    throws IllegalArgumentException, IllegalStateException {
        if (isTerminated())
            throw new IllegalStateException("Square is terminated!");
        for (Direction direction : Direction.values())
            if (getBorderAt(direction).equals(border))
                    return direction;
        throw new IllegalArgumentException();
    }

    /** 
     * Check whether this square has the given border as its border. 
     * 
     * @param border 
     * The border to check.
     * @return 
     * Whether this square has the given border as its border, or false if 
     * this square is terminated. 
     *   | if (isTerminated()  ||  border == null)
     *   |      then result == false
     *   | else
     *   |      result == (for some direction in Direction.values() :
     *   |                      getBorderAt(direction).equals(border))
     */
    public boolean hasBorder(Border border) {
        if (isTerminated()  ||  border == null)
            return false;
        return borders.containsValue(border);
    }

    
    /** 
     * Returns the squares that neighbour this square. 
     * 
     * @return 
     * A mapping of direction and squares that neighbour this square (in 
     * said direction).
     */
    public Map<Direction, Square> getNeighbours(){
        return getFilteredNeighbours(acceptAllNeighboursFilter);
    }

    /** 
     * Returns the squares that neighbour this square through open borders. 
     * 
     * @return 
     * A mapping of direction and squares that neighbour this square (in 
     * said direction) through open borders.
     */
    public Map<Direction, Square> getAccessibleNeighbours(){
        return getFilteredNeighbours(acceptOpenlyConnectedNeighboursFilter);
    }

    /** 
     * Return a collection of squares that can directly be navigated to 
     * from this square in a single step in one way or another.
     * 
     * @return 
     * A collection of squares that can directly be navigated to from this 
     * square in a single step in one way or another.
     */
    public Collection<Square> getNavigatableSquares() {
        return getAccessibleNeighbours().values();
    }

    /** 
     * An interface to filter neighbouring squares.
     */
    static interface NeighbourFilter {
        /** 
         * Check whether the given neighbour of the given square is allowed 
         * to pass this neighbour filter.
         * 
         * @param square
         * The square whose neighbour to filter.
         * @param border
         * The border at which the given square borders the given neighbour.
         * @param neighbour
         * The neighbour to filter.
         * @pre
         *   | border != null &amp;&amp;
         *   |              square != null &amp;&amp; neighbour != null
         * @pre
         *   | border.isSharedByTwoSquares()
         * @pre
         *   | border.bordersOnSquare(square);
         * @pre
         *   | border.bordersOnSquare(neighbour);
         */
        @Basic
        public boolean filter(Square square, Border border, Square neighbour);
    }
    
    static final NeighbourFilter acceptAllNeighboursFilter =
                new NeighbourFilter() {
                    public boolean filter(Square s, Border b, Square n){
                        return true;
                    }
                };

    static final NeighbourFilter acceptOpenlyConnectedNeighboursFilter =
                new NeighbourFilter() {
                    public boolean filter(Square s, Border border, Square n){
                        return border.isOpen();
                    }
                };

    /** 
     * Return a list of neighbouring squares that satisfy the given filter.
     * 
     * @param nf
     * The neighbourfilter that will be applied to the neighbours.
     * @pre
     *   | nf != null
     * @return
     * A list of neighbouring squares that satisfy the given filter.
     */
    @Raw
    Map<Direction, Square> getFilteredNeighbours(NeighbourFilter nf) {
        assert nf != null;
        Map<Direction, Square> result = 
            new EnumMap<Direction, Square>(Direction.class);
        for (Direction direction : Direction.values()){
            Border border = getBorderAt(direction);
            if (border == null)
                continue;
            Square neighbour = border.getNeighbour(this);
            if (neighbour != null  &&  nf.filter(this, border, neighbour))
                result.put(direction, neighbour);
        }
        return result;
    }

    /** 
     * Initialize the borders of this square.
     *
     * @param borderInitializer
     * The border initializer to use for the initialization of the borders.
     * @effect
     *   | borderInitializer.initializeBorders(this)
     */
    @Raw @Model
    private void initializeBorders(BorderInitializer borderInitializer) {
        borderInitializer.initializeBorders(this);
    }

    /** 
     * An interface to initialize the borders of a square during 
     * construction.
     */
    //@Model //not allowed, so it seems
    static interface BorderInitializer {
        /**
         * Initialize the borders of the given square.
         *
         * @post
         *   | for each direction in Direction.values():
         *   |      canHaveAsBorderAt(direction, getBorderAt(direction))
         */
        public void initializeBorders(@Raw Square square);
    }

    /** 
     * Set the border of this square in the given direction to the given 
     * border.
     * 
     * @param direction 
     * The direction of the new border.
     * @param border 
     * The new border.
     * @pre
     * The given border is a proper border for this square in the given 
     * direction.
     *   | isProperBorderAt(direction, border)
     * @post
     * The given border is the new border for this square in the given 
     * direction.
     *   | new.getBorderAt(direction).equals(border)
     */
    @Raw @Model
    protected void setBorderAt(Direction direction, Border border) {
        assert isProperBorderAt(direction, border);
        borders.put(direction, border);
    }

    /** 
     * Returns a string representation of the borders.
     */
    private String bordersString() {
        String result = "";
        for (Direction direction : Direction.values())
            result += direction.symbol()
                + ":" 
                + getBorderAt(direction).symbol()
                + " ";
        return result;
    }


    /** 
     * Variable referencing a map of borders of this square.
     */
    private Map<Direction, Border> borders = 
                            new EnumMap<Direction, Border>(Direction.class);


    /** 
     * Merge this square with the given square in the given direction. 
     * 
     * @param other
     * The square to merge with this one.
     * @param direction
     * The direction in which to merge the squares.
     * @effect
     * The border of this square in the given direction gets merged with 
     * the border of the other square in the complementary direction.
     *   | getBorderAt(direction).mergeWith(
     *   |              other.getBorderAt(direction.complement()))
     * @throws IllegalArgumentException
     * The given other square is not effective or the given direction is 
     * not valid.
     *   | other == null  ||  !isValidDirection(direction)
     * @throws IllegalStateException
     * This square and/or the given square is/are terminated.
     *   | this.isTerminated() || other.isTerminated()
     */
    public void mergeWith(Square other, Direction direction)
        throws IllegalArgumentException, IllegalStateException {
        if (other == null  ||  !isValidDirection(direction))
            throw new IllegalArgumentException();
        if (isTerminated() || other.isTerminated())
            throw new IllegalStateException();

        getBorderAt(direction).mergeWith(
                other.getBorderAt(direction.complement()));
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
     *   |      ((old this).getHumidity() + (old other).getHumidity() + 1) / 2
     *   | &amp;&amp;
     *   | (new other).getHumidity() == 
     *   |      ((old this).getHumidity() + (old other).getHumidity() + 1) / 2
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
     * 'getMergeTemperatureWeight()' and an additional weight, proportional 
     * to the humidity of the squares, to reach a total average weight of 
     * unity.
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
     * @throws MergingTemperaturesViolatesLimitsException
     * Merging the temperaturs would violate the temperature limits of one 
     * of the squares.
     */
    @Deprecated
    public void mergeTemperatures(Square other)
                        throws MergingTemperaturesViolatesLimitsException {
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

        if (!this.canHaveAsTemperature(newTemp)
                || !other.canHaveAsTemperature(newTemp))
            throw new MergingTemperaturesViolatesLimitsException();

        this.setTemperatureRaw(newTemp);
        other.setTemperatureRaw(newTemp);
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
     *   |          &amp;&amp; 
     *   |          mergeTemperatureWeight &lt;= MAX_MERGE_TEMPERATURE_WEIGHT)
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

    /** 
     * Terminate this square.
     *
     * @post
     * This square is terminated.
     *   | new.isTerminated()
     * @effect
     * The old borders get changed to null and they get detatched from any 
     * other squares.
     *   | for (Direction direction : Direction.values())
     *   |      changeBorderAt(direction, null);
     */
    void terminate(){
        isTerminated = true;
        for (Direction direction : Direction.values())
            changeBorderAt(direction, null);
    }

    /**
     * Variable registering the termination status for this square.
     */
    private boolean isTerminated = false;


    protected void equilibrateMyArea() 
        throws EquilibratingSquaresViolatesLimitsException {
        Set<Square> area = getArea();
        equilibrateAreaInternally(area);
        Set<Square> boundary = getBoundary(area);
        equilibrateBoundary(boundary);
    }

    /** 
     * ..................... 
     * 
     * @param squares 
     * @param nf 
     * @return 
     */
    @Raw
    static Set<Square> getNeighbouringSquares(@Raw Set<Square> squares,
                NeighbourFilter nf) {
            Set<Square> result = new HashSet<Square>();
            for (Square next : squares) {
                for (Square neigh : next.getFilteredNeighbours(nf).values()) {
                if (!squares.contains(neigh))
                    result.add(neigh);
            }
        }
        return result;
    }

    /** 
     * ..................... 
     * 
     * @param squares 
     * @param nf 
     * @return 
     */
    @Raw
    static Set<Square> getNeighbouringSquaresRecursively(
                                                    @Raw Set<Square> squares,
                                                    NeighbourFilter nf) {
        Set<Square> result = new HashSet<Square>();
        Queue<Square> queue = new LinkedList<Square>();
        queue.addAll(squares);
        while (!queue.isEmpty()) {
            Square next = queue.remove();
            for (Square neigh : next.getFilteredNeighbours(nf).values()) {
                if (!squares.contains(neigh) && !result.contains(neigh)){
                    result.add(neigh);
                    queue.add(neigh);
                }
            }
        }
        return result;
    }

    @Raw
    protected Set<Square> getArea() {
        Set<Square> squareSet = new HashSet<Square>();
        squareSet.add(this);
        Set<Square> neighbours = getNeighbouringSquaresRecursively(squareSet,
                                    acceptOpenlyConnectedNeighboursFilter);
        neighbours.addAll(squareSet);
        return neighbours;
    }

    @Raw
    protected static Set<Square> getBoundary(@Raw Set<Square> area) {
        return getNeighbouringSquares(area, acceptAllNeighboursFilter);
    }

    protected static void equilibrateAreaInternally(@Raw Set<Square> area) 
                        throws EquilibratingSquaresViolatesLimitsException {
        if (area.size() == 0)
            return;

        double temperatureWeightedSum = 0;
        double humiditiesSum = 0;
        double temperatureWeightOffset = getMergeTemperatureWeight();
        double temperatureBaseWeight = 1 - temperatureWeightOffset;

        for (Square square : area) {
            humiditiesSum  += square.getHumidity();
        }
        double averageHumidity = humiditiesSum / area.size();

        for (Square square : area) {
            double temperatureWeight = temperatureWeightOffset
                            + temperatureBaseWeight * square.getHumidity() 
                                                        / averageHumidity;
            temperatureWeightedSum += square.getTemperature().temperature()
                                                        * temperatureWeight;
        }
        Temperature newTemperature = new Temperature(
                                        temperatureWeightedSum / area.size());
        int newHumidity = (int) Math.round(averageHumidity);

        for (Square square : area){
            if (!square.canHaveAsTemperature(newTemperature)
                                    || !square.canHaveAsHumidity(newHumidity))
                throw new EquilibratingSquaresViolatesLimitsException();
        }
        for (Square square : area) {
            square.setTemperatureRaw(newTemperature);
            square.setHumidity(newHumidity);
        }
    }

    /** 
     * Notify all the squares in the given set that one of their neighbours 
     * has changed its temperature and/or humidity. 
     * 
     * @param boundary 
     * .............
     */
    protected static void equilibrateBoundary(@Raw Set<Square> boundary) {
        for (Square square : boundary) {
            square.neighbourHasChangedTemperatureOrHumidity();
        }
    }

    /** 
     * Function signalling that one of the neighbours of this square has 
     * changed its temperature or humidity.
     * This function will <i>only</i> be called if this square is bordering 
     * an area that has just been equilibrated. It will <i>not</i> get 
     * called for every temperature and/or humidity change of the squares 
     * in an area that get equilibrated.
     */
    @Raw
    protected void neighbourHasChangedTemperatureOrHumidity() {
        return;
    }

    public String toString() {
        if (isTerminated())
            return "Terminated!";
        return  "Temperature:    " + getTemperature()
            + "\nHumidity:       " + getHumidityString()
            + "\nSlippery Floor: " + hasSlipperyFloor()
            + "\nSlippery:       " + isSlippery()
            + "\nCold damage:    " + coldDamage()
            + "\nHeat damage:    " + heatDamage()
            + "\nRust damage:    " + rustDamage()
            + "\nInhabitability: " + inhabitability()
            + "\nBorders:        " + bordersString();
    }
}


// vim: ts=4:sw=4:expandtab:smarttab

