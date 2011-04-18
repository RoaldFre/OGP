import rpg.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SquareTest.class,
	BorderTest.class,
	WallTest.class,
	DoorTest.class,
	OpenBorderTest.class,
	TemperatureTest.class,
	CoordinateTest.class,
	CoordinateSystemTest.class,
})
public class AllTests {
}
