package rpg;

import static org.junit.Assert.*;
import org.junit.*;
import rpg.exceptions.*;

public class DungeonTest {

    private Dungeon<Square> level_10;
    private Dungeon<Square> level_10_withSquares;
    private Dungeon<Square> dungeonDemo;

    private Square square;
    private Square square_0_10_0;
    private Square square_1_10_0;
    private Square square_0_11_0;
    private Square square_1_11_0;
    private Square square_1_12_0;
    private Door door;

    private Square square1;
    private Square square2;
    private Square square3;
    private Square square4;
    private Square square5;
    private Square square6;
    private Coordinate coordinate1;
    private Coordinate coordinate2;
    private Coordinate coordinate3;
    private Coordinate coordinate4;
    private Coordinate coordinate5;
    private Coordinate coordinate6;

    @Before
    public void setUpMutableFixture() {
        level_10 = new Level<Square>(10, 10);

        square = new RegularSquare();

        level_10_withSquares = new Level<Square>(10, 10);
        coordinate1 = new Coordinate(1, 2, 0);
        coordinate2 = new Coordinate(1, 3, 0);
        coordinate3 = new Coordinate(1, 1, 0);
        coordinate4 = new Coordinate(2, 2, 0);
        coordinate5 = new Coordinate(0, 2, 0);
        coordinate6 = new Coordinate(8, 9, 0);
        square1 = new RegularSquare();
        square2 = new RegularSquare();
        square3 = new RegularSquare();
        square4 = new RegularSquare();
        square5 = new RegularSquare();
        square6 = new RegularSquare();
        level_10_withSquares.addSquareAt(coordinate1, square1);
        level_10_withSquares.addSquareAt(coordinate2, square2);
        level_10_withSquares.addSquareAt(coordinate3, square3);
        level_10_withSquares.addSquareAt(coordinate4, square4);
        level_10_withSquares.addSquareAt(coordinate5, square5);
        level_10_withSquares.addSquareAt(coordinate6, square6);


        square_0_10_0 = new RegularSquare(new Temperature(100), 5000);
        new Wall(square_0_10_0.getBorderAt(Direction.WEST));
        new Wall(square_0_10_0.getBorderAt(Direction.SOUTH));
        new Wall(square_0_10_0.getBorderAt(Direction.EAST));

        square_1_10_0 = new RegularSquare(new Temperature(90),  2500);
        new Wall(square_1_10_0.getBorderAt(Direction.SOUTH));
        new Wall(square_1_10_0.getBorderAt(Direction.WEST));
        new Wall(square_1_10_0.getBorderAt(Direction.NORTH));

        square_0_11_0 = new RegularSquare(new Temperature(100), 5000);
        new Wall(square_0_11_0.getBorderAt(Direction.WEST));
        new Wall(square_0_11_0.getBorderAt(Direction.NORTH));
        new Door(square_0_11_0.getBorderAt(Direction.EAST), false);

        square_1_11_0 = new RegularSquare(new Temperature(-30), 7000);
        new Wall(square_1_11_0.getBorderAt(Direction.EAST));

        square_1_12_0 = new RegularSquare(new Temperature(-30), 5000);
        new Wall(square_1_12_0.getBorderAt(Direction.EAST));
        new Wall(square_1_12_0.getBorderAt(Direction.WEST));

        dungeonDemo = new Level<Square>(20, 20);
        dungeonDemo.addSquareAt(new Coordinate(0, 10, 0), square_0_10_0);
        dungeonDemo.addSquareAt(new Coordinate(1, 10, 0), square_1_10_0);
        dungeonDemo.addSquareAt(new Coordinate(0, 11, 0), square_0_11_0);
        dungeonDemo.addSquareAt(new Coordinate(1, 11, 0), square_1_11_0);
        dungeonDemo.addSquareAt(new Coordinate(1, 12, 0), square_1_12_0);

        door = (Door) square_0_11_0.getBorderAt(Direction.EAST);
    }

    private void assertClassInvariants(Dungeon<?> dungeon) {
        assertTrue(dungeon.canHaveSquaresAtTheirCoordinates());
        assertTrue(dungeon.squaresSatisfyConstraints());
        assertTrue(dungeon.hasProperBorderingSquares());
        assertTrue(dungeon.canHaveAsCoordSyst(dungeon.getCoordSyst()));
        assertTrue(dungeon.getSquareMapping() != null);
        assertTrue(dungeon.canHaveAsRootDungeon(dungeon.getRootDungeon()));
    }


    @Test
    public void isValidSquareCoordinate_test() {
        assertTrue(level_10.isValidSquareCoordinate(
                                    new Coordinate(1, 2, 0)));
        assertFalse(level_10.isValidSquareCoordinate(
                                    new Coordinate(1, 2, 1)));
        assertFalse(level_10.isValidSquareCoordinate(
                                    new Coordinate(0, 0, 0)));
        assertFalse(level_10.isValidSquareCoordinate(
                                    new Coordinate(-1, -2, 0)));
        assertFalse(level_10.isValidSquareCoordinate(
                                    new Coordinate(11, 12, 0)));
        assertFalse(level_10.isValidSquareCoordinate(null));
    }

    @Test
    public void addSquareAt_legal() {
        Coordinate coordinate = new Coordinate(1, 2, 0);
        new Wall(square.getBorderAt(Direction.DOWN), false);
        level_10.addSquareAt(coordinate, square);
        assertTrue(level_10.hasSquare(square));
        assertEquals(square, level_10.getSquareAt(coordinate));
        assertClassInvariants(level_10);
        assertEquals(1, level_10.getNbSquares());
        assertEquals(0, level_10.getNbIntrinsicallySlipperySquares());
    }
    @Test
    public void addSquareAt_multipleSquares() {
        Coordinate coordinate = new Coordinate(2, 3, 0);
        level_10_withSquares.addSquareAt(coordinate, square);
        assertClassInvariants(level_10_withSquares);
    }

    @Test (expected = IllegalArgumentException.class)
    public void addSquareAt_invalidSquare_null() {
        Coordinate coordinate = new Coordinate(1, 2, 0);
        level_10.addSquareAt(coordinate, null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void addSquareAt_invalidCoordinate_null() {
        level_10.addSquareAt(null, square);
    }
    @Test (expected = IllegalArgumentException.class)
    public void addSquareAt_invalidCoordinate_allEqual() {
        Coordinate coordinate = new Coordinate(0, 0, 0);
        level_10.addSquareAt(coordinate, square);
    }
    @Test (expected = DungeonConstraintsException.class)
    public void addSquareAt_tooManySlippery() {
        Coordinate coordinate = new Coordinate(1, 2, 0);
        new Wall(square.getBorderAt(Direction.DOWN), true);
        level_10.addSquareAt(coordinate, square);
    }
    public void addSquareAt_tooManySlippery_checkNotAdded() {
        Coordinate coordinate = new Coordinate(1, 2, 0);
        new Wall(square.getBorderAt(Direction.DOWN), true);
        try {
            level_10.addSquareAt(coordinate, square);
            assertTrue(false);
        } catch (DungeonConstraintsException e) {
            assertEquals(square, e.getSquare());
            assertEquals(level_10, e.getDungeon());
            assertFalse(level_10.hasSquare(square));
            assertClassInvariants(level_10);
        }
    }
    @Test (expected = CoordinateOccupiedException.class)
    public void addSquareAt_isOccupied() {
        Coordinate coordinate1 = new Coordinate(1, 2, 0);
        Coordinate coordinate2 = new Coordinate(1, 2, 0);
        Square square1 = new RegularSquare();
        Square square2 = new RegularSquare();
        level_10.addSquareAt(coordinate1, square1);
        level_10.addSquareAt(coordinate2, square2);
    }

    @Test
    public void getSquareAt_legal() {
        Coordinate coordinate = new Coordinate(1, 2, 0);
        level_10.addSquareAt(coordinate, square);
        assertEquals(square, level_10.getSquareAt(coordinate));
        assertClassInvariants(level_10);
    }
    @Test (expected = IllegalArgumentException.class)
    public void getSquareAt_invalidCoordinate_null() {
        level_10.getSquareAt(null);
    }
    @Test (expected = CoordinateNotOccupiedException.class)
    public void getSquareAt_nonOccupiedCoordinate() {
        Coordinate coordinate = new Coordinate(1, 2, 0);
        level_10.getSquareAt(coordinate);
    }

    @Test
    public void deleteSquareAt_legal() {
        Coordinate coordinate = new Coordinate(1, 2, 0);
        level_10.addSquareAt(coordinate, square);
        level_10.deleteSquareAt(coordinate);
        assertClassInvariants(level_10);
        assertFalse(level_10.hasSquare(square));
        assertFalse(level_10.isOccupied(coordinate));
        assertTrue(square.isTerminated());
    }
    @Test
    public void deleteSquareAt_multipleSquares() {
        Coordinate coordinate = new Coordinate(2, 3, 0);
        level_10_withSquares.addSquareAt(coordinate, square);
        level_10_withSquares.deleteSquareAt(coordinate);
        assertClassInvariants(level_10_withSquares);
        assertFalse(level_10_withSquares.hasSquare(square));
        assertFalse(level_10_withSquares.isOccupied(coordinate));
        assertTrue(square.isTerminated());
    }
    @Test (expected = IllegalArgumentException.class)
    public void deleteSquareAt_invalidCoordinate_null() {
        level_10.deleteSquareAt(null);
    }
    @Test (expected = CoordinateNotOccupiedException.class)
    public void deleteSquareAt_nonOccupiedCoordinate() {
        Coordinate coordinate = new Coordinate(1, 2, 0);
        level_10.deleteSquareAt(coordinate);
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
        Coordinate nonOccupiedCoordinate = new Coordinate(5,6,0);
        Coordinate invalidCoordinateNegative = new Coordinate(-1, -2, 0);
        Coordinate invalidCoordinateTooFar = new Coordinate(50, 60, 0);
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

    @Test
    public void overlaps_test() {
        assertTrue(level_10.overlaps(level_10_withSquares));
        assertTrue(level_10.overlaps(level_10_withSquares.getCoordSyst()));
    }

}

// vim: ts=4:sw=4:expandtab:smarttab

