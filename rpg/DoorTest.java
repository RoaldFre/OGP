package rpg;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of doors.
 *
 * @author Roald Frederickx
 */
public class DoorTest {

    /**
     * Instance variables referencing objects that may change during 
     * individual tests.
     */
    private Square square1;
    private Square square2;
    private Border border1north;
    private Border border2south;
    private Door closedDoor;

    /**
     * Set up a mutable test fixture.
     */
    @Before
    public void setUpMutableFixture() {
        square1 = new RegularSquare(new Temperature(70), 2000);
        square2 = new RegularSquare(new Temperature(30), 8000);
        border1north = square1.getBorderAt(Direction.NORTH);
        border2south = square2.getBorderAt(Direction.SOUTH);
        
        Square tempSquare = new RegularSquare();
        closedDoor = new Door(tempSquare.getBorderAt(Direction.NORTH), false);
    }

    /** 
     * Asserts the class invariants of the given door.
     *
     * @param door 
     * The door to test.
     */
    public static void assertClassInvariants(Door door) {
        BorderTest.assertClassInvariants(door);
    }

    @Test
    public void constructor_fromBorder() {
        Door door = new Door(border1north, true);
        assertClassInvariants(door);
        assertTrue(door.bordersOnSquare(square1));
        BorderTest.testConstructor_fromBorder(border1north, door);
        assertFalse(door.isWall());
        assertTrue(door.isDoor());
        assertTrue(door.isOpen());
        assertFalse(door.isSlippery());
        assertFalse(door.isTerminated());
        assertTrue(border1north.isTerminated());
    }

    @Test
    public void constructor_fromSquare() {
        Door door = new Door(square1, true);
        assertTrue(door.bordersOnSquare(square1));
        BorderTest.testConstructor_fromSquare(door, square1);
        assertFalse(door.isWall());
        assertTrue(door.isDoor());
        assertTrue(door.isOpen());
        assertFalse(door.isSlippery());
        assertFalse(door.isTerminated());
    }

    @Test
    public void openAndClose() {
        assertFalse(closedDoor.isOpen());
        closedDoor.open();
        assertTrue(closedDoor.isOpen());
        closedDoor.close();
        assertFalse(closedDoor.isOpen());
    }

    @Test
    public void mergeWith_correctOrder_keep() {
        Border openBorder = new OpenBorder(border1north);
        Border door = new Door(border2south, false);
        
        Border mergedBorder = openBorder.mergeWith(door);

        BorderTest.assertClassInvariants(border1north);
        BorderTest.assertClassInvariants(border2south);
        BorderTest.assertClassInvariants(openBorder);
        BorderTest.assertClassInvariants(door);
        assertEquals(mergedBorder, square1.getBorderAt(Direction.NORTH));
        assertEquals(mergedBorder, square2.getBorderAt(Direction.SOUTH));
        assertTrue(mergedBorder.isDoor());
        assertTrue(mergedBorder.bordersOnSquare(square2));
        assertTrue(mergedBorder.bordersOnSquare(square1));
    }

    @Test
    public void mergeWith_correctOrder_replace() {
        Border wall = new Wall(border1north, false);
        Border door = new Door(border2south, false);
        
        Border mergedBorder = wall.mergeWith(door);

        BorderTest.assertClassInvariants(border1north);
        BorderTest.assertClassInvariants(border2south);
        BorderTest.assertClassInvariants(wall);
        BorderTest.assertClassInvariants(door);
        assertEquals(mergedBorder, square1.getBorderAt(Direction.NORTH));
        assertEquals(mergedBorder, square2.getBorderAt(Direction.SOUTH));
        assertTrue(mergedBorder.isWall());
        assertTrue(mergedBorder.bordersOnSquare(square2));
        assertTrue(mergedBorder.bordersOnSquare(square1));
    }

    @Test
    public void merged_open_equilibrate() {
        Border openBorder = new OpenBorder(border1north);
        Border door = new Door(border2south, false);
        Door mergedDoor = (Door) door.mergeWith(openBorder);
        mergedDoor.open();

        assertEquals(square1.getTemperature(), square2.getTemperature());
        assertEquals(square1.getHumidity(), square2.getHumidity());
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

