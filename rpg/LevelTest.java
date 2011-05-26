package rpg;

import static org.junit.Assert.*;
import org.junit.*;
//import rpg.exceptions.*;

import rpg.util.Coordinate;
import rpg.util.CoordinateSystem;

/**
 * A class collecting tests for the class of level dungeons.
 *
 * @author Roald Frederickx
 */
public class LevelTest {

    private Level<Square> level_10_offset10;

    @Before
    public void setUpMutableFixture() {
        level_10_offset10 = new Level<Square>(new Coordinate(10, 10, 10),
                                              10, 10);
    }

    public static void assertClassInvariants(Level<?> level) {
        assertTrue(level.isNotRaw());
    }

    @Test
    public void constructor_dimensions_legal() {
        Level<Square> level = new Level<Square>(10, 10);
        assertEquals(new Coordinate(9, 9, 0),
                                level.getCoordSyst().getUpperBound());
        assertClassInvariants(level);
    }
    @Test (expected = IllegalArgumentException.class)
    public void constructor_dimensions_xNegative() {
        new Level<Square>(-1, 10);
    }
    @Test (expected = IllegalArgumentException.class)
    public void constructor_dimensions_yNegative() {
        new Level<Square>(10, -1);
    }

    @Test
    public void constructor_originAndDimensions_legal() {
        Level<Square> level = new Level<Square>(new Coordinate(1,2,3),10,10);
        assertEquals(new Coordinate(1, 2, 3),
                                level.getCoordSyst().getLowerBound());
        assertEquals(new Coordinate(10, 11, 3),
                                level.getCoordSyst().getUpperBound());
        assertClassInvariants(level);
    }

    @Test
    public void canHaveAsCoordSyst_test() {
        CoordinateSystem coordSyst = level_10_offset10.getCoordSyst();
        Coordinate lo = coordSyst.getLowerBound();
        Coordinate hi = coordSyst.getUpperBound();

        coordSyst = new CoordinateSystem(new Coordinate(9, 9, 10), hi);
        assertTrue(level_10_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(new Coordinate(11, 11, 10), hi);
        assertFalse(level_10_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(new Coordinate(10, 10, 9), hi);
        assertFalse(level_10_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(19, 19, 10));
        assertTrue(level_10_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(18, 18, 10));
        assertFalse(level_10_offset10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(19, 19, 11));
        assertFalse(level_10_offset10.canHaveAsCoordSyst(coordSyst));

        assertFalse(level_10_offset10.canHaveAsCoordSyst(null));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

