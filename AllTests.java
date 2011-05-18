import rpg.*;
import rpg.util.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    SquareTest.class,
    RegularSquareTest.class,
    TransparentSquareTest.class,
    RockTest.class,
    BorderTest.class,
    WallTest.class,
    DoorTest.class,
    OpenBorderTest.class,
    TemperatureTest.class,
    CoordinateTest.class,
    CoordinateSystemTest.class,
    DungeonTest.class,
    CoupleTest.class,
})
public class AllTests {
}

// vim: ts=4:sw=4:expandtab:smarttab

