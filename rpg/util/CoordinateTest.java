package rpg.util;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of coordinates.
 *
 * @author Roald Frederickx
 */
public class CoordinateTest {

    private static Coordinate origin;
    private static Coordinate coord_10_20_30;
    private static Coordinate coord_n10_n20_n30;

    @BeforeClass
    public static void setUpImmutableFixture() {
        origin = Coordinate.ORIGIN;
        coord_10_20_30 = new Coordinate(10, 20, 30);
        coord_n10_n20_n30 = new Coordinate(-10, -20, -30);
    }

    @Test
    public void equals_true() {
        assertTrue(origin.equals(new Coordinate(0, 0, 0)));
        assertTrue(coord_10_20_30.equals(new Coordinate(10, 20, 30)));
    }

    @Test
    public void equals_false() {
        assertFalse(coord_10_20_30.equals(origin));
        assertFalse(origin.equals(coord_10_20_30));
    }
    
    @Test
    public void hashCode_identical() {
        assertEquals(coord_10_20_30.hashCode(),
            (new Coordinate(10, 20, 30).hashCode()));
    }

    @Test
    public void moveTo_legal() {
        assertEquals(new Coordinate( 1, 0, 0),origin.moveTo(Direction.EAST));
        assertEquals(new Coordinate(-1, 0, 0),origin.moveTo(Direction.WEST));
        assertEquals(new Coordinate( 0, 1, 0),origin.moveTo(Direction.NORTH));
        assertEquals(new Coordinate( 0,-1, 0),origin.moveTo(Direction.SOUTH));
        assertEquals(new Coordinate( 0, 0, 1),origin.moveTo(Direction.UP));
        assertEquals(new Coordinate( 0, 0,-1),origin.moveTo(Direction.DOWN));
    }
    @Test (expected = IllegalArgumentException.class)
    public void moveTo_null() {
        origin.moveTo(null);
    }

    @Test
    public void isBoundedBy_test() {
        assertTrue(origin.isBoundedBy(coord_n10_n20_n30, coord_10_20_30));
        assertFalse(origin.isBoundedBy(coord_10_20_30, coord_n10_n20_n30));
        assertTrue(origin.isBoundedBy(origin, origin));
    }

    @Test
    public void add_test() {
        assertEquals(origin, coord_10_20_30.add(coord_n10_n20_n30));
        assertEquals(coord_10_20_30, coord_10_20_30.add(origin));
    }

    @Test
    public void sub_test() {
        assertEquals(origin, coord_10_20_30.sub(coord_10_20_30));
        assertEquals(coord_10_20_30, coord_10_20_30.sub(origin));
    }

    @Test
    public void mirror_test() {
        assertEquals(origin, origin.mirror());
        assertEquals(coord_n10_n20_n30, coord_10_20_30.mirror());
    }

}


// vim: ts=4:sw=4:expandtab:smarttab

