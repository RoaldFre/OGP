package rpg;

import static org.junit.Assert.*;
import org.junit.*;
import rpg.exceptions.*;

public class DungeonTest {

	private Dungeon dungeon_10;

	@Before
	public void setUpMutableFixture() {
		dungeon_10 = new Dungeon(new Coordinate(10, 10, 10));
	}

	private void assertClassInvariants(Dungeon dungeon) {
		assertTrue(dungeon.squaresSatisfyConstraints());
		assertTrue(dungeon.squaresHaveValidCoordinates());
	}

	@Test
	public void constructor_legal() {
		Coordinate farCorner = new Coordinate(1, 2, 3);
		Dungeon dungeon = new Dungeon(farCorner);
		assertEquals(farCorner, dungeon.getFarCorner());
		assertClassInvariants(dungeon);
	}
	@Test (expected = IllegalArgumentException.class)
	public void constructor_nullFarCorner() {
		new Dungeon(null);
	}
	@Test (expected = IllegalArgumentException.class)
	public void constructor_negativeFarCorner() {
		new Dungeon(new Coordinate(-1, -1, -1));
	}

	@Test
	public void setFarCorner_legal() {
		Coordinate farCorner = new Coordinate(10, 11, 12);
		dungeon_10.setFarCorner(farCorner);
		assertEquals(farCorner, dungeon_10.getFarCorner());
		assertClassInvariants(dungeon_10);
	}
	@Test (expected = IllegalArgumentException.class)
	public void setFarCorner_null() {
		dungeon_10.setFarCorner(null);
	}
	@Test (expected = IllegalArgumentException.class)
	public void setFarCorner_moreStrict() {
		dungeon_10.setFarCorner(new Coordinate(10, 10, 9));
	}

	@Test
	public void isValidSquareCoordinate_test() {
		assertTrue(dungeon_10.isValidSquareCoordinate(
									new Coordinate(1, 2, 3)));
		assertFalse(dungeon_10.isValidSquareCoordinate(
									new Coordinate(1, 1, 1)));
		assertFalse(dungeon_10.isValidSquareCoordinate(
									new Coordinate(-1, -2, -3)));
		assertFalse(dungeon_10.isValidSquareCoordinate(
									new Coordinate(11, 12, 13)));
		assertFalse(dungeon_10.isValidSquareCoordinate(null));
	}

	@Test
	public void addSquareAt_legal() {
		Coordinate coordinate = new Coordinate(1, 2, 3);
		Square square = new Square();
		new Wall(square.getBorderAt(Direction.DOWN), false);
		dungeon_10.addSquareAt(coordinate, square);
		assertTrue(dungeon_10.hasSquare(square));
		assertEquals(square, dungeon_10.getSquareAt(coordinate));
		assertClassInvariants(dungeon_10);
		assertEquals(1, dungeon_10.getNbSquares());
		assertEquals(0, dungeon_10.getNbSlipperySquares());
	}
	@Test (expected = IllegalArgumentException.class)
	public void addSquareAt_invalidSquare_null() {
		Coordinate coordinate = new Coordinate(1, 2, 3);
		dungeon_10.addSquareAt(coordinate, null);
	}
	@Test (expected = IllegalArgumentException.class)
	public void addSquareAt_invalidCoordinate_null() {
		Square square = new Square();
		dungeon_10.addSquareAt(null, square);
	}
	@Test (expected = IllegalArgumentException.class)
	public void addSquareAt_invalidCoordinate_allEqual() {
		Coordinate coordinate = new Coordinate(1, 1, 1);
		Square square = new Square();
		dungeon_10.addSquareAt(coordinate, square);
	}
	@Test (expected = DungeonConstraintsException.class)
	public void addSquareAt_tooManySlippery() {
		Coordinate coordinate = new Coordinate(1, 2, 3);
		Square square = new Square();
		new Wall(square.getBorderAt(Direction.DOWN), true);
		dungeon_10.addSquareAt(coordinate, square);
	}
	public void addSquareAt_tooManySlippery_checkNotAdded() {
		Coordinate coordinate = new Coordinate(1, 2, 3);
		Square square = new Square();
		new Wall(square.getBorderAt(Direction.DOWN), true);
		try {
			dungeon_10.addSquareAt(coordinate, square);
			assertTrue(false);
		} catch (DungeonConstraintsException e) {
			assertEquals(square, e.getSquare());
			assertEquals(dungeon_10, e.getDungeon());
			assertFalse(dungeon_10.hasSquare(square));
			assertClassInvariants(dungeon_10);
		}
	}
	@Test (expected = CoordinateOccupiedException.class)
	public void addSquareAt_isOccupied() {
		Coordinate coordinate1 = new Coordinate(1, 2, 3);
		Coordinate coordinate2 = new Coordinate(1, 2, 3);
		Square square1 = new Square();
		Square square2 = new Square();
		dungeon_10.addSquareAt(coordinate1, square1);
		dungeon_10.addSquareAt(coordinate2, square2);
	}

	@Test
	public void getSquareAt_legal() {
		Coordinate coordinate = new Coordinate(1, 2, 3);
		Square square = new Square();
		dungeon_10.addSquareAt(coordinate, square);
		assertEquals(square, dungeon_10.getSquareAt(coordinate));
		assertClassInvariants(dungeon_10);
	}
	@Test (expected = IllegalArgumentException.class)
	public void getSquareAt_invalidCoordinate_null() {
		dungeon_10.getSquareAt(null);
	}
	@Test (expected = IllegalArgumentException.class)
	public void getSquareAt_invalidCoordinate_allEqual() {
		Coordinate coordinate = new Coordinate(1, 1, 1);
		dungeon_10.getSquareAt(coordinate);
	}
	@Test (expected = CoordinateNotOccupiedException.class)
	public void getSquareAt_nonOccupiedCoordinate() {
		Coordinate coordinate = new Coordinate(1, 2, 3);
		dungeon_10.getSquareAt(coordinate);
	}

	@Test
	public void deleteSquareAt_legal() {
		Coordinate coordinate = new Coordinate(1, 2, 3);
		Square square = new Square();
		dungeon_10.addSquareAt(coordinate, square);
		dungeon_10.deleteSquareAt(coordinate);
		assertClassInvariants(dungeon_10);
		assertFalse(dungeon_10.hasSquare(square));
		assertFalse(dungeon_10.isOccupied(coordinate));
		assertTrue(square.isTerminated());
	}
	@Test (expected = IllegalArgumentException.class)
	public void deleteSquareAt_invalidCoordinate_null() {
		dungeon_10.deleteSquareAt(null);
	}
	@Test (expected = IllegalArgumentException.class)
	public void deleteSquareAt_invalidCoordinate_allEqual() {
		Coordinate coordinate = new Coordinate(1, 1, 1);
		dungeon_10.deleteSquareAt(coordinate);
	}
	@Test (expected = CoordinateNotOccupiedException.class)
	public void deleteSquareAt_nonOccupiedCoordinate() {
		Coordinate coordinate = new Coordinate(1, 2, 3);
		dungeon_10.deleteSquareAt(coordinate);
	}
}
