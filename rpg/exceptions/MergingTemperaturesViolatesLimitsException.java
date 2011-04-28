package rpg.exceptions;

/**
 * A class for signalling a violation of the temperature limits of a square 
 * as a result of trying to merge squares together.
 * 
 * @author Roald Frederickx
 */
public class MergingTemperaturesViolatesLimitsException extends RuntimeException {
    /** 
     * Create a new merging temperatures violates limits exception.
     *
     * @effect
     *   | super("Merging the squares would violate the temperature "
     *   |                                      + "limits of a square!");
     */
    public MergingTemperaturesViolatesLimitsException() {
        super("Merging the squares would violate the temperature limits of a square!");
    }

    /** 
     * Create a new merging temperatures violates limits exception with the 
     * given message.
     *
     * @effect
     *   | super(message)
     */
    public MergingTemperaturesViolatesLimitsException(String message) {
        super(message);
    }

    static final long serialVersionUID = 1;
}


// vim: ts=4:sw=4:expandtab:smarttab

