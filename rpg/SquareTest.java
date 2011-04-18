package rpg;

import static org.junit.Assert.*;
import org.junit.*;
import rpg.exceptions.*;

/**
 * A class collecting tests for the class of squares.
 *
 * @author Roald Frederickx
 */
public class SquareTest {
	/**
	 * Instance variable referencing squares that may change during 
	 * individual tests.
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

	/** 
	 * Asserts the class invariants of the given square.
	 *
	 * @param square 
	 * The square to test.
	 */
	private void assertClassInvariants(Square square) {
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
		Square squareDefault = new Square();
		assertClassInvariants(squareDefault);
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
		assertClassInvariants(extended);
	}

	@Test(expected = IllegalArgumentException.class)
	public void extendedConstructor_NullTemp() {
		Square extended = new Square(
				null,
				new Temperature(-10), new Temperature(200),
				3000,
				true);
		/* do something with variable to make compiler happy */
		assertTrue(extended.hasSlipperyFloor());
	}

	@Test(expected = IllegalArgumentException.class)
	public void extendedConstructor_NullMinTemp() {
		Square extended = new Square(
				new Temperature(25),
				null, new Temperature(200),
				3000,
				true);
		/* do something with variable to make compiler happy */
		assertTrue(extended.hasSlipperyFloor());
	}

	@Test(expected = IllegalArgumentException.class)
	public void extendedConstructor_NullMaxTemp() {
		Square extended = new Square(
				new Temperature(25),
				new Temperature(-10), null,
				3000,
				true);
		/* do something with variable to make compiler happy */
		assertTrue(extended.hasSlipperyFloor());
	}

	@Test
	public void setTemperature_LegalCase() {
		square_T100_H50.setTemperature(new Temperature(200));
		assertEquals(200, square_T100_H50.getTemperature().temperature(), 0);
		assertClassInvariants(square_T100_H50);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setTemperature_TooHigh() throws Exception {
		double T = Double.MAX_VALUE;
		if (!square_T100_H50.canHaveAsTemperature(new Temperature(T)))
			square_T100_H50.setTemperature(new Temperature(T));
		else
			throw new IllegalArgumentException();
	}

	@Test(expected = IllegalArgumentException.class)
	public void setTemperature_TooLow() throws Exception {
		double T = Double.MIN_VALUE;
		if (!square_T100_H50.canHaveAsTemperature(new Temperature(T)))
			square_T100_H50.setTemperature(new Temperature(T));
		else
			throw new IllegalArgumentException();
	}

	@Test
	public void setMaxTemperature_LegalCase() {
		square_T100_H50.setMaxTemperature(new Temperature(1000));
		assertEquals( 1000, square_T100_H50.getMaxTemperature().temperature(), 0);
		assertClassInvariants(square_T100_H50);
	}

	@Test
	public void setMinTemperature_LegalCase() {
		square_T100_H50.setMinTemperature(new Temperature(-1000));
		assertEquals(-1000, square_T100_H50.getMinTemperature().temperature(), 0);
		assertClassInvariants(square_T100_H50);
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
	public void rustDamage_Test() {
		assertEquals(0, square_Tneg15p01_H0.rustDamage());
		assertEquals(0, square_Tneg5_H20.rustDamage());
		assertEquals(2, square_T100_H50.rustDamage());
		assertEquals(7, square_Tneg100_H80.rustDamage());
	}

	@Test
	public void slippery_Test() {
		assertTrue(square_T20_Tmin0_Tmax100_H20.isSlippery());

		assertTrue(square_T40_H100.isSlippery());
		assertTrue(square_Tneg15_H100.isSlippery());

		assertFalse(square_Tneg15p01_H0.isSlippery());
		assertFalse(square_Tneg4p99_H5.isSlippery());
	}
	
	@Test
	public void inhabitability_Test() {
		double heatDam = squareDefault.heatDamage();
		double heatDamCubed = heatDam * heatDam * heatDam;
		double coldDam = squareDefault.coldDamage();
		double humidityPercent = squareDefault.getHumidity() / 100.0;

		double expected = -1 * Math.sqrt(heatDamCubed / (101 - humidityPercent))
					- Math.sqrt(coldDam);
		assertEquals(expected, squareDefault.inhabitability(), 0);
	}

	@Test
	public void setMergeTemperatureWeight_LegalCase() {
		Square.setMergeTemperatureWeight(0.3);
		assertEquals(0.3, Square.getMergeTemperatureWeight(), 0); 
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMergeTemperatureWeight_IllegalCase() {
		Square.setMergeTemperatureWeight(-1);
	}

	@Test
	public void getBorderAt_Test() {
		Border newBorder = new Wall(
				squareDefault.getBorderAt(Direction.NORTH), false);
		assertEquals(newBorder, squareDefault.getBorderAt(Direction.NORTH));
		assertClassInvariants(squareDefault);
	}

	@Test(expected = IllegalArgumentException.class)
	public void changeBorderAt_NonProperBorder() {
		squareDefault.changeBorderAt(Direction.NORTH, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void changeBorderAt_Loop() {
		squareDefault.changeBorderAt(Direction.NORTH,
				squareDefault.getBorderAt(Direction.SOUTH));
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

	@Test
	public void isProperBorderAt_Test() {
		Border terminatedBorder = squareDefault.getBorderAt(Direction.NORTH);
		new Wall(terminatedBorder, false);

		Border borderWithCorrectSquare = new Wall(squareDefault, false);
		Border borderWithWrongSquare = new Wall(square_T100_H50, false);

		assertTrue(squareDefault.isProperBorderAt(Direction.NORTH,
												borderWithCorrectSquare));
		assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
												borderWithWrongSquare));
		assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
												terminatedBorder));
		assertFalse(squareDefault.isProperBorderAt(null,
												borderWithCorrectSquare));
		assertFalse(squareDefault.isProperBorderAt(Direction.NORTH, null));

		squareDefault.terminate();
		assertTrue(squareDefault.isProperBorderAt(Direction.NORTH, null));
		assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
												borderWithWrongSquare));
		assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
												borderWithCorrectSquare));
		assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
												terminatedBorder));
	}

	@Test
	public void getDirectionOfBorder_Legal() {
		for (Direction direction : Direction.values())
			assertEquals(direction,
					squareDefault.getDirectionOfBorder(
						squareDefault.getBorderAt(direction)));

		Border newBorder = new Wall(
				squareDefault.getBorderAt(Direction.NORTH), false);
		assertEquals(Direction.NORTH,
				squareDefault.getDirectionOfBorder(newBorder));
	}

	@Test
	public void hasBorder_Legal() {
		for (Direction direction : Direction.values())
			assertTrue(squareDefault.hasBorder(
						squareDefault.getBorderAt(direction)));

		Border newBorder = new Wall(
				squareDefault.getBorderAt(Direction.NORTH), false);
		assertTrue(squareDefault.hasBorder(newBorder));
	}

	@Test
	public void mergeWith_TestHumiditiesAndTemperatures_Legal() {
		square_T100_H50.mergeWith(square_T40_H100, Direction.NORTH);

		assertEquals(square_T100_H50.getBorderAt(Direction.NORTH),
						square_T40_H100.getBorderAt(Direction.SOUTH));

		assertEquals(7500, square_T100_H50.getHumidity());
		assertEquals(7500, square_T40_H100.getHumidity());

		double weightOffset = Square.getMergeTemperatureWeight();
		Temperature newTemp = new Temperature(
				((weightOffset + (1 - weightOffset) * 50/75) * 100
				 + (weightOffset + (1 - weightOffset) * 100/75) * 40) / 2.0);
		assertTrue(newTemp.equals(square_T100_H50.getTemperature()));
		assertTrue(newTemp.equals(square_T40_H100.getTemperature()));

		assertClassInvariants(square_T100_H50);
		assertClassInvariants(square_T40_H100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void mergeWith_IllegalSquare() {
		squareDefault.mergeWith(null, Direction.NORTH);
	}

	@Test(expected = IllegalArgumentException.class)
	public void mergeWith_IllegalDirection() {
		squareDefault.mergeWith(square_T40_H100, null);
	}

	@Test(expected = BorderMergeException.class)
	public void mergeWith_Loop() {
		squareDefault.mergeWith(squareDefault, Direction.NORTH);
	}

	@Test(expected = IllegalStateException.class)
	public void mergeWith_ThisTerminated() {
		square_T100_H50.terminate();
		square_T100_H50.mergeWith(square_T40_H100, Direction.NORTH);
	}

	@Test(expected = IllegalStateException.class)
	public void mergeWith_OtherTerminated() {
		square_T40_H100.terminate();
		square_T100_H50.mergeWith(square_T40_H100, Direction.NORTH);
	}
}
