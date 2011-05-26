package rpg.util;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A class collecting tests for the class of coordinate systems.
 *
 * @author Roald Frederickx
 */
public class CoordinateSystemTest {

    private CoordinateSystem coordSyst_0_to_20;
    private CoordinateSystem coordSyst_10_to_20;   

    @Before
    public void setUpMutableFixture() {
        coordSyst_0_to_20  = new CoordinateSystem(new Coordinate( 0,  0,  0),
                                                  new Coordinate(20, 20, 20));
        coordSyst_10_to_20 = new CoordinateSystem(new Coordinate(10, 10, 10),
                                                  new Coordinate(20, 20, 20));
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
    @Test (expected = IllegalArgumentException.class)
    public void constructor_negativeLowerBound() {
        new CoordinateSystem(new Coordinate(-1, -1, -1),
                             new Coordinate( 9,  8,  7));
    }

    @Test
    public void setLowerBound_legal() {
        Coordinate lowerBound = new Coordinate(1, 2, 3);
        coordSyst_10_to_20.setLowerBound(lowerBound);
        assertEquals(lowerBound, coordSyst_10_to_20.getLowerBound());
        assertClassInvariants(coordSyst_10_to_20);
    }
    @Test (expected = IllegalArgumentException.class)
    public void setLowerBound_null() {
        coordSyst_0_to_20.setLowerBound(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void setLowerBound_stricterThenPrevious() {
        Coordinate lowerBound = new Coordinate(1, 2, 3);
        coordSyst_0_to_20.setLowerBound(lowerBound);
    }

    @Test
    public void setUpperBound_legal() {
        Coordinate upperBound = new Coordinate(21, 22, 23);
        coordSyst_0_to_20.setUpperBound(upperBound);
        assertEquals(upperBound, coordSyst_0_to_20.getUpperBound());
        assertClassInvariants(coordSyst_0_to_20);
    }
    @Test (expected = IllegalArgumentException.class)
    public void setUpperBound_null() {
        coordSyst_0_to_20.setUpperBound(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void setUpperBound_stricterThenPrevious() {
        Coordinate upperBound = new Coordinate(9, 8, 7);
        coordSyst_0_to_20.setUpperBound(upperBound);
    }

    @Test
    public void containsCoordinate_true() {
        assertTrue(coordSyst_0_to_20.contains(new Coordinate(1, 2, 3)));
        assertFalse(coordSyst_10_to_20.contains(new Coordinate(1, 2, 3)));
        assertFalse(coordSyst_10_to_20.contains(new Coordinate(21, 22, 23)));
    }

    @Test
    public void neighboursOf_legal() {
        Coordinate coordinate = new Coordinate(1, 2, 3);
        for (Entry<Direction, Coordinate> e  : 
                    coordSyst_0_to_20.neighboursOf(coordinate).entrySet()) {
            assertEquals(coordinate.moveTo(e.getKey()), e.getValue());
            assertTrue(coordSyst_0_to_20.contains(e.getValue()));
        }

        Map<Direction, Coordinate> map = 
                        coordSyst_0_to_20.neighboursOf(Coordinate.ORIGIN);
        assertEquals(3, map.size());
        assertTrue(map.containsValue(new Coordinate(0, 0, 1)));
        assertTrue(map.containsValue(new Coordinate(0, 1, 0)));
        assertTrue(map.containsValue(new Coordinate(1, 0, 0)));
    }
    @Test (expected = IllegalArgumentException.class)
    public void neighboursOf_null() {
        coordSyst_0_to_20.neighboursOf(null);
    }

    @Test
    public void translate_test() {
        Coordinate offset = new Coordinate(1, 2, 3);
        coordSyst_0_to_20.translate(offset);
        assertEquals(coordSyst_0_to_20.getLowerBound(), offset);
        assertEquals(coordSyst_0_to_20.getUpperBound(),
                                                new Coordinate(21, 22, 23));
    }
    @Test (expected = IllegalArgumentException.class)
    public void translate_null() {
        coordSyst_0_to_20.translate(null);
    }
    
    @Test
    public void containsCoordSyst_test() {
        assertTrue(coordSyst_0_to_20.contains(coordSyst_10_to_20));
        assertTrue(coordSyst_0_to_20.contains(coordSyst_0_to_20));
        assertFalse(coordSyst_10_to_20.contains(coordSyst_0_to_20));
    }

    @Test
    public void iterator_test() {
        CoordinateSystem coordSyst2x2x0 = new CoordinateSystem(Coordinate.ORIGIN,
                                                        new Coordinate(1,1,0));
        Set<Coordinate> set = new HashSet<Coordinate>();
        for (Coordinate coord : coordSyst2x2x0)
            set.add(coord);
        assertEquals(4, set.size());
        assertTrue(set.contains(new Coordinate(0, 0, 0)));
        assertTrue(set.contains(new Coordinate(0, 1, 0)));
        assertTrue(set.contains(new Coordinate(1, 0, 0)));
        assertTrue(set.contains(new Coordinate(1, 1, 0)));
    }
}        

// vim: ts=4:sw=4:expandtab:smarttab

