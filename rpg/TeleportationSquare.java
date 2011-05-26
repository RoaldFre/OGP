package rpg;

/**
 * An interface introducing methods for teleporting between squares.
 *
 * @author Roald Frederickx
 */
public interface TeleportationSquare extends Square {
    /**
     * Return a destination of this teleportation square.
     *
     * @return
     *   | result != null
     */
    public Square teleport();
}

// vim: ts=4:sw=4:expandtab:smarttab

