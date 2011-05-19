package rpg;

import static org.junit.Assert.*;
import org.junit.*;
//import rpg.exceptions.*;

public class LevelTest {

    private Level<Square> level_10;

    @Before
    public void setUpMutableFixture() {
        level_10 = new Level<Square>(10, 10);
    }

    private void assertClassInvariants(Level<?> level) {
        assertTrue(level.squaresSatisfyConstraints());
        assertTrue(level.canHaveSquaresAtTheirCoordinates());
        assertTrue(level.hasProperBorderingSquares());
        assertTrue(Dungeon.isValidCoordSyst(level.getCoordSyst()));
    }

    @Test
    public void constructor_legal() {
        Level<Square> level = new Level<Square>(10, 10);
        assertEquals(new Coordinate(10, 10, 0), level.getFarCorner());
        assertClassInvariants(level);
    }
    @Test (expected = IllegalArgumentException.class)
    public void constructor_xNegative() {
        new Level<Square>(-1, 10);
    }
    @Test (expected = IllegalArgumentException.class)
    public void constructor_yNegative() {
        new Level<Square>(10, -1);
    }

    @Test
    public void canHaveAsOrigin_test() {
        assertTrue (level_10.canHaveAsOrigin(new Coordinate(-1, -1,  0)));
        assertFalse(level_10.canHaveAsOrigin(new Coordinate( 1,  1,  0)));
        assertFalse(level_10.canHaveAsOrigin(new Coordinate(-1, -1, -1)));
        assertFalse(level_10.canHaveAsOrigin(null));
    }
    @Test
    public void canHaveAsFarCorner_test() {
        assertTrue (level_10.canHaveAsFarCorner(new Coordinate(20, 20, 0)));
        assertFalse(level_10.canHaveAsFarCorner(new Coordinate( 1,  1, 0)));
        assertFalse(level_10.canHaveAsFarCorner(new Coordinate(20, 20, 1)));
        assertFalse(level_10.canHaveAsFarCorner(null));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

