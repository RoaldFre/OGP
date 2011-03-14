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
	private Square squareTempNeg5;
	private Square squareTempNeg14p99;
	private Square squareTempNeg15;
	private Square squareTempNeg15p01;
	private Square squareTempNeg100;

	/**
	 * Set up a mutable test fixture.
	 *
	 * @post The variable squareTemp100 references a new square with a 
	 * temperature of 100 degrees C.
	 */
	@Before
	public void setUpMutableFixture() {
		squareTemp100      = new Square(new Temperature(100));
		squareTempNeg5     = new Square(new Temperature(-5));
		squareTempNeg14p99 = new Square(new Temperature(-14.99));
		squareTempNeg15    = new Square(new Temperature(-15));
		squareTempNeg15p01 = new Square(new Temperature(-15.01));
		squareTempNeg100   = new Square(new Temperature(-100));
	}


	@Test
	public void setTemperature_LegalCase() {
		squareTemp100.setTemperature(new Temperature(200));
		assertEquals(200, squareTemp100.getTemperature().temperature(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setTemperature_TooHigh() throws Exception {
		if (!squareTemp100.canHaveAsTemperature(new Temperature(Double.MAX_VALUE)))
			squareTemp100.setTemperature(new Temperature(Double.MAX_VALUE));
		else
			throw new IllegalArgumentException();
	}

	@Test(expected = IllegalArgumentException.class)
	public void setTemperature_TooLow() throws Exception {
		if (!squareTemp100.canHaveAsTemperature(new Temperature(Double.MIN_VALUE)))
			squareTemp100.setTemperature(new Temperature(Double.MIN_VALUE));
		else
			throw new IllegalArgumentException();
	}

	@Test
	public void setMaxTemperature_LegalCase() {
		squareTemp100.setMaxTemperature(new Temperature(1000));
		assertEquals( 1000, squareTemp100.getMaxTemperature().temperature(), 0);
	}

	@Test
	public void setMinTemperature_LegalCase() {
		squareTemp100.setMinTemperature(new Temperature(-1000));
		assertEquals(-1000, squareTemp100.getMinTemperature().temperature(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMaxTemperature_BelowTemp() {
		squareTemp100.setMaxTemperature(new Temperature(99));
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMinTemperature_AboveTemp() {
		squareTemp100.setMinTemperature(new Temperature(101));
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMaxTemperature_BelowMin() {
		Temperature lower = new Temperature(
				squareTemp100.getMinTemperature().temperature() - 1);
		squareTemp100.setMaxTemperature(lower);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMinTemperature_AboveMax() {
		Temperature higher = new Temperature(
				squareTemp100.getMaxTemperature().temperature() + 1);
		squareTemp100.setMinTemperature(higher);
	}

	@Test
	public void coldDamage(){
		assertEquals(0, squareTemp100.coldDamage());
		assertEquals(1, squareTempNeg5.coldDamage());
		assertEquals(1, squareTempNeg14p99.coldDamage());
		assertEquals(2, squareTempNeg15.coldDamage());
		assertEquals(2, squareTempNeg15p01.coldDamage());
		assertEquals(10,squareTempNeg100.coldDamage());
	}
	
}

