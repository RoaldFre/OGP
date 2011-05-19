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
	 * @effect
	 *   | setDestination(destination)
	 */
	public Teleporter(Square destination) {
		setDestination(destination);
	}
	
	/**
	 * Return the destination for this teleporter.
	 */
	@Basic @Raw @Immutable
	public Square getDestination() {
		return destination;
	}
	
	/**
	 * Set the destination for this teleporter to the given destination.
	 *
	 * @param destination
	 * The new destination for this teleporter.
	 * @pre
	 * The given destination must be a valid destination for this 
	 * teleporter.
	 *   | canHaveAsDestination(destination)
	 * @post
	 * The new destination for this teleporter is equal to the given 
	 * destination.
	 *   | new.getDestination() == destination
	 */
	@Raw @Model
	public void setDestination(Square destination) {
		assert canHaveAsDestination(destination);
		this.destination = destination;
	}
	
	/**
	 * Checks whether this teleporter can have the given destination as its 
	 * destination.
	 *
	 * @param destination
	 * The destination to check.
	 * @return
	 * True iff the given destination is effective.
	 *   | result == (destination != null)
	 */
	@Raw
	public boolean canHaveAsDestination(Square destination) {
		return destination != null;
	}
	
	/**
	 * Variable registering the destination for this teleporter.
	 */
	private Square destination;
}
