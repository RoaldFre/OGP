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
		squareTemp100      = new Square(100);
		squareTempNeg5     = new Square(-5);
		squareTempNeg14p99 = new Square(-14.99);
		squareTempNeg15    = new Square(-15);
		squareTempNeg15p01 = new Square(-15.01);
		squareTempNeg100   = new Square(-100);
	}


	@Test
	public void setTemperature_LegalCase() {
		squareTemp100.setTemperature(200);
		assertEquals(200, squareTemp100.getTemperature(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setTemperature_TooHigh() throws Exception {
		if (!squareTemp100.canHaveAsTemperature(Double.MAX_VALUE))
			squareTemp100.setTemperature(Double.MAX_VALUE);
		else
			throw new IllegalArgumentException();
	}

	@Test(expected = IllegalArgumentException.class)
	public void setTemperature_TooLow() throws Exception {
		if (!squareTemp100.canHaveAsTemperature(Double.MIN_VALUE))
			squareTemp100.setTemperature(Double.MIN_VALUE);
		else
			throw new IllegalArgumentException();
	}

	@Test
	public void setMaxTemperature_LegalCase() {
		squareTemp100.setMaxTemperature(1000);
		assertEquals( 1000, squareTemp100.getMaxTemperature(), 0);
	}

	@Test
	public void setMinTemperature_LegalCase() {
		squareTemp100.setMinTemperature(-1000);
		assertEquals(-1000, squareTemp100.getMinTemperature(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMaxTemperature_BelowTemp() {
		squareTemp100.setMaxTemperature(99);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMinTemperature_AboveTemp() {
		squareTemp100.setMinTemperature(101);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMaxTemperature_BelowMin() {
		squareTemp100.setMaxTemperature(squareTemp100.getMinTemperature() - 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMinTemperature_AboveMax() {
		squareTemp100.setMinTemperature(squareTemp100.getMaxTemperature() + 1);
	}

	@Test
	//XXX can just bunch them in a single method with this name?
	public void coldDamage(){
		assertEquals(0, squareTemp100.coldDamage());
		assertEquals(1, squareTempNeg5.coldDamage());
		assertEquals(1, squareTempNeg14p99.coldDamage());
		assertEquals(2, squareTempNeg15.coldDamage());
		assertEquals(2, squareTempNeg15p01.coldDamage());
		assertEquals(10,squareTempNeg100.coldDamage());
	}
	
}

