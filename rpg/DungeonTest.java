package rpg;

import static org.junit.Assert.*;
import org.junit.*;
import rpg.exceptions.*;
import rpg.util.Coordinate;

public class DungeonTest {

    private Dungeon<Square> level_10;
    private Dungeon<Square> level_10_withSquares;

    private Square square;

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

        Level<Square> l10ws = new Level<Square>(10, 10);
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
        l10ws.addSquareAt(coordinate1, square1);
        l10ws.addSquareAt(coordinate2, square2);
        l10ws.addSquareAt(coordinate3, square3);
        l10ws.addSquareAt(coordinate4, square4);
        l10ws.addSquareAt(coordinate5, square5);
        l10ws.addSquareAt(coordinate6, square6);
        level_10_withSquares = l10ws;
    }

    public static void assertClassInvariants(Dungeon<?> dungeon) {
        assertTrue(dungeon.isNotRaw());
    }

    @Test
    public void getSquareAt_legal() {
        Coordinate coordinate = new Coordinate(1, 2, 0);
        ((Level<Square>) level_10).addSquareAt(coordinate, square);
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
        ((Level<Square>) level_10).addSquareAt(coordinate, square);
        level_10.deleteSquareAt(coordinate);
        assertClassInvariants(level_10);
        assertFalse(level_10.hasSquare(square));
        assertFalse(level_10.isOccupied(coordinate));
        assertTrue(square.isTerminated());
    }
    @Test
    public void deleteSquareAt_multipleSquares() {
        Coordinate coordinate = new Coordinate(2, 3, 0);
        ((Level<Square>) level_10_withSquares).addSquareAt(coordinate, square);
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
    public void overlaps_test() {
        assertTrue(level_10.overlaps(level_10_withSquares));
        assertTrue(level_10.overlaps(level_10_withSquares.getCoordSyst()));
    }

}

// vim: ts=4:sw=4:expandtab:smarttab

