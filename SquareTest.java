import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of squares
 *
 * @author Roald Frederickx
 */
public class SquareTest {
	/**
	 * Instance variable referencing bank accounts that may
	 * change during individual tests.
	 */
	private Square squareTemp100;

	/**
	 * Set up a mutable test fixture.
	 *
	 * @post The variable squareTemp100 references a new square with a 
	 * temperature of 100 degrees C.
	 */
	@Before
	public void setUpMutableFixture() {
		squareTemp100 = new Square(100);
	}


	@Test
	public void setTemperature_LegalCase() {
		squareTemp100.setTemperature(200);
		assertEquals(200, squareTemp100.getTemperature(), 0);
	}

	@Test
	public void setTemperature_TooHigh() {
		squareTemp100.setTemperature(200);
		assertEquals(200, squareTemp100.getTemperature(), 0);
	}
}
	


