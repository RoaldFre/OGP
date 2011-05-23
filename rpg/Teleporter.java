package rpg;

import be.kuleuven.cs.som.annotate.*;

import java.util.Set;
import java.util.HashSet;

/**
 * A class representing a teleporter involving destination squares.
 *
 * @invar
 * Each teleporter has valid destinations.
 *   | hasValidDestinations()
 *
 * @author Roald Frederickx
 */
public class Teleporter {
	/** 
	 * Initialize this new teleporter to a teleporter with the given 
	 * destination.
	 * 
	 * @param destination 
	 * The destination for this new teleporter.
	 * @post
	 *   | new.getDestination().equals(destination)
	 * @throws IllegalArgumentException
	 *   | !isValidDestination(destination)
	 */
	public Teleporter(Square destination) throws IllegalArgumentException {
		if (!isValidDestination(destination))
			throw new IllegalArgumentException();
		this.destination = destination;
	}
	
	/**
	 * Return a destination of this teleporter.
	 *
	 * @return
	 *   | getDestinations().contains(result)
	 */
	@Basic @Raw 
	public Square teleport() {
		return destination;
	}

	/** 
	 * Return the destinations associated with this teleporter.
	 */
	@Basic
	public Set<Square> getDestinations() {
		Set<Square> destinations = new HashSet<Square>();
		destinations.add(destination);
		return destinations;
	}
	
	/**
	 * Checks whether the given destination is a valid destination for all 
	 * teleporters.
	 *
	 * @param destination
	 * The destination to check.
	 * @return
	 * True iff the given destination is effective.
	 *   | result == (destination != null)
	 */
	@Raw
	public static boolean isValidDestination(Square destination) {
		return destination != null;
	}

	/**
	 * Checks whether all destinations of this teleporter are valid 
	 * destinations.
	 *
	 * @return
	 *   | result == (getDestinations() != null
	 *   |		&amp;&amp; for each destination in getDestinations() :
	 *   |								isValidDestination(destination))
	 */
	@Raw
	public boolean hasValidDestinations() {
		if (getDestinations() == null)
			return false;
		for (Square destination : getDestinations()) {
			if (!isValidDestination(destination))
				return false;
		}
		return true;
	}

	/**
	 * Variable registering the destination for this teleporter.
	 */
	private final Square destination;
}
