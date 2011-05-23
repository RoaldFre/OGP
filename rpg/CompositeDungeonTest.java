package rpg;

import rpg.util.*;
import rpg.exceptions.*;

import static org.junit.Assert.*;
import org.junit.*;

public class CompositeDungeonTest {

    Level<TransparentSquare> transparentLevel;
    TransparentSquare transSq1;
    TransparentSquare transSq2;
    TransparentSquare transSq3;

    Level<TeleportationSquare> teleportLevel;
    RegularTeleportationSquare regTelSq1;
    RegularTeleportationSquare regTelSq2;
    RegularTeleportationSquare regTelSq3;

    Shaft<RegularSquare> regularShaft;
    RegularSquare regSq1;
    RegularSquare regSq2;
    RegularSquare regSq3;

    CompositeDungeon<Square> dungeon;

    @Before
    public void setUpMutableFixture() {
        transparentLevel = new Level<TransparentSquare>(2, 2);
        transSq1 = new TransparentSquare();
        transSq2 = new TransparentSquare();
        transSq3 = new TransparentSquare();

        teleportLevel = new Level<TeleportationSquare>(2, 2);
        regTelSq1 = new RegularTeleportationSquare(new Teleporter(transSq1));
        regTelSq2 = new RegularTeleportationSquare(new Teleporter(transSq2));
        regTelSq3 = new RegularTeleportationSquare(new Teleporter(transSq3));

        regularShaft = new Shaft<RegularSquare>(3, Direction.UP);
        regSq1 = new RegularSquare(); 
        regSq2 = new RegularSquare(); 
        regSq3 = new RegularSquare(); 

        CoordinateSystem coordSyst = new CoordinateSystem(
                new Coordinate(0, 0, 0),
                new Coordinate(9, 9, 9));
        dungeon = new CompositeDungeon<Square>(coordSyst);
    }

    private void assertClassInvariants(CompositeDungeon<?> compositeDungeon) {
        assertTrue(compositeDungeon.isNotRaw());
    }

    @Test
    public void constructor_legal() {
        CoordinateSystem coordSyst = new CoordinateSystem(
                new Coordinate(0, 0, 0),
                new Coordinate(9, 9, 9));
        CompositeDungeon<Square> dungeon = 
                                new CompositeDungeon<Square>(coordSyst);
        assertClassInvariants(dungeon);
    }

}

// vim: ts=4:sw=4:expandtab:smarttab

