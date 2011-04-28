package rpg;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of open borders.
 *
 * @author Roald Frederickx
 */
public class OpenBorderTest {

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

    /** 
     * Asserts the class invariants of the given open border.
     *
     * @param openBorder 
     * The open border to test.
     */
    public static void assertClassInvariants(OpenBorder openBorder) {
        BorderTest.assertClassInvariants(openBorder);
    }

    @Test
    public void constructor_fromBorder() {
        OpenBorder openBorder = new OpenBorder(border1north);
        assertClassInvariants(openBorder);
        assertTrue(openBorder.bordersOnSquare(square1));
        BorderTest.testConstructor_fromBorder(border1north, openBorder);
        assertFalse(openBorder.isWall());
        assertFalse(openBorder.isDoor());
        assertTrue(openBorder.isOpen());
        assertFalse(openBorder.isSlippery());
        assertFalse(openBorder.isTerminated());
        assertTrue(border1north.isTerminated());
    }

    @Test
    public void constructor_fromSquare() {
        OpenBorder openBorder = new OpenBorder(square1);
        assertTrue(openBorder.bordersOnSquare(square1));
        BorderTest.testConstructor_fromSquare(openBorder, square1);
        assertFalse(openBorder.isWall());
        assertFalse(openBorder.isDoor());
        assertTrue(openBorder.isOpen());
        assertFalse(openBorder.isSlippery());
        assertFalse(openBorder.isTerminated());
    }

    @Test
    public void mergeWith_testCorrectOrder() {
        Border openBorder = new OpenBorder(border1north);
        Border wall = new Wall(border2south, false);
        
        openBorder.mergeWith(wall);

        BorderTest.assertClassInvariants(border1north);
        BorderTest.assertClassInvariants(border2south);
        BorderTest.assertClassInvariants(openBorder);
        BorderTest.assertClassInvariants(wall);
        assertTrue(square1.getBorderAt(Direction.NORTH).equals(
                                    square2.getBorderAt(Direction.SOUTH)));
        assertTrue(square1.getBorderAt(Direction.NORTH).isWall());
        assertTrue(square1.getBorderAt(Direction.NORTH).bordersOnSquare(
                                                                square2));
        assertTrue(square2.getBorderAt(Direction.SOUTH).bordersOnSquare(
                                                                square1));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

