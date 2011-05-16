package rpg;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of borders.
 *
 * @author Roald Frederickx
 */
public class BorderTest {

    /**
     * Instance variables referencing objects that may change during 
     * individual tests.
     */
    private Square square1;
    private Square square2;
    private Border border1north;
    private Border border2south;

    /**
     * Set up a mutable test fixture.
     */
    @Before
    public void setUpMutableFixture() {
        square1 = new Square();
        square2 = new Square();
        border1north = square1.getBorderAt(Direction.NORTH);
        border2south = square2.getBorderAt(Direction.SOUTH);
    }

    @Test
    public void classInvariantsForBordersCreatedBySquare() {
        Square square = new Square();
        for (Direction direction : Direction.values())
            assertClassInvariants(square.getBorderAt(direction));
    }

    @Test
    public void mergeWith_Legal() {
        border1north.mergeWith(border2south);
        assertClassInvariants(border1north);
        assertClassInvariants(border2south);
        assertTrue(square1.getBorderAt(Direction.NORTH).equals(
                                    square2.getBorderAt(Direction.SOUTH)));
        Border mergedBorder = square1.getBorderAt(Direction.NORTH);
        assertTrue(square1.getBorderAt(Direction.NORTH).bordersOnSquare(
                                                                square2));
        assertTrue(square2.getBorderAt(Direction.SOUTH).bordersOnSquare(
                                                                square1));
        assertEquals(square1, mergedBorder.getNeighbour(square2));
        assertEquals(square2, mergedBorder.getNeighbour(square1));
    }

    /** 
     * Asserts the class invariants of the given border.
     *
     * @param border 
     * The border to test.
     */
    public static void assertClassInvariants(Border border) {
        assertTrue(border.hasNoDuplicateSquares());
        assertTrue(border.hasProperSquares());
        assertTrue(border.hasNoTerminatedSquares());
    }

    /** 
     * Asserts the postconditions of the constructor that made the given 
     * border using the given square.
     *
     * @param border 
     * The newly created border, using the given square.
     * @param square 
     * The square after creating the border.
     */
    public static void testConstructor_fromSquare(Border border,
                                                    Square square) {
        assertTrue(border.bordersOnSquare(square));
        assertFalse(border.isTerminated());
    }

    /** 
     * Asserts the postconditions of the constructor that made the given 
     * new border using the given old border.
     * <b>Note:</b> you need to check for yourself if the new border borders every 
     * square that the original oldborder bordered!
     *
     * @param oldBorder 
     * The border that was used to call the constructor.
     * @param newBorder 
     * The newly created border, using the given old border.
     */
    public static void testConstructor_fromBorder(Border oldBorder,
                                                    Border newBorder) {
        assertTrue(oldBorder.isTerminated());
        assertFalse(newBorder.isTerminated());
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

