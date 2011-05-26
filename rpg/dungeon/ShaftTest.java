package rpg.dungeon;

import rpg.square.*;
import rpg.util.*;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of shafts.
 *
 * @author Roald Frederickx
 */
public class ShaftTest {

    private Shaft<Square> shaft_10N;
    private Shaft<Square> shaft_10N_offset10;

    @Before
    public void setUpMutableFixture() {
        shaft_10N = new Shaft<Square>(10, Direction.NORTH);
        shaft_10N_offset10 = new Shaft<Square>(new Coordinate(10, 10, 10),
                                               10, Direction.NORTH);
    }

    public static void assertClassInvariants(Shaft<?> shaft) {
        assertTrue(shaft.isNotRaw());
    }

    @Test
    public void constructor_dimensions_legal() {
        Shaft<Square> dommeSchacht = new Shaft<Square>(10, Direction.NORTH);
        assertEquals(new Coordinate(0, 0, 0).moveTo(Direction.NORTH, 9),
                                dommeSchacht.getCoordSyst().getUpperBound());
        assertClassInvariants(dommeSchacht);
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
        CoordinateSystem coordSyst = shaft_10N_offset10.getCoordSyst();
        Coordinate lo = coordSyst.getLowerBound();
        Coordinate hi = coordSyst.getUpperBound();

        coordSyst = new CoordinateSystem(new Coordinate(10, 9, 10), hi);
        assertTrue(shaft_10N_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(new Coordinate(10, 11, 10), hi);
        assertFalse(shaft_10N_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(new Coordinate(9, 9, 9), hi);
        assertFalse(shaft_10N_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(10, 20, 10));
        assertTrue(shaft_10N_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(11, 20, 10));
        assertFalse(shaft_10N_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(10, 20, 11));
        assertFalse(shaft_10N_offset10.canHaveAsCoordSyst(coordSyst));

        assertFalse(shaft_10N_offset10.canHaveAsCoordSyst(null));
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

