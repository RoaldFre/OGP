package rpg.exceptions;

/**
 * A class for signalling a violation of the temperature or humidity limits 
 * of a square as a result of trying to equilibrate the squares of an area.
 * 
 * @author Roald Frederickx
 */
public class EquilibratingSquaresViolatesLimitsException extends RuntimeException {
    /** 
     * Create a new equilibrating squares violates limits exception.
     *
     * @effect
     *   | super("Merging the squares would violate the temperature "
     *   |                                      + "limits of a square!");
     */
    public EquilibratingSquaresViolatesLimitsException() {
		super("Equilibrating the squares would violate the temperature "
				+ "limits or humidity limins of a square!");
    }

    /** 
     * Create a new equilibrating squares violates limits exception with the 
     * given message.
     *
     * @effect
     *   | super(message)
     */
    public EquilibratingSquaresViolatesLimitsException(String message) {
        super(message);
    }

    static final long serialVersionUID = 1;
}


// vim: ts=4:sw=4:expandtab:smarttab

