package rpg;

import static org.junit.Assert.*;
import org.junit.*;

import rpg.exceptions.*;
import rpg.util.Direction;
import rpg.util.Temperature;

/**
 * A class collecting tests for the class of transparent squares.
 *
 * @author Roald Frederickx
 */
public class TransparentSquareTest {
    /**
     * Instance variable referencing transparent squares that may change during 
     * individual tests.
     */
    private TransparentSquare squareDefault;

    /**
     * Set up a mutable test fixture.
     */
    @Before
    public void setUpMutableFixture() {
        squareDefault = new TransparentSquare();
    }

    /** 
     * Asserts the class invariants of the given transparent square.
     *
     * @param square 
     * The transparent square to test.
     */
    private void assertClassInvariants(TransparentSquare square) {
        assertTrue(square.isNotRaw());
    }

    @Test
    public void defaultConstructor() {
        TransparentSquare squareDefault = new TransparentSquare();
        assertClassInvariants(squareDefault);
    }
    
    @Test
    public void extendedConstructor_LegalCase() {
        TransparentSquare extended = new TransparentSquare(
                new Temperature(25),
                new Temperature(-10), new Temperature(200),
                3000,
                Direction.WEST);
        assertEquals(25, extended.getTemperature().temperature(), 0);
        assertEquals(-10, extended.getMinTemperature().temperature(), 0);
        assertEquals(200, extended.getMaxTemperature().temperature(), 0);
        assertEquals(3000, extended.getHumidity());
        assertTrue(extended.getBorderAt(Direction.WEST).isDoor());
        assertClassInvariants(extended);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extendedConstructor_NonComplementaryDirections() {
        new TransparentSquare(new Temperature(25),
                new Temperature(-10), new Temperature(200),
                3000,
                Direction.NORTH, Direction.EAST);
    }
    @Test(expected = IllegalArgumentException.class)
    public void extendedConstructor_NullDirection() {
        new TransparentSquare(new Temperature(25),
                new Temperature(-10), new Temperature(200),
                3000,
                (Direction[]) null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void extendedConstructor_NoDirection() {
        new TransparentSquare(new Temperature(25),
                new Temperature(-10), new Temperature(200),
                3000);
    }
    @Test(expected = IllegalArgumentException.class)
    public void extendedConstructor_ThreeDirections() {
        new TransparentSquare(new Temperature(25),
                new Temperature(-10), new Temperature(200),
                3000,
                Direction.NORTH, Direction.SOUTH, Direction.EAST);
    }


    @Test
    public void changeBorderAt_BorderConstraints() {
        Door door = new Door(squareDefault, false);
        Border original = squareDefault.getBorderAt(Direction.DOWN);
        try {
            squareDefault.changeBorderAt(Direction.DOWN, door);
            assertTrue(false); //if there was no exception, we failed!
        } catch (BorderConstraintsException e) {
            //nop
        }
        assertEquals(original, squareDefault.getBorderAt(Direction.DOWN));
        assertClassInvariants(squareDefault);
    }
    @Test
    public void changeBorderAt_BorderConstraints_nonComplementaryDoors() {
        TransparentSquare doorNorth = new TransparentSquare(
                                new Temperature(10), 2000, Direction.NORTH);
        Door door = new Door(doorNorth, false);
        Border original = doorNorth.getBorderAt(Direction.DOWN);
        try {
            doorNorth.changeBorderAt(Direction.EAST, door);
            assertTrue(false); //if there was no exception, we failed!
        } catch (BorderConstraintsException e) {
            //nop
        }
        assertEquals(original, doorNorth.getBorderAt(Direction.DOWN));
        assertClassInvariants(doorNorth);
    }

    @Test
    public void canHaveAsBorderAt_Test() {
        Border terminatedBorder = squareDefault.getBorderAt(Direction.NORTH);
        Border openBorder = new OpenBorder(terminatedBorder);
        Border wall = new Wall(squareDefault, false);

        assertTrue(squareDefault.canHaveAsBorderAt(Direction.NORTH, openBorder));
        assertFalse(squareDefault.canHaveAsBorderAt(Direction.NORTH, wall));
        assertFalse(squareDefault.canHaveAsBorderAt(null, openBorder));
        assertFalse(squareDefault.canHaveAsBorderAt(Direction.NORTH, null));
        assertFalse(squareDefault.canHaveAsBorderAt(Direction.NORTH,
                                                        terminatedBorder));

        squareDefault.terminate();
        assertTrue(squareDefault.canHaveAsBorderAt(Direction.NORTH, null));
        assertFalse(squareDefault.canHaveAsBorderAt(Direction.NORTH, openBorder));
        assertFalse(squareDefault.canHaveAsBorderAt(Direction.NORTH,
                                                        terminatedBorder));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

