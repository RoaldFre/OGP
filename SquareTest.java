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
	private Square squareDefault;
	private Square square_T100_H50;
	private Square square_Tneg5_H20;
	private Square square_Tneg4p99_H5;
	private Square square_Tneg15_H100;
	private Square square_Tneg15p01_H0;
	private Square square_Tneg100_H80;
	private Square square_T35_H50;
	private Square square_T40_H100;
	private Square squareTemp49p99;
	private Square squareTemp50;
	private Square square_T20_Tmin0_Tmax100_H20;

	/**
	 * Set up a mutable test fixture.
	 */
	@Before
	public void setUpMutableFixture() {
		squareDefault = new Square();

		square_T100_H50 = new Square(new Temperature(100), 5000);

		square_Tneg5_H20	= new Square(new Temperature(-5), 1000);
		square_Tneg4p99_H5	= new Square(new Temperature(-14.99), 500);
		square_Tneg15_H100	= new Square(new Temperature(-15), 10000);
		square_Tneg15p01_H0	= new Square(new Temperature(-15.01), 0);
		square_Tneg100_H80	= new Square(new Temperature(-100), 8000);

		square_T35_H50	= new Square(new Temperature(35), 5000);
		square_T40_H100	= new Square(new Temperature(40), 10000);
		squareTemp49p99	= new Square(new Temperature(49.99), 5000);
		squareTemp50	= new Square(new Temperature(50), 5000);

		square_T20_Tmin0_Tmax100_H20 = new Square(
					new Temperature(25),
					new Temperature(-10), new Temperature(200),
					3000,
					true);
	}

	@Test
	public void defaultConstructor() {
		Square squareDefault = new Square();
		//do something with the variable to make compiler happy;
		assert Square.isValidHumidity(squareDefault.getHumidity());
	}
	
	@Test
	public void extendedConstructor_LegalCase() {
		Square extended = new Square(
				new Temperature(25),
				new Temperature(-10), new Temperature(200),
				3000,
				true);
		assertEquals(25, extended.getTemperature().temperature(), 0);
		assertEquals(-10, extended.getMinTemperature().temperature(), 0);
		assertEquals(200, extended.getMaxTemperature().temperature(), 0);
		assertEquals(3000, extended.getHumidity());
		assertTrue(extended.hasSlipperyFloor());
	}

	@Test
	public void setTemperature_LegalCase() {
		square_T100_H50.setTemperature(new Temperature(200));
		assertEquals(200, square_T100_H50.getTemperature().temperature(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setTemperature_TooHigh() throws Exception {
		if (!square_T100_H50.canHaveAsTemperature(new Temperature(Double.MAX_VALUE)))
			square_T100_H50.setTemperature(new Temperature(Double.MAX_VALUE));
		else
			throw new IllegalArgumentException();
	}

	@Test(expected = IllegalArgumentException.class)
	public void setTemperature_TooLow() throws Exception {
		if (!square_T100_H50.canHaveAsTemperature(new Temperature(Double.MIN_VALUE)))
			square_T100_H50.setTemperature(new Temperature(Double.MIN_VALUE));
		else
			throw new IllegalArgumentException();
	}

	@Test
	public void setMaxTemperature_LegalCase() {
		square_T100_H50.setMaxTemperature(new Temperature(1000));
		assertEquals( 1000, square_T100_H50.getMaxTemperature().temperature(), 0);
	}

	@Test
	public void setMinTemperature_LegalCase() {
		square_T100_H50.setMinTemperature(new Temperature(-1000));
		assertEquals(-1000, square_T100_H50.getMinTemperature().temperature(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMaxTemperature_BelowTemp() {
		square_T100_H50.setMaxTemperature(new Temperature(99));
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMinTemperature_AboveTemp() {
		square_T100_H50.setMinTemperature(new Temperature(101));
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMaxTemperature_BelowMin() {
		Temperature lower = new Temperature(
				square_T100_H50.getMinTemperature().temperature() - 1);
		square_T100_H50.setMaxTemperature(lower);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMinTemperature_AboveMax() {
		Temperature higher = new Temperature(
				square_T100_H50.getMaxTemperature().temperature() + 1);
		square_T100_H50.setMinTemperature(higher);
	}

	@Test
	public void coldDamage(){
		assertEquals(0, square_T100_H50.coldDamage());
		assertEquals(1, square_Tneg5_H20.coldDamage());
		assertEquals(1, square_Tneg4p99_H5.coldDamage());
		assertEquals(2, square_Tneg15_H100.coldDamage());
		assertEquals(2, square_Tneg15p01_H0.coldDamage());
		assertEquals(10,square_Tneg100_H80.coldDamage());
	}


	@Test
	public void heatDamage(){
		assertEquals(0, square_Tneg5_H20.heatDamage());
		assertEquals(1, square_T35_H50.heatDamage());
		assertEquals(1, square_T40_H100.heatDamage());
		assertEquals(1, squareTemp49p99.heatDamage());
		assertEquals(2, squareTemp50.heatDamage());
	}


	public void setHeatDamageThreshold_LegalCase() {
		Square.setHeatDamageThreshold(new Temperature(100));
		assertEquals(100, Square.getHeatDamageThreshold().temperature(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setHeatDamageThreshold_Null() {
		Square.setHeatDamageThreshold(null);
	}

	public void setHeatDamageStep_LegalCase() {
		Square.setHeatDamageThreshold(new Temperature(100));
		assertEquals(100, Square.getHeatDamageStep(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setHeatDamageStep_Zero() {
		Square.setHeatDamageStep(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setHeatDamageStep_Negative() {
		Square.setHeatDamageStep(-1);
	}


	@Test
	public void rustDamage() {
		assertEquals(0, square_Tneg15p01_H0.rustDamage());
		assertEquals(0, square_Tneg5_H20.rustDamage());
		assertEquals(2, square_T100_H50.rustDamage());
		assertEquals(7, square_Tneg100_H80.rustDamage());
	}

	@Test
	public void slippery() {
		assertTrue(square_T20_Tmin0_Tmax100_H20.isSlippery());

		assertTrue(square_T40_H100.isSlippery());
		assertTrue(square_Tneg15_H100.isSlippery());

		assertFalse(square_Tneg15p01_H0.isSlippery());
		assertFalse(square_Tneg4p99_H5.isSlippery());
	}
	
	@Test
	public void inhabitability() {
		double heatDam = squareDefault.heatDamage();
		double heatDamCubed = heatDam * heatDam * heatDam;
		double coldDam = squareDefault.coldDamage();
		double humidityPercent = squareDefault.getHumidity() / 100.0;

		double expected = -1 * Math.sqrt(heatDamCubed / (101 - humidityPercent))
					- Math.sqrt(coldDam);
		assertEquals(expected, squareDefault.inhabitability(), 0);
	}

	@Test
	public void setBorderAt() {
		squareDefault.setBorderAt(1, true);
		assertTrue(squareDefault.hasBorderAt(1));
	}
}
