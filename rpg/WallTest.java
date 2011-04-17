package rpg;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of walls.
 *
 * @author Roald Frederickx
 */
public class WallTest {

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
	 * Asserts the class invariants of the given wall.
	 *
	 * @param wall 
	 * The wall to test.
	 */
	public static void assertClassInvariants(Wall wall) {
		BorderTest.assertClassInvariants(wall);
	}

	@Test
	public void constructor_fromBorder() {
		Wall wall = new Wall(border1north, true);
		assertTrue(wall.isSlippery());
		assertClassInvariants(wall);
		assertTrue(wall.bordersOnSquare(square1));
		BorderTest.testConstructor_fromBorder(border1north, wall);
		assertTrue(wall.isWall());
		assertFalse(wall.isDoor());
		assertFalse(wall.isOpen());
		assertFalse(wall.isTerminated());
		assertTrue(border1north.isTerminated());
	}

	@Test
	public void constructor_fromSquare() {
		Wall wall = new Wall(square1, true);
		assertTrue(wall.isSlippery());
		assertTrue(wall.bordersOnSquare(square1));
		BorderTest.testConstructor_fromSquare(wall, square1);
		assertTrue(wall.isWall());
		assertFalse(wall.isDoor());
		assertFalse(wall.isOpen());
		assertFalse(wall.isTerminated());
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
