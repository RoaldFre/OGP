package rpg;

import static org.junit.Assert.*;
import org.junit.*;
import rpg.exceptions.*;
import rpg.util.Coordinate;
import rpg.util.CoordinateSystem;

public class LeafDungeonTest {

    private LeafDungeon<Square> level_10;
    private LeafDungeon<Square> level_10_withSquares;

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
    }

    private void assertClassInvariants(LeafDungeon<? extends Square> ld) {
        assertTrue(ld.canHaveSquaresAtTheirCoordinates());
        assertTrue(ld.squaresSatisfyConstraints());
        assertTrue(ld.hasProperBorderingSquares());
        assertTrue(ld.canHaveAsCoordSyst(ld.getCoordSyst()));
        assertTrue(ld.getSquareMapping() != null);
        assertTrue(ld.canHaveAsRootDungeon(ld.getRootDungeon()));
    }

    @Test
    public void translate_legal() {
        LeafDungeon<?> l10ws = level_10_withSquares; //shorten name for 80 col
        Coordinate offset = new Coordinate(0, 0, 10);
        l10ws.translate(offset);
        assertClassInvariants(l10ws);
        assertEquals(square1, l10ws.getSquareAt(coordinate1.add(offset)));
        assertEquals(square2, l10ws.getSquareAt(coordinate2.add(offset)));
        assertEquals(square3, l10ws.getSquareAt(coordinate3.add(offset)));
        assertEquals(square4, l10ws.getSquareAt(coordinate4.add(offset)));
        assertEquals(square5, l10ws.getSquareAt(coordinate5.add(offset)));
        assertEquals(square6, l10ws.getSquareAt(coordinate6.add(offset)));
		assertEquals(offset, l10ws.getCoordSyst().getLowerBound());
        assertEquals(new Coordinate(9, 9, 0).add(offset),
									l10ws.getCoordSyst().getUpperBound());
    }
    @Test (expected = IllegalArgumentException.class)
    public void translate_null() {
        level_10_withSquares.translate(null);
    }
    @Test
    public void translate_coordinateConstraint_properRollBack() {
        LeafDungeon<?> l10ws = level_10_withSquares; //shorten name for 80 col
        CoordinateSystem coordSyst = l10ws.getCoordSyst();
        Coordinate oldLower = coordSyst.getLowerBound();
        Coordinate oldUpper = coordSyst.getUpperBound();
        Coordinate offset = new Coordinate(0, 0, 2);
        try {
            l10ws.translate(offset);
            assertTrue(false); //if we got here: we failed...
        } catch (CoordinateConstraintsException e) {
            //nop
        }
        assertClassInvariants(l10ws);
        assertEquals(square1, l10ws.getSquareAt(coordinate1));
        assertEquals(square2, l10ws.getSquareAt(coordinate2));
        assertEquals(square3, l10ws.getSquareAt(coordinate3));
        assertEquals(square4, l10ws.getSquareAt(coordinate4));
        assertEquals(square5, l10ws.getSquareAt(coordinate5));
        assertEquals(square6, l10ws.getSquareAt(coordinate6));
		assertEquals(oldLower, l10ws.getCoordSyst().getLowerBound());
        assertEquals(oldUpper, l10ws.getCoordSyst().getUpperBound());
    }


    @Test (expected = IllegalArgumentException.class)
    public void getDirectionsAndNeighboursOf_null() {
        level_10_withSquares.getDirectionsAndNeighboursOf(null);
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

