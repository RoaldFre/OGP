package rpg;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of transparent teleportation squares.
 *
 * @author Roald Frederickx
 */
public class TransparentTeleportationSquareTest {
    /**
	 * Instance variable referencing transparent teleportation squares that may 
	 * change during individual tests.
     */
    private TransparentTeleportationSquare squareDefault;
    private Square destination;

    /**
     * Set up a mutable test fixture.
     */
    @Before
    public void setUpMutableFixture() {
		destination = new RegularSquare();
        squareDefault = new TransparentTeleportationSquare(
											new Teleporter(destination));
    }

    /** 
	 * Asserts the class invariants of the given transparent teleportation 
	 * square.
     *
     * @param square 
     * The transparent teleportation square to test.
     */
    private void assertClassInvariants(TransparentTeleportationSquare square) {
        assertTrue(square.matchesMinTemperatureMax(
                square.getMinTemperature(), 
                square.getTemperature(),
                square.getMaxTemperature()));
        assertTrue(TransparentTeleportationSquare.isValidHeatDamageThreshold(
                TransparentTeleportationSquare.getHeatDamageThreshold()));
        assertTrue(TransparentTeleportationSquare.isValidHeatDamageStep(
                TransparentTeleportationSquare.getHeatDamageStep()));
        assertTrue(TransparentTeleportationSquare.isValidHumidity(
                square.getHumidity()));
        assertTrue(TransparentTeleportationSquare.isValidMergeTemperatureWeight(
                TransparentTeleportationSquare.getMergeTemperatureWeight()));
        assertTrue(square.hasProperBorders());
        assertTrue(square.bordersSatisfyConstraints());
        assertTrue(square.hasNoDuplicateBorders());
        assertTrue(TransparentTeleportationSquare.isValidTeleporter(
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

