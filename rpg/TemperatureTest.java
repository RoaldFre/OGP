package rpg;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the class of temperatures.
 *
 * @author Roald Frederickx
 */
public class TemperatureTest {

	private static Temperature zeroC;
	private static Temperature tenC;

	@BeforeClass
	public static void setUpImmutableFixture() {
		zeroC = new Temperature(0);
		tenC = new Temperature(10);
	}

	@Test
	public void constructor_celcius() {
		assertEquals(123, new Temperature(123).temperature(
					Temperature.Scale.CELCIUS), Temperature.EQUALS_EPSILON);
	}

	@Test
	public void constructor_otherScales() {
		assertEquals(1,
				new Temperature(1, Temperature.Scale.CELCIUS).temperature(
					Temperature.Scale.CELCIUS), Temperature.EQUALS_EPSILON);
		assertEquals(1,
				new Temperature(1, Temperature.Scale.FAHRENHEIT).temperature(
					Temperature.Scale.FAHRENHEIT), Temperature.EQUALS_EPSILON);
		assertEquals(1,
				new Temperature(1, Temperature.Scale.KELVIN).temperature(
					Temperature.Scale.KELVIN), Temperature.EQUALS_EPSILON);
	}

	@Test (expected = IllegalArgumentException.class)
	public void constructor_otherScales_null() {
		new Temperature(1, null);
	}

	@Test
	public void temperature_scaleConversion() {
		assertEquals(zeroC,
				new Temperature(32, Temperature.Scale.FAHRENHEIT));
		assertEquals(zeroC,
				new Temperature(273.15, Temperature.Scale.KELVIN));
		assertEquals(tenC,
				new Temperature(50, Temperature.Scale.FAHRENHEIT));
		assertEquals(tenC,
				new Temperature(283.15, Temperature.Scale.KELVIN));
	}

	@Test
	public void compareTo_lower() {
		assertEquals(-1, zeroC.compareTo(tenC));
	}
	@Test
	public void compareTo_higher() {
		assertEquals(1, tenC.compareTo(zeroC));
	}
	@Test
	public void compareTo_equal() {
		assertEquals(0, zeroC.compareTo(zeroC));
	}

	@Test
	public void hashCode_equal() {
		assertEquals(zeroC.hashCode(), new Temperature(0).hashCode());
	}

	@Test
	public void toString_test() {
		assertEquals(zeroC.temperature() + "C", zeroC.toString());
	}
}

