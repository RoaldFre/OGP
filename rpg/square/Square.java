package rpg.square;

import rpg.exceptions.*;
import rpg.util.Direction;
import rpg.util.Temperature;

import be.kuleuven.cs.som.annotate.*;

import java.util.Map;
import java.util.Set;

/**
 * An interface of squares involving a temperature, a humidity and a set of 
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
 * Each square has a valid humidity for that square.
 *   | canHaveAsHumidity(getHumidity()) 
 * @invar
 * Each square has proper borders.
 *   | hasProperBorders()
 * @invar
 * The borders of each square satisfy the constraints of the game.
 *   | bordersSatisfyConstraints()
 * @invar
 * No square has duplicate borders.
 *   | hasNoDuplicateBorders()
 * @invar
 * The area of this square is equilibrated.
 *   | myAreaIsEquilibrated()
 *
 * @author Roald Frederickx
 */

public interface Square {

    /**
     * Returns the temperature of this square.
     */
    @Basic @Raw
    public Temperature getTemperature();

    /** 
     * Checks whether the given temperature is valid for this square. 
     * 
     * @param temperature
     * The temperature of this square.
     * @return
     * False if the given temperature does not match with the minimum and 
     * maximum temperature of this square.
     *   | if !matchesMinTemperatureMax(getMinTemperature(), 
     *   |                              temperature, getMaxTemperature())
     *   |      then result == false
     */
    public boolean canHaveAsTemperature(Temperature temperature);

    /**
     * Sets the temperature of this square and equilibrate its area 
     * afterwards.
     *
     * @param temperature
     * The new temperature.
     * @effect
     * The new temperature of this square is set to the given temperature 
     * and then the area of this square is equilibrated.
     *   | setTemperatureRaw(temperature);
     *   | equilibrateMyArea()
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
                                EquilibratingSquaresViolatesLimitsException;

    /**
     * Sets the temperature of this square, without equilibrating its area.
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
    @Raw 
    public void setTemperatureRaw(Temperature temperature)
                                            throws IllegalArgumentException;

    /**
     * Returns the minimum temperature for this square.
     */
    @Basic @Raw
    public Temperature getMinTemperature();

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
    public boolean canHaveAsMinTemperature(Temperature min);

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
                                    throws IllegalArgumentException;

    /**
     * Returns the maximum temperature for this square.
     */
    @Basic @Raw
    public Temperature getMaxTemperature();

    /**
     * Checks whether this square can have the given maximum temperature as 
     * its maximum temperature.
     *
     * @param max
     * The maximum temperature.
     * @return 
     * True iff the given maximum temperature is consistent with the 
     * current temperature and the current maximum temperature.
     *   | result == matchesMinTemperatureMax(getMinTemperature(),
     *   |                              getTemperature(), min)
     */
    public boolean canHaveAsMaxTemperature(Temperature max);

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
                                throws IllegalArgumentException;

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
    public boolean matchesMinTemperatureMax(Temperature minTemperature,
                            Temperature temperature, Temperature maxTemperature);


    /** 
     * Returns the cold damage associated with this square.
     *
     * @return
     * A positive number
     *   | result &gt;= 0
     */
    public int coldDamage();

    /** 
     * Returns the heat damage associated with this square.
     *
     * @return
     * A positive number
     *   | result &gt;= 0
     */
    public int heatDamage();

    /**
     * Return the humidity for this square, expressed in hundredths of 
     * percent.
     * Zero denotes 0% humidity, 10000 denotes 100% humidity.
     */
    @Basic @Raw
    public int getHumidity();

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
    public boolean canHaveAsHumidity(int humidity);

    /**
     * Set the humidity for this square to the given humidity, expressed in 
     * hundredths of percent.
     * Zero denotes 0% humidity, 10000 denotes 100% humidity.
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
    public void setHumidity(int humidity);
    
    /** 
     * Returns the rust damage associated with this square.
     * 
     * @return
     * A positive number
     *   | result &gt;= 0
     */
    public int rustDamage();

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
    public boolean hasSlipperyFloor();

    /**
     * Returns whether or not this square is slippery at the moment.
     * 
     * @return
     * True if this square has a slippery floor, is slippery because of 
     * its humidity of is slippery because of its temperature.
     *   | if (hasSlipperyFloor()
     *   |              || isSlipperyBecauseOfTemperature()
     *   |              || isSlipperyBecauseOfHumidity()
     *   |      then result == true
     */
    public boolean isSlippery();

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
    public boolean isSlipperyBecauseOfHumidity();

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
    public boolean isSlipperyBecauseOfTemperature();


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
    public double inhabitability();


    /** 
     * Return the border of this square in the given direction.
     * 
     * @param direction 
     * The direction of the border.
     * @pre
     * The direction is effective
     *   | direction != null
     */
    @Basic @Raw
    public Border getBorderAt(Direction direction);

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
     * True iff the given direction is not null and:
     *     - this square is terminated and the given border is null;
     *   or 
     *     - this square is not terminated and the given border is not null 
     *       nor terminated.
     *   | if (direction == null)
     *   |      then result == false
     *   | else if (isTerminated())
     *   |      then result == (border == null)
     *   | else
     *   |      result == (border != null 
     *   |                      &amp;&amp; !border.isTerminated())
     */
    @Raw
    public boolean canPossiblyHaveAsBorderAt(Direction direction, Border border);

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
    public boolean canHaveAsBorderAt(Direction direction, Border border);

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
    public boolean bordersSatisfyConstraints();

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
    public boolean isProperBorderAt(Direction direction, Border border);

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
    public boolean hasProperBorders();

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
    public boolean hasNoDuplicateBorders();

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
     * @post
     * If this square is not terminated, the area of this square gets 
     * equilibrated after having changed the border.
     *   | old.isTerminated() || new.myAreaIsEquilibrated()
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
     * some border constraints as enforced by bordersSatisfyConstraints()  
     * would be violated.
     */
    // Note: I would have liked to restrict the visibility of this method 
    // to this package only, but that cannot be done in an interface.
    public void changeBorderAt(Direction direction, @Raw Border border) 
                throws IllegalArgumentException, BorderConstraintsException;

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
    // Note: I would have liked to restrict the visibility of this method 
    // to this package only, but that cannot be done in an interface.
    public void updateBorder(@Raw Border oldBorder, @Raw Border newBorder) 
                throws IllegalArgumentException, BorderConstraintsException;

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
                    throws IllegalArgumentException, IllegalStateException;

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
    public boolean hasBorder(Border border);
    
    /** 
     * Returns the squares that neighbour this square. 
     * 
     * @return 
	 *   | result == getFilteredNeighbours(acceptAllNeighboursFilter)
     */
    public Map<Direction, Square> getNeighbours();

    /** 
     * Returns the squares that neighbour this square through open borders. 
     * 
     * @return 
	 *   | result == getFilteredNeighbours(
	 *   |							acceptOpenlyConnectedNeighboursFilter)
     */
    public Map<Direction, Square> getAccessibleNeighbours();

    /** 
     * Return a set of squares that can directly be navigated to 
     * from this square in a single step in one way or another.
     * 
     * @return 
     * A set of squares that can directly be navigated to from this 
     * square in a single step in one way or another.
     * @return
     * The resulting set contains all accessible neighbours.
     *   | for each square in getAccessibleNeighbours().values()
     *   |      result.contains(square)
     */
    public Set<Square> getNavigatableSquares();

    /** 
     * Checks whether it is possible to navigate to the given destination 
     * square, starting from this square.
     * 
     * @param destination
     * The destination square.
     * @return
     *   | if (this.equals(destination))
     *   |      then result == true
     *   | else result == (for some square in getNavigatableSquares() :
     *   |                          square.canNavigateTo(destination))
     * @throws IllegalArgumentException
     *   | destination == null
     */
    public boolean canNavigateTo(Square destination)
                                            throws IllegalArgumentException;

    /** 
     * Checks whether the given destination square can be reached, starting 
     * from this square and only moving through open borders, without 
     * visiting a square that is in the given set of already visited squares.
     *
     * @param destination
     * The destination square.
     * @param visited
     * The squares that have already been visited.
     * @pre
     *   | destination != null
     * @pre
     *   | visited != null
     * @pre
     *   | !visited.contains(destination)
     * @post
     *   | if (result == false)
     *   | then (new visited).contains(this)
     * @post
     *   | if (result == false)
     *   | then for each square in getNavigatableSquares() :
     *   |          (new visited).contains(square)
     * @post
     *   | for each square in (old visited) :
     *   |          (new visited).contains(square)
     * @return
     *   | if (this.equals(destination))
     *   |      then result == true
     *   | else (if visited.contains(this))
     *   |      then result == false
     *   | else result == (for some square in getNavigatableSquares() :
     *   |                          square.canNavigateTo(destination))
     */
    public boolean canNavigateTo(Square destination, Set<Square> visited);

    /** 
     * An interface to filter neighbouring squares.
     */
    public static interface NeighbourFilter {
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
         *   | square != neighbour
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
    
    /**
     * Constant referencing a neighbour filter that accepts all squares.
     */
    public static final NeighbourFilter acceptAllNeighboursFilter =
                new NeighbourFilter() {
                    public boolean filter(Square s, Border b, Square n){
                        return true;
                    }
                };

    /**
     * Constant referencing a neighbour filter that accepts all openly 
     * connected squares.
     */
    public static final NeighbourFilter acceptOpenlyConnectedNeighboursFilter =
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
    public Map<Direction, Square> getFilteredNeighbours(NeighbourFilter nf);

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
     * not effective.
     *   | other == null  ||  direction == null
     * @throws IllegalStateException
     * This square and/or the given square is/are terminated.
     *   | this.isTerminated() || other.isTerminated()
     */
    public void mergeWith(Square other, Direction direction)
        throws IllegalArgumentException, IllegalStateException;

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
    @Deprecated
    public void mergeHumidities(Square other);

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
                        throws MergingTemperaturesViolatesLimitsException;

    /** 
     * Checks whether the area of this square is properly equilibrated.
     */
    @Raw
    public boolean myAreaIsEquilibrated() throws IllegalStateException;

    /** 
     * Equilibrate the temperatures and humidities of the area that this 
     * square is part of.
     *
     * @pre
     *   | !isTerminated
     * @post
     *   | new.myAreaIsEquilibrated()
     */
    public void equilibrateMyArea() 
                        throws EquilibratingSquaresViolatesLimitsException;

    
    /** 
     * Return the area that this square blongs to.
     * 
     * @pre
     *   | !isTerminated()
     * @return 
     * A maximal set of openly connected squares that contains this square.
     */
    @Raw
    public Set<Square> getArea();

    /** 
     * Return the boundary of the area that this square blongs to.
     * 
     * @pre
     *   | !isTerminated()
     * @return 
     * The squares bordering a maximal set of openly connected squares that 
     * contains this square.
     */
    @Raw
    public Set<Square> getAreaBoundary();


    /** 
     * Function signalling that one of the neighbours of this square has 
     * changed its temperature or humidity.
     * This function will <i>only</i> be called if this square is bordering 
     * an area that has just been equilibrated. It will <i>not</i> get 
     * called for every temperature and/or humidity change of the squares 
     * in an area that get equilibrated.
     */
    @Raw
    public void neighbourHasChangedTemperatureOrHumidity();

    /**
     * Check if this square can be used as an endpoint in a teleporter.
     *
     * @return
     *   | if (isTerminated())
     *   |      then result == false
     */
    public boolean isPossibleTeleportationEndPoint();

    /**
     * Return the termination status for this square.
     */
    @Basic @Raw
    public boolean isTerminated();

    /** 
     * Terminate this square.
     *
     * @post
     * This square is terminated.
     *   | new.isTerminated()
     * @effect
     * The old borders get changed to null and they get detatched from any 
     * other squares.
     *   | for each direction in Direction.values() :
     *   |      changeBorderAt(direction, null);
     * @note
     * If you call this as an end user on a square that is embedded in a 
     * higher order structure, then <b>Demons may fly out of your nose!</b>
     * Those higher order structures (eg dungeons) may turn into a raw 
     * state without them knowing. Use caution and double check the 
     * appropriate invariants and preconditions.
     */
    public void terminate();

    /**
     * Check whether this square satisfies all its class invariants.
     *
     * @return
     * True iff this square satisfies all its class invariants.
     */
    public boolean isNotRaw();
}


// vim: ts=4:sw=4:expandtab:smarttab

