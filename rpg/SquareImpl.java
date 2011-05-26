package rpg;

import rpg.exceptions.*;
import rpg.util.Direction;
import rpg.util.Temperature;

import be.kuleuven.cs.som.annotate.*;

import java.util.Map;
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
 * The weight constant for merging temperatures that applies to all 
 * squares must be a valid weight constant for merging temperatures.
 *   | isValidMergeTemperatureWeight(getMergeTemperatureWeight()) 
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

abstract public class SquareImpl implements Square {
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
    protected SquareImpl(Temperature temperature,
                    Temperature minTemp, Temperature maxTemp,
                    int humidity, BorderInitializer borderInitializer)
                            throws IllegalArgumentException,
                                   BorderConstraintsException {
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
    @Raw @Model
    protected SquareImpl(Temperature temperature, int humidity,
                            BorderInitializer borderInitializer)
                                        throws IllegalArgumentException,
                                               BorderConstraintsException {
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
    @Raw @Model
    protected SquareImpl(BorderInitializer borderInitializer)
                                        throws IllegalArgumentException,
                                               BorderConstraintsException {
        this(new Temperature(20), 5000, borderInitializer);
    }

    /**
     * Returns the temperature of this square.
     */
    @Override
    @Basic @Raw
    public Temperature getTemperature() {
        return temperature;
    }

    /** 
     * Checks whether the given temperature is valid for this square. 
     */
    @Override
    public boolean canHaveAsTemperature(Temperature temperature){
        return matchesMinTemperatureMax(getMinTemperature(), temperature,
                getMaxTemperature());
    }

    /**
     * Sets the temperature of this square and equilibrate its area 
     * afterwards.
     */
    @Override
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
     * Sets the temperature of this square, without equilibrating its area.
     */
    @Override
    @Raw
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
    @Override
    @Basic @Raw
    public Temperature getMinTemperature() {
        return minTemperature;
    }

    /**
     * Checks whether this square can have the given minimum temperature as 
     * its minimum temperature.
     */
    @Override
    public boolean canHaveAsMinTemperature(Temperature min) {
        return matchesMinTemperatureMax(min, getTemperature(),
                getMaxTemperature());
    }

    /** 
     * Set the minimum temperature for this square. 
     */
    @Override
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
    @Override
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
     */
    @Override
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
     */
    @Override
    public boolean matchesMinTemperatureMax(Temperature minTemperature,
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
    @Override
    public int coldDamage() {
        double temp = getTemperature().temperature();
        if (temp > COLD_DAMAGE_THRESHOLD)
            return 0;
        return 1 + (int)((COLD_DAMAGE_THRESHOLD - temp) / COLD_DAMAGE_STEP);
    }

    /** 
     * Constant registering the cold damage temperature threshold.
     */
    public static final double COLD_DAMAGE_THRESHOLD = -5;
    /** 
     * Constant registering the cold damage temperature step.
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
    @Override
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
        SquareImpl.heatDamageThreshold = heatDamageThreshold;
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
        SquareImpl.heatDamageStep = heatDamageStep;
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
    @Override
    public int getHumidity() {
        return humidity;
    }

    /**
     * Return the humidity for this square, in string form.
     *
     * @return
     * A string representing the humidity percentage
     *   | result == (getHumidity() / 100 + "." 
     *   |                + (getHumidity() % 100 < 10 ? "0" : "") 
     *   |                + getHumidity() % 100 + "%")
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
     */
    @Override
    public boolean canHaveAsHumidity(int humidity) {
        return isValidHumidity(humidity);
    }

    /**
     * Set the humidity for this square to the given humidity.
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
    @Override
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
    @Override
    public boolean hasSlipperyFloor() {
        assert !isTerminated();
        return getBorderAt(Direction.DOWN).isSlippery();
    }

    /**
     * Returns whether or not this square is slippery at the moment.
     */
    @Override
    public boolean isSlippery() {
        return hasSlipperyFloor()
                || isSlipperyBecauseOfTemperature()
                || isSlipperyBecauseOfHumidity();
    }

    /** 
     * Returns whether or not this square is slippery at the moment because 
     * of humidity.
     */
    @Override
    public boolean isSlipperyBecauseOfHumidity() {
        return getHumidity() == 10000 && getTemperature().temperature() > 0;
    }

    /** 
     * Returns whether or not this square is slippery at the moment because 
     * of temperature.
     */
    @Override
    public boolean isSlipperyBecauseOfTemperature() {
        return getTemperature().temperature() < 0 && getHumidity() > 1000;
    }

    /** 
     * Return the inhabitability associated with this square. 
     */
    @Override
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
     */
    @Basic @Raw
    @Override
    public Border getBorderAt(Direction direction) {
        assert isValidDirection(direction);
        if (borders == null)
            return null;
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
     */
    @Raw
    @Override
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
     */
    @Raw
    @Override
    abstract public boolean canHaveAsBorderAt(Direction direction, Border border);

    /**
     * Checks whether the borders of this square satisfy the specific 
     * constraints of the game for this type of square.
     */
    @Raw
    @Override
    abstract public boolean bordersSatisfyConstraints();

    /** 
     * Check whether the given border is a proper border for the given 
     * direction.
     */
    @Raw
    @Override
    public boolean isProperBorderAt(Direction direction, Border border) {
        return (canHaveAsBorderAt(direction, border) &&
                (border == null  ||  border.bordersOnSquare(this)));
    }

    /** 
     * Checks whether this square has proper borders associated with it.
     */
    @Raw
    @Override
    public boolean hasProperBorders() {
        for (Direction direction : Direction.values()){
            if (!isProperBorderAt(direction, getBorderAt(direction)))
                return false;
        }
        return true;
    }

    /** 
     * Returns whether this square has no duplicate borders.
     */
    @Raw
    @Override
    public boolean hasNoDuplicateBorders() {
        if (isTerminated())
            return true;
        return borders.size() 
                == (new HashSet<Border>(borders.values())).size();
    }

    /** 
     * Change the border of this square for the given direction to the given 
     * border.
     */
    @Override
    public void changeBorderAt(Direction direction, @Raw Border border) 
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

        if (!isTerminated())
            equilibrateMyArea();
    }

    /** 
     * Update the border of this square to the given border.
     */
    @Override
    public void updateBorder(@Raw Border oldBorder, @Raw Border newBorder) 
                throws IllegalArgumentException, BorderConstraintsException {
        changeBorderAt(getDirectionOfBorder(oldBorder), newBorder);
    }

    /** 
     * Returns the direction associated with the given border of this 
     * square.
     */
    @Override
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
     */
    @Override
    public boolean hasBorder(Border border) {
        if (isTerminated()  ||  border == null)
            return false;
        return borders.containsValue(border);
    }
    
    /** 
     * Returns the squares that neighbour this square. 
     */
    @Override
    public Map<Direction, Square> getNeighbours(){
        return getFilteredNeighbours(acceptAllNeighboursFilter);
    }

    /** 
     * Returns the squares that neighbour this square through open borders. 
     */
    @Override
    public Map<Direction, Square> getAccessibleNeighbours(){
        return getFilteredNeighbours(acceptOpenlyConnectedNeighboursFilter);
    }

    /** 
     * Return a set of squares that can directly be navigated to 
     * from this square in a single step in one way or another.
     */
    @Override
    public Set<Square> getNavigatableSquares() {
        return new HashSet<Square>(getAccessibleNeighbours().values());
    }

    /** 
     * Return a list of neighbouring squares that satisfy the given filter.
     */
    @Raw
    @Override
    public Map<Direction, Square> getFilteredNeighbours(NeighbourFilter nf) {
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
     * @throws BorderConstraintsException
     * Initializing the borders with the given borderInitializer leads to 
     * inconsintencies with the border constraints as enforced by 
     * bordersSatisfyConstraints().
     */
    @Raw @Model
    private void initializeBorders(BorderInitializer borderInitializer)
                                        throws BorderConstraintsException {
        borderInitializer.initializeBorders(this);
        if (!bordersSatisfyConstraints())
            throw new BorderConstraintsException();
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
         *   |     new.canHaveAsBorderAt(direction, new.getBorderAt(direction))
         */
        public void initializeBorders(@Raw SquareImpl square);
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
     */
    @Override
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
     */
    @Override
    @Deprecated
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
     */
    @Override
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
        SquareImpl.mergeTemperatureWeight = mergeTemperatureWeight;
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
     * Checks whether the area of this square is properly equilibrated.
     *
	 * @return 
	 *   | isTerminated() || new Area().isEquilibrated();
	 */
    @Override
	public boolean myAreaIsEquilibrated() {
		if (isTerminated())
			return true;
		return new Area().isEquilibrated();
	}

    /** 
     * Equilibrate the temperatures and humidities of the area that this 
     * square is part of.
     *
     * @effect
     *   | new Area().equilibrate()
     */
    @Override
    public void equilibrateMyArea() 
                    throws EquilibratingSquaresViolatesLimitsException {
        new Area().equilibrate();
    }

	/**
     * Return the area that this square blongs to.
	 * 
	 * @return
	 * The set of squares as returned by new Area().getArea().
	 */
    @Override
	public Set<Square> getArea() {
		assert !isTerminated();
		return new Area().getArea();
	}

	/**
     * Return the boundary of the area that this square blongs to.
	 *
	 * @return
	 * The set of squares as returned by new Area().getBoundary().
	 */
    @Override
	public Set<Square> getAreaBoundary() {
		assert !isTerminated();
		return new Area().getBoundary();
	}

	/** 
	 * A class collecting methods that relate to the concept of an area 
	 * surrounding this square.
	 */
	public class Area {

		/** 
		 * Returns a set of the squares in this area associated to this 
		 * square.
		 * 
		 * @throws IllegalStateException
		 *   | SquareImpl.this.isTerminated()
		 */
		@Basic
		public Set<Square> getArea() throws IllegalStateException {
			if (SquareImpl.this.isTerminated())
				throw new IllegalStateException();
			Set<Square> squareSet = new HashSet<Square>();
			Square square = SquareImpl.this;
			squareSet.add(square);
			addNeighbouringSquaresRecursively(squareSet,
                            Square.acceptOpenlyConnectedNeighboursFilter);
			return squareSet;
		}

		/** 
		 * Return the boundary of this area.
		 * 
		 * @return
		 * The neighbouring squares of this area.
		 * @throws IllegalStateException
		 *   | SquareImpl.this.isTerminated()
		 */
		@Basic
		public Set<Square> getBoundary() throws IllegalStateException {
			if (SquareImpl.this.isTerminated())
				throw new IllegalStateException();
			return getNeighbouringSquaresOnly(getArea(),
									Square.acceptAllNeighboursFilter);
		}

		/** 
		 * Return the boundary of the given set of squares.
		 * 
		 * @param area 
		 * The set of squares whose boundary to return.
		 * @return 
		 *   | result == getNeighbouringSquaresOnly(area,
		 *   |				Square.acceptAllNeighboursFilter)
		 */
		private Set<Square> getBoundary(@Raw Set<Square> area) {
			return getNeighbouringSquaresOnly(area,
									Square.acceptAllNeighboursFilter);
		}

		/** 
		 * Checks whether the area of this square is properly equilibrated.
		 *
         * @return
         * True iff all squares in the area of this squre have the same 
         * temperature and humidity as this square, and all squares in the 
         * boundary of the area of this square can have their temperatures 
         * and humidities.
		 *   | result == (
		 *   |   (for each square in getArea() :
		 *   |      square.getTemperature().equals(getTemperature())
		 *   |      &amp;&amp; square.getHumidity() == getHumidity())
		 *   |   &amp;&amp;
		 *   |   (for each square in getBoundary(getArea()) :
		 *   |      square.canHaveAsTemperature(square.getTemperature())
		 *   |      &amp;&amp; square.canHaveAsHumidity(square.getHumidity())))
		 * @throws IllegalStateException
		 *   | SquareImpl.this.isTerminated()
		 */
		@Raw
		public boolean isEquilibrated() throws IllegalStateException {
			if (SquareImpl.this.isTerminated())
				throw new IllegalStateException();
			Set<Square> area = getArea();
			Temperature myTemperature = getTemperature();
			int myHumidity = getHumidity();
			for (Square square : area)
				if (!square.getTemperature().equals(myTemperature)
							|| square.getHumidity() != myHumidity)
					return false;
			Set<Square> boundary = getBoundary(area);
			for (Square square : boundary)
				if (!square.canHaveAsTemperature(square.getTemperature())
                        || !square.canHaveAsHumidity(square.getHumidity()))
					return false;
			return true;
		}

		/** 
		 * Equilibrate the temperatures and humidities of the area associated 
		 * with this square.
		 *
         * @effect
         *   | equilibrateAreaInternally()
         *   | equilibrateBoundary()
		 * @post
		 *   | new.isEquilibrated()
		 * @throws EquilibratingSquaresViolatesLimitsException
		 * Equilibrating this area violates some temperature or humidities 
		 * constraints of a square.
		 */
		public void equilibrate() 
							throws EquilibratingSquaresViolatesLimitsException {
			Set<Square> area = getArea();
			equilibrateAreaInternally(area);
			Set<Square> boundary = getBoundary(area);
			equilibrateBoundary(boundary);
		}
       
        /**
         * Equilibrate the enterior of this area.
         *
         * @effect
         *   | equilibrateAreaInternally(getArea());
		 * @throws EquilibratingSquaresViolatesLimitsException
         * Equilibrating the interior of this area violates some 
         * temperature or humidities constraints of a square.
         */
        public void equilibrateAreaInternally()
							throws EquilibratingSquaresViolatesLimitsException {
            equilibrateAreaInternally(getArea());
        }
        /**
         * Equilibrate the boundary of this area.
         *
         * @effect
         *   | equilibrateBoundary(getBoundary())
		 * @throws EquilibratingSquaresViolatesLimitsException
         * Equilibrating the boundary of this area violates some 
         * temperature or humidities constraints of a square.
         */
        public void equilibrateBoundary()
							throws EquilibratingSquaresViolatesLimitsException {
            equilibrateBoundary(getBoundary());
        }

		/** 
		 * Return the neighbouring squares of the given set of (raw) 
		 * squares that satisfy the given filter.
		 * 
		 * @param squares
		 * A set of raw squares whose neighbours to get.
		 * @param nf
		 * The filter to apply to the neighbours.
		 * @return
		 * The neighbouring squares of the given set of (raw) squares that 
		 * satisfy the given filter.
		 */
		@Raw
		private Set<Square> getNeighbouringSquaresOnly(
											@Raw Set<Square> squares,
											Square.NeighbourFilter nf) {
				Set<Square> result = new HashSet<Square>();
				for (Square next : squares) {
					for (Square neigh : next.getFilteredNeighbours(nf)
                                                                .values()) {
					if (!squares.contains(neigh))
						result.add(neigh);
				}
			}
			return result;
		}

		/** 
         * Recursively expand the given set of squares with all neighbours 
         * of its squares according to the given neighbourfilter.
		 * 
		 * @param squares 
		 * The set of squares to expand.
		 * @param nf 
		 * The neighbourfilter to use.
		 * @pre
		 *   | squares != null  &amp;&amp;  nf != null
		 * @post
		 * new.squares is a maximal recursive expansion of old.squares, by 
		 * adding all neighbouring squares that satisfy the given neighbour 
		 * filter.
		 */
		@Raw
		private void addNeighbouringSquaresRecursively(
                                                @Raw Set<Square> squares,
                                                Square.NeighbourFilter nf) {
			assert squares != null;
			assert nf != null;
			Queue<Square> queue = new LinkedList<Square>();
			queue.addAll(squares);
			while (!queue.isEmpty()) {
				Square next = queue.remove();
				for (Square neigh : next.getFilteredNeighbours(nf).values()) {
					if (!squares.contains(neigh)) {
						squares.add(neigh);
						queue.add(neigh);
					}
				}
			}
		}

		/** 
		 * Equilibrate the temperatures and humidities betwneen the given 
		 * squares.
		 * 
		 * @param area
		 * The set of squares to equilibrate.
		 * @post
		 * All squares in the given area have the same temperature and 
		 * humidity.
         * The temperature is the weighted sum of the 
         * temperatures of the squares in this area, weighted by their 
         * humidities according to the merge temperature weight that 
         * applies to all squares.
         * The humidity is the average of the humidities of the squares in 
         * this area.
		 */
        @Model
		private void equilibrateAreaInternally(@Raw Set<Square> area) 
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
				temperatureWeightedSum += 
                            square.getTemperature().temperature()
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
         * Notify all the squares in the given set that one of their 
         * neighbours has changed its temperature and/or humidity. 
		 * 
         * @param boundary A set of squares that border a region of squares 
         * that have changed their temperature or humidity. For more 
         * information, also see 
         * neighbourHasChangedTemperatureOrHumidity(), equilibrateMyArea() 
         * and setTemperature().
         * @pre
		 *   | boundary != null
		 * @effect
		 *   | for each square in boundary:
		 *   |      square.neighbourHasChangedTemperatureOrHumidity()
		 */
        @Model
		private void equilibrateBoundary(@Raw Set<Square> boundary) {
			assert boundary != null;
			for (Square square : boundary) {
				square.neighbourHasChangedTemperatureOrHumidity();
			}
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
    @Override
    public void neighbourHasChangedTemperatureOrHumidity() {
        return;
    }

    /** 
     * Checks whether it is possible to navigate to the given destination 
     * square, starting from this square.
     *
     * The implementation does a depth-first traversal of the neighbouring 
     * squares of this square on a spanning tree of the graph of 
     * navigatable squares.
     *
     * Its average time complexity is linear in the number of squares that 
     * are openly connected to this square. Hence, in general, if this 
     * square is part of a dungeon, the average time complexity is 
     * linear in the number of squares in the root dungeon.
     * 
     * Note that the <i>worst</i> case time complexity is superlinear 
     * (probably quadratic, depending on the implementation of java's 
     * HashSet). This happens in the pathological case where all 
     * squares hash to the same value.
     *
     * Also note that that if the containing dungeon of this square were to 
     * enforce extra constraints (eg. open areas can be no larger than N 
     * squares), the time complexity would effectively reduce to 
     * constant-time. 
     *
     * Finally, note that smarter search strategies could be employed at 
     * the level of dungeons, where, for instance, the distance between the 
     * coordinates can be used as a cost function in heuristic algorithms 
     * such as the A* search algorithm.
     */
    @Override
    public boolean canNavigateTo(Square destination)
                                            throws IllegalArgumentException {
        if (destination == null)
            throw new IllegalArgumentException();
        
        Set<Square> visited = new HashSet<Square>();
        return canNavigateTo(destination, visited);
    }

    /** 
     * Checks whether the given destination square can be reached, starting 
     * from this square and only moving through open borders, without 
     * visiting a square that is in the given set of already visited squares.
     */
    @Override
    public boolean canNavigateTo(Square destination, Set<Square> visited) {
        assert destination != null;
        assert visited != null;
        assert !visited.contains(destination);

        if (this.equals(destination))
            return true;

        if (visited.contains(this))
            return false;

        visited.add(this);

        for (Square square : getNavigatableSquares())
            if (square.canNavigateTo(destination, visited))
                return true;
        return false;
    }

    /**
     * Return the termination status for this square.
     */
    @Basic @Raw
    @Override
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
    @Override
    public void terminate(){
        isTerminated = true;
        for (Direction direction : Direction.values())
            changeBorderAt(direction, null);
    }

    /**
     * Variable registering the termination status for this square.
     */
    private boolean isTerminated = false;


    public String summarizedStatusString() {
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

    /**
     * Check whether this square satisfies all its class invariants.
     */
    @Raw
    @Override
    public boolean isNotRaw() {
        return true
            && isValidMergeTemperatureWeight(getMergeTemperatureWeight()) 
            && isValidHeatDamageThreshold(getHeatDamageThreshold()) 
            && isValidHeatDamageStep(getHeatDamageStep()) 
            && matchesMinTemperatureMax(getMinTemperature(),
                                    getTemperature(), getMaxTemperature())
            && canHaveAsHumidity(getHumidity()) 
            && hasProperBorders()
            && bordersSatisfyConstraints()
            && hasNoDuplicateBorders()
            && myAreaIsEquilibrated();
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

