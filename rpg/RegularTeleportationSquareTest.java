package rpg;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of regular teleportation squares.
 *
 * @author Roald Frederickx
 */
public class RegularTeleportationSquareTest {
    /**
	 * Instance variable referencing regular teleportation squares that may 
	 * change during individual tests.
     */
    private RegularTeleportationSquare squareDefault;
    private Square destination;

    /**
     * Set up a mutable test fixture.
     */
    @Before
    public void setUpMutableFixture() {
		destination = new RegularSquare();
        squareDefault = new RegularTeleportationSquare(
											new Teleporter(destination));
    }

    /** 
     * Asserts the class invariants of the given regular teleportation square.
     *
     * @param square 
     * The regular teleportation square to test.
     */
    private void assertClassInvariants(RegularTeleportationSquare square) {
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
        assertTrue(RegularTeleportationSquare.isValidTeleporter(
                                                    square.getTeleporter()));
    }

    @Test
    public void defaultConstructor() {
		assertEquals(destination, squareDefault.teleport());
        assertClassInvariants(squareDefault);
    }

	@Test
	public void setTeleporter_test() {
		squareDefault.setTeleporter(new Teleporter(squareDefault));
		assertEquals(squareDefault, squareDefault.teleport());
	}
    
    @Test
    public void getNavigatableSquares_test() {
        assertTrue(squareDefault.getNavigatableSquares().contains(
                                                            destination));
        assertEquals(1, squareDefault.getNavigatableSquares().size());
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

