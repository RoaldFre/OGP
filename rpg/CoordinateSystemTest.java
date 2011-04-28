package rpg;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of coordinate systems.
 *
 * @author Roald Frederickx
 */
public class CoordinateSystemTest {

    private CoordinateSystem coordSyst_0_to_10; 
    private CoordinateSystem coordSyst_n10_to_10;   

    @Before
    public void setUpMutableFixture() {
        coordSyst_0_to_10 = new CoordinateSystem(new Coordinate(0, 0, 0),
                                                new Coordinate(10, 10, 10));
        coordSyst_n10_to_10 = new CoordinateSystem(new Coordinate(-10,-10,-10),
                                                new Coordinate(10, 10, 10));
    }

    private void assertClassInvariants(CoordinateSystem cs) {
        assertTrue(cs.canHaveAsLowerBound(cs.getLowerBound()));
        assertTrue(cs.canHaveAsUpperBound(cs.getUpperBound()));
    }

    @Test
    public void constructor_legal() {
        Coordinate lowerBound = new Coordinate(1, 2, 3);
        Coordinate upperBound = new Coordinate(9, 8, 7);
        CoordinateSystem coordSyst = 
                        new CoordinateSystem(lowerBound, upperBound);
        assertClassInvariants(coordSyst);
        assertEquals(lowerBound, coordSyst.getLowerBound());
        assertEquals(upperBound, coordSyst.getUpperBound());
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructor_nullLowerBound() {
        new CoordinateSystem(null, new Coordinate(9, 8, 7));
    }
    @Test (expected = IllegalArgumentException.class)
    public void constructor_nullUpperBound() {
        new CoordinateSystem(new Coordinate(1, 2, 3), null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void constructor_nonMatchingBounds() {
        new CoordinateSystem(new Coordinate(9, 8, 7),
                            new Coordinate(1, 2, 3));
    }

    @Test
    public void setLowerBound_legal() {
        Coordinate lowerBound = new Coordinate(-1, -2, -3);
        coordSyst_0_to_10.setLowerBound(lowerBound);
        assertEquals(lowerBound, coordSyst_0_to_10.getLowerBound());
        assertClassInvariants(coordSyst_0_to_10);
    }
    @Test (expected = IllegalArgumentException.class)
    public void setLowerBound_null() {
        coordSyst_0_to_10.setLowerBound(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void setLowerBound_stricterThenPrevious() {
        Coordinate lowerBound = new Coordinate(1, 2, 3);
        coordSyst_0_to_10.setLowerBound(lowerBound);
    }

    @Test
    public void setUpperBound_legal() {
        Coordinate upperBound = new Coordinate(11, 12, 13);
        coordSyst_0_to_10.setUpperBound(upperBound);
        assertEquals(upperBound, coordSyst_0_to_10.getUpperBound());
        assertClassInvariants(coordSyst_0_to_10);
    }
    @Test (expected = IllegalArgumentException.class)
    public void setUpperBound_null() {
        coordSyst_0_to_10.setUpperBound(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void setUpperBound_stricterThenPrevious() {
        Coordinate upperBound = new Coordinate(9, 8, 7);
        coordSyst_0_to_10.setUpperBound(upperBound);
    }

    @Test
    public void isValidCoordinate_true() {
        assertTrue(coordSyst_0_to_10.isValidCoordinate(
                                            new Coordinate(1, 2, 3)));
        assertTrue(coordSyst_n10_to_10.isValidCoordinate(
                                            new Coordinate(-1, -2, -3)));
    }
    @Test
    public void isValidCoordinate_false() {
        assertFalse(coordSyst_0_to_10.isValidCoordinate(
                                            new Coordinate(-1, -2, -3)));
        assertFalse(coordSyst_n10_to_10.isValidCoordinate(
                                            new Coordinate(11, 12, 13)));
    }

    @Test
    public void neighboursOf_legal() {
        for (Coordinate coord : coordSyst_0_to_10.neighboursOf(
                                    new Coordinate(1, 2, 3)).values())
            assertTrue(coordSyst_0_to_10.isValidCoordinate(coord));
    }
    @Test (expected = IllegalArgumentException.class)
    public void neighboursOf_null() {
        coordSyst_0_to_10.neighboursOf(null);
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

