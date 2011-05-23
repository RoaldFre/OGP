import rpg.*;
import rpg.util.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TemperatureTest.class,

    SquareImplTest.class,
    RegularSquareTest.class,
    RegularTeleportationSquareTest.class,
    TransparentSquareTest.class,
    TransparentTeleportationSquareTest.class,
    RockTest.class,

    CoupleTest.class,

    BorderTest.class,
    WallTest.class,
    DoorTest.class,
    OpenBorderTest.class,

    CoordinateTest.class,
    CoordinateSystemTest.class,

    DungeonTest.class,
    LeafDungeonTest.class,
    LevelTest.class,
    ShaftTest.class,
    CompositeDungeonTest.class,
})
public class AllTests {
}

// vim: ts=4:sw=4:expandtab:smarttab

