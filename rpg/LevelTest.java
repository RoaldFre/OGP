package rpg;

import static org.junit.Assert.*;
import org.junit.*;
//import rpg.exceptions.*;

import rpg.util.Coordinate;
import rpg.util.CoordinateSystem;


public class LevelTest {

    private Level<Square> level_10;

    @Before
    public void setUpMutableFixture() {
        level_10 = new Level<Square>(10, 10);
    }

    private void assertClassInvariants(Level<?> level) {
        assertTrue(level.canHaveSquaresAtTheirCoordinates());
        assertTrue(level.squaresSatisfyConstraints());
        assertTrue(level.hasProperBorderingSquares());
        assertTrue(level.canHaveAsCoordSyst(level.getCoordSyst()));
        assertTrue(level.getSquareMapping() != null);
        assertTrue(level.canHaveAsRootDungeon(level.getRootDungeon()));
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
        CoordinateSystem coordSyst = level_10.getCoordSyst();
        Coordinate lo = coordSyst.getLowerBound();
        Coordinate hi = coordSyst.getUpperBound();

        coordSyst = new CoordinateSystem(new Coordinate(-1, -1, 0), hi);
        assertTrue(level_10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(new Coordinate(1, 1, 0), hi);
        assertFalse(level_10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(new Coordinate(-1, -1, -1), hi);
        assertFalse(level_10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(20, 20, 0));
        assertTrue(level_10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(1, 1, 0));
        assertFalse(level_10.canHaveAsCoordSyst(coordSyst));

        coordSyst = new CoordinateSystem(lo, new Coordinate(20, 20, 1));
        assertFalse(level_10.canHaveAsCoordSyst(coordSyst));

        assertFalse(level_10.canHaveAsCoordSyst(null));

    }
}

// vim: ts=4:sw=4:expandtab:smarttab

