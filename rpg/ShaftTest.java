package rpg;

import static org.junit.Assert.*;
import org.junit.*;
//import rpg.exceptions.*;

import rpg.util.Coordinate;
import rpg.util.CoordinateSystem;
import rpg.util.Direction;


public class ShaftTest {

    private Shaft<Square> shaft_10N;

    @Before
    public void setUpMutableFixture() {
        shaft_10N = new Shaft<Square>(10, Direction.NORTH);
    }

    public static void assertClassInvariants(Shaft<?> shaft) {
        assertTrue(shaft.isNotRaw());
    }

    @Test
    public void constructor_dimensions_legal() {
        Shaft<Square> shaft = new Shaft<Square>(10, Direction.NORTH);
        assertEquals(new Coordinate(0, 0, 0).moveTo(Direction.NORTH, 9),
                                shaft.getCoordSyst().getUpperBound());
        assertClassInvariants(shaft);
    }
    @Test (expected = IllegalArgumentException.class)
    public void constructor_directionNull() {
        new Shaft<Square>(10, null);
    }

    @Test
    public void constructor_originAndDimensions_legal() {
        Shaft<Square> shaft = new Shaft<Square>(new Coordinate(1, 2, 3),
													10, Direction.NORTH);
        assertEquals(new Coordinate(1, 2, 3),
                                shaft.getCoordSyst().getLowerBound());
        assertEquals(new Coordinate(1, 2, 3).moveTo(Direction.NORTH, 9),
                                shaft.getCoordSyst().getUpperBound());
        assertClassInvariants(shaft);
    }



    @Test
    public void canHaveAsCoordSyst_test() {
        CoordinateSystem coordSyst = shaft_10N.getCoordSyst();
        Coordinate lo = coordSyst.getLowerBound();
        Coordinate hi = coordSyst.getUpperBound();

        coordSyst = new CoordinateSystem(new Coordinate(0, -1, 0), hi);
        assertTrue(shaft_10N.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(new Coordinate(0, 1, 0), hi);
        assertFalse(shaft_10N.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(new Coordinate(-1, -1, -1), hi);
        assertFalse(shaft_10N.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(0, 20, 0));
        assertTrue(shaft_10N.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(1, 20, 0));
        assertFalse(shaft_10N.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(0, 20, 1));
        assertFalse(shaft_10N.canHaveAsCoordSyst(coordSyst));

        assertFalse(shaft_10N.canHaveAsCoordSyst(null));
    }


    @Test
    public void canHaveAsSquareAt_rock() {
        Coordinate coordinate010 = new Coordinate(0, 1, 0);
        Rock rock = new Rock();
        assertFalse(shaft_10N.canHaveAsSquareAt(coordinate010, rock));
    }
    @Test
    public void canHaveAsSquareAt_door() {
        Coordinate coordinate010 = new Coordinate(0, 1, 0);
        Coordinate coordinate020 = new Coordinate(0, 2, 0);
        TransparentSquare transDoorN = new TransparentSquare(Direction.NORTH);
        assertTrue(shaft_10N.canHaveAsSquareAt(coordinate010, transDoorN));
        TransparentSquare transDoorS = new TransparentSquare(Direction.SOUTH);
        assertTrue(shaft_10N.canHaveAsSquareAt(coordinate010, transDoorS));

        shaft_10N.addSquareAt(coordinate010, transDoorN);
        assertFalse(shaft_10N.canHaveAsSquareAt(coordinate020, transDoorS));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

