package rpg;

import static org.junit.Assert.*;
import org.junit.*;
import rpg.exceptions.*;

/**
 * A class collecting tests for the class of regular squares.
 *
 * @author Roald Frederickx
 */
public class RegularSquareTest {
    /**
     * Instance variable referencing regular squares that may change during 
     * individual tests.
     */
    private RegularSquare squareDefault;

    /**
     * Set up a mutable test fixture.
     */
    @Before
    public void setUpMutableFixture() {
        squareDefault = new RegularSquare();
    }

    /** 
     * Asserts the class invariants of the given regular square.
     *
     * @param square 
     * The regular square to test.
     */
    private void assertClassInvariants(RegularSquare square) {
        assertTrue(Square.matchesMinTemperatureMax(
                        square.getMinTemperature(), 
                        square.getTemperature(),
                        square.getMaxTemperature()));
        assertTrue(Square.isValidHeatDamageThreshold(
                        Square.getHeatDamageThreshold()));
        assertTrue(Square.isValidHeatDamageStep(Square.getHeatDamageStep()));
        assertTrue(Square.isValidHumidity(square.getHumidity()));
        assertTrue(Square.isValidMergeTemperatureWeight(
                        Square.getMergeTemperatureWeight()));
        assertTrue(square.hasProperBorders());
        assertTrue(square.bordersSatisfyConstraints());
        assertTrue(square.hasNoDuplicateBorders());
    }

    @Test
    public void defaultConstructor() {
        RegularSquare squareDefault = new RegularSquare();
        assertClassInvariants(squareDefault);
    }
    
    @Test
    public void extendedConstructor_LegalCase() {
        RegularSquare extended = new RegularSquare(
                new Temperature(25),
                new Temperature(-10), new Temperature(200),
                3000,
                true);
        assertEquals(25, extended.getTemperature().temperature(), 0);
        assertEquals(-10, extended.getMinTemperature().temperature(), 0);
        assertEquals(200, extended.getMaxTemperature().temperature(), 0);
        assertEquals(3000, extended.getHumidity());
        assertTrue(extended.hasSlipperyFloor());
        assertClassInvariants(extended);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extendedConstructor_NullTemp() {
        new RegularSquare( null,
                new Temperature(-10), new Temperature(200),
                3000,
                true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extendedConstructor_NullMinTemp() {
        new RegularSquare(new Temperature(25),
                null, new Temperature(200),
                3000,
                true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void extendedConstructor_NullMaxTemp() {
        new RegularSquare(new Temperature(25),
                new Temperature(-10), null,
                3000,
                true);
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
    public void canHaveAsBorderAt_Test() {
        Border terminatedBorder = squareDefault.getBorderAt(Direction.NORTH);
        Border border = new Wall(terminatedBorder, false);

        assertTrue(squareDefault.canHaveAsBorderAt(Direction.NORTH, border));
        assertFalse(squareDefault.canHaveAsBorderAt(null, border));
        assertFalse(squareDefault.canHaveAsBorderAt(Direction.NORTH, null));
        assertFalse(squareDefault.canHaveAsBorderAt(Direction.NORTH,
                                                        terminatedBorder));

        squareDefault.terminate();
        assertTrue(squareDefault.canHaveAsBorderAt(Direction.NORTH, null));
        assertFalse(squareDefault.canHaveAsBorderAt(Direction.NORTH, border));
        assertFalse(squareDefault.canHaveAsBorderAt(Direction.NORTH,
                                                        terminatedBorder));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

