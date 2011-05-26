package rpg;

import static org.junit.Assert.*;
import org.junit.*;

import rpg.util.Direction;
import rpg.util.Temperature;

/**
 * A class collecting tests for the class of rocks.
 *
 * @author Roald Frederickx
 */
public class RockTest {

    private Rock rockDefault;

    /**
     * Set up a mutable test fixture.
     */
    @Before
    public void setUpMutableFixture() {
        rockDefault = new Rock();
    }

    /** 
     * Asserts the class invariants of the given rock.
     *
     * @param rock 
     * The rock to test.
     */
    public static void assertClassInvariants(Rock rock) {
        assertTrue(rock.isNotRaw());
    }

    @Test
    public void defaultConstructor() {
        Rock rockDefault = new Rock();
        assertEquals(0, rockDefault.getTemperature().temperature(), 0);
        assertEquals(0, rockDefault.getHumidity());
        assertClassInvariants(rockDefault);
    }
    
    @Test
    public void extendedConstructor_LegalCase() {
        Rock extended = new Rock(new Temperature(-10), new Temperature(200));
        assertEquals(0, extended.getTemperature().temperature(), 0);
        assertEquals(0, extended.getHumidity());
        assertEquals(-10, extended.getMinTemperature().temperature(), 0);
        assertEquals(200, extended.getMaxTemperature().temperature(), 0);
        assertClassInvariants(extended);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extendedConstructor_NullMinTemp() {
        new Rock(null, new Temperature(200));
    }

    @Test(expected = IllegalArgumentException.class)
    public void extendedConstructor_NullMaxTemp() {
        new Rock(new Temperature(-10), null);
    }

    @Test
    public void canHaveAsBorderAt_Test() {
        Border terminatedBorder = rockDefault.getBorderAt(Direction.NORTH);
        Wall wall = new Wall(terminatedBorder, false);
        Door door = new Door(rockDefault, false);

        assertTrue(rockDefault.canHaveAsBorderAt(Direction.NORTH, wall));
        assertFalse(rockDefault.canHaveAsBorderAt(Direction.NORTH, door));
        assertFalse(rockDefault.canHaveAsBorderAt(null, wall));
        assertFalse(rockDefault.canHaveAsBorderAt(Direction.NORTH, null));
        assertFalse(rockDefault.canHaveAsBorderAt(Direction.NORTH,
                                                        terminatedBorder));

        rockDefault.terminate();
        assertTrue(rockDefault.canHaveAsBorderAt(Direction.NORTH, null));
        assertFalse(rockDefault.canHaveAsBorderAt(Direction.NORTH, wall));
        assertFalse(rockDefault.canHaveAsBorderAt(Direction.NORTH,
                                                        terminatedBorder));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

