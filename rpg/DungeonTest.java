package rpg;

import static org.junit.Assert.*;
import org.junit.*;
import rpg.exceptions.*;

public class DungeonTest {

	private Dungeon dungeon_10;
	private Dungeon dungeon_10_withSquares;
	private Dungeon dungeonDemo;

	private Square square_0_10_0;
	private Square square_1_10_0;
	private Square square_0_11_0;
	private Square square_1_11_0;
	private Square square_1_12_0;
	private Door door;

	@Before
	public void setUpMutableFixture() {
		dungeon_10 = new Dungeon(new Coordinate(10, 10, 10));


		dungeon_10_withSquares = new Dungeon(new Coordinate(10, 10, 10));
		Coordinate coordinate1 = new Coordinate(1, 2, 3);
		Coordinate coordinate2 = new Coordinate(1, 2, 4);
		Coordinate coordinate3 = new Coordinate(1, 2, 2);
		Coordinate coordinate4 = new Coordinate(1, 3, 3);
		Coordinate coordinate5 = new Coordinate(1, 1, 3);
		Coordinate coordinate6 = new Coordinate(2, 2, 3);
		Coordinate coordinate7 = new Coordinate(0, 2, 3);
		Coordinate coordinate8 = new Coordinate(8, 9, 10);
		Square square1 = new Square();
		Square square2 = new Square();
		Square square3 = new Square();
		Square square4 = new Square();
		Square square5 = new Square();
		Square square6 = new Square();
		Square square7 = new Square();
		Square square8 = new Square();
		dungeon_10_withSquares.addSquareAt(coordinate1, square1);
		dungeon_10_withSquares.addSquareAt(coordinate2, square2);
		dungeon_10_withSquares.addSquareAt(coordinate3, square3);
		dungeon_10_withSquares.addSquareAt(coordinate4, square4);
		dungeon_10_withSquares.addSquareAt(coordinate5, square5);
		dungeon_10_withSquares.addSquareAt(coordinate6, square6);
		dungeon_10_withSquares.addSquareAt(coordinate7, square7);
		dungeon_10_withSquares.addSquareAt(coordinate8, square8);


		square_0_10_0 = new Square(new Temperature(100), 5000);
		new Wall(square_0_10_0.getBorderAt(Direction.WEST));
		new Wall(square_0_10_0.getBorderAt(Direction.SOUTH));
		new Wall(square_0_10_0.getBorderAt(Direction.EAST));

		square_1_10_0 = new Square(new Temperature(90),  2500);
		new Wall(square_1_10_0.getBorderAt(Direction.SOUTH));
		new Wall(square_1_10_0.getBorderAt(Direction.WEST));
		new Wall(square_1_10_0.getBorderAt(Direction.NORTH));

		square_0_11_0 = new Square(new Temperature(100), 5000);
		new Wall(square_0_11_0.getBorderAt(Direction.WEST));
		new Wall(square_0_11_0.getBorderAt(Direction.NORTH));
		new Door(square_0_11_0.getBorderAt(Direction.EAST), false);

		square_1_11_0 = new Square(new Temperature(-30), 7000);
		new Wall(square_1_11_0.getBorderAt(Direction.EAST));

		square_1_12_0 = new Square(new Temperature(-30), 5000);
		new Wall(square_1_12_0.getBorderAt(Direction.EAST));
		new Wall(square_1_12_0.getBorderAt(Direction.WEST));

		dungeonDemo = new Dungeon(new Coordinate(20, 20, 20));
		dungeonDemo.addSquareAt(new Coordinate(0, 10, 0), square_0_10_0);
		dungeonDemo.addSquareAt(new Coordinate(1, 10, 0), square_1_10_0);
		dungeonDemo.addSquareAt(new Coordinate(0, 11, 0), square_0_11_0);
		dungeonDemo.addSquareAt(new Coordinate(1, 11, 0), square_1_11_0);
		dungeonDemo.addSquareAt(new Coordinate(1, 12, 0), square_1_12_0);

		door = (Door) square_0_11_0.getBorderAt(Direction.EAST);
	}

	private void assertClassInvariants(Dungeon dungeon) {
		assertTrue(dungeon.squaresSatisfyConstraints());
		assertTrue(dungeon.squaresHaveValidCoordinates());
		assertTrue(dungeon.hasProperBorderingSquares());
		assertTrue(dungeon.hasNoTerminatedSquares());
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
	@Test
	public void addSquareAt_multipleSquares() {
		Coordinate coordinate = new Coordinate(2, 2, 4);
		Square square = new Square();
		dungeon_10_withSquares.addSquareAt(coordinate, square);
		assertClassInvariants(dungeon_10_withSquares);
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
	@Test
	public void deleteSquareAt_multipleSquares() {
		Coordinate coordinate = new Coordinate(2, 2, 4);
		Square square = new Square();
		dungeon_10_withSquares.addSquareAt(coordinate, square);
		dungeon_10_withSquares.deleteSquareAt(coordinate);
		assertClassInvariants(dungeon_10_withSquares);
		assertFalse(dungeon_10_withSquares.hasSquare(square));
		assertFalse(dungeon_10_withSquares.isOccupied(coordinate));
		assertTrue(square.isTerminated());
	}
	@Test (expected = IllegalArgumentException.class)
	public void deleteSquareAt_invalidCoordinate_null() {
		dungeon_10.deleteSquareAt(null);
	}
	@Test (expected = CoordinateNotOccupiedException.class)
	public void deleteSquareAt_nonOccupiedCoordinate() {
		Coordinate coordinate = new Coordinate(1, 2, 3);
		dungeon_10.deleteSquareAt(coordinate);
	}

	@Test
	public void canReach_legalTests() {
		Coordinate c_0_10_0 = new Coordinate(0, 10, 0);
		Coordinate c_1_12_0 = new Coordinate(1, 12, 0);
		Coordinate c_1_10_0 = new Coordinate(1, 10, 0);

		/* Separated by door (and open borders in between) */
		assertFalse(dungeonDemo.canReach(c_0_10_0, c_1_12_0));
		door.open();
		assertTrue(dungeonDemo.canReach(c_0_10_0, c_1_12_0));

		/* Separated by wall */
		assertFalse(dungeonDemo.canReach(c_0_10_0, c_1_10_0));
		assertFalse(dungeonDemo.canReach(c_1_12_0, c_1_10_0));

		/* Non-occupied or invalid coordinates for dungeon */
		Coordinate nonOccupiedCoordinate = new Coordinate(5,6,7);
		Coordinate invalidCoordinateNegative = new Coordinate(-1, -2, -3);
		Coordinate invalidCoordinateTooFar = new Coordinate(50, 60, 70);
		assertFalse(dungeonDemo.canReach(c_0_10_0, nonOccupiedCoordinate));
		assertFalse(dungeonDemo.canReach(c_0_10_0, invalidCoordinateNegative));
		assertFalse(dungeonDemo.canReach(c_0_10_0, invalidCoordinateTooFar));

		/* Equal coordinates */
		assertTrue(dungeonDemo.canReach(c_0_10_0, c_0_10_0));
		assertFalse(dungeonDemo.canReach(nonOccupiedCoordinate,
												nonOccupiedCoordinate));
		assertFalse(dungeonDemo.canReach(invalidCoordinateTooFar,
												invalidCoordinateTooFar));
		assertFalse(dungeonDemo.canReach(invalidCoordinateNegative,
												invalidCoordinateNegative));
	}
	@Test (expected = IllegalArgumentException.class)
	public void canReach_destinationIsNull() {
		Coordinate c_0_10_0 = new Coordinate(0, 10, 0);
		dungeonDemo.canReach(c_0_10_0, null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void canReach_sourceIsNull() {
		Coordinate c_0_10_0 = new Coordinate(0, 10, 0);
		dungeonDemo.canReach(null, c_0_10_0);
	}
	@Test (expected = IllegalArgumentException.class)
	public void canReach_bothAreNull() {
		dungeonDemo.canReach(null, null);
	}
}
