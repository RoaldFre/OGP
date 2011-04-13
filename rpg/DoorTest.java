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
		square1 = new Square();
		square2 = new Square();
		border1north = square1.getBorderAt(Direction.NORTH);
		border2south = square2.getBorderAt(Direction.SOUTH);
		
		Square tempSquare = new Square();
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
		
		openBorder.mergeWith(door);

		BorderTest.assertClassInvariants(border1north);
		BorderTest.assertClassInvariants(border2south);
		BorderTest.assertClassInvariants(openBorder);
		BorderTest.assertClassInvariants(door);
		assertTrue(square1.getBorderAt(Direction.NORTH).equals(
									square2.getBorderAt(Direction.SOUTH)));
		assertTrue(square1.getBorderAt(Direction.NORTH).isDoor());

		assertTrue(square1.getBorderAt(Direction.NORTH).bordersOnSquare(
																square2));
		assertTrue(square2.getBorderAt(Direction.SOUTH).bordersOnSquare(
																square1));
	}

	@Test
	public void mergeWith_correctOrder_replace() {
		Border wall = new Wall(border1north, false);
		Border door = new Door(border2south, false);
		
		wall.mergeWith(door);

		BorderTest.assertClassInvariants(border1north);
		BorderTest.assertClassInvariants(border2south);
		BorderTest.assertClassInvariants(wall);
		BorderTest.assertClassInvariants(door);
		assertTrue(square1.getBorderAt(Direction.NORTH).equals(
									square2.getBorderAt(Direction.SOUTH)));
		assertTrue(square1.getBorderAt(Direction.NORTH).isWall());

		assertTrue(square1.getBorderAt(Direction.NORTH).bordersOnSquare(
																square2));
		assertTrue(square2.getBorderAt(Direction.SOUTH).bordersOnSquare(
																square1));
	}
}
