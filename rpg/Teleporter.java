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
     * @pre
     *   | isValidDestination(destination)
     * @post
     *   | new.getDestination().equals(destination)
     */
    public Teleporter(Square destination) throws IllegalArgumentException {
        assert isValidDestination(destination);
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
     *   | result == (destination != null
     *   |          &amp;&amp; destination.isPossibleTeleportationEndPoint())
     */
    @Raw
    public static boolean isValidDestination(Square destination) {
        return destination != null
                    && destination.isPossibleTeleportationEndPoint();
    }

    /**
     * Checks whether all destinations of this teleporter are valid 
     * destinations.
     *
     * @return
     *   | result == (getDestinations() != null
     *   |      &amp;&amp; for each destination in getDestinations() :
     *   |                              isValidDestination(destination))
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

    /** 
     * Check whether this teleporter satisfies all its class invariants.
     */
    public boolean isNotRaw() {
        return hasValidDestinations();
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

