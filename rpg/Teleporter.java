package rpg;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class representing a teleporter involving a destination square.
 *
 * @invar
 * Each teleporter can have its destination as its destination.
 *   | canHaveAsDestination(getDestination())
 *
 * @author Roald Frederickx
 */
@Value
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
	 * Return the destination for this teleporter.
	 */
	@Basic @Raw @Immutable
	public Square getDestination() {
		return destination;
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
	 * Variable registering the destination for this teleporter.
	 */
	private final Square destination;
}
