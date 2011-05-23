package rpg;

import rpg.util.*;
import rpg.exceptions.*;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.Map;

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
        transparentLevel.addSquareAt(new Coordinate(0, 1, 0), transSq1);
        transparentLevel.addSquareAt(new Coordinate(1, 0, 0), transSq2);
        transparentLevel.addSquareAt(new Coordinate(1, 1, 0), transSq3);

        teleportLevel = new Level<TeleportationSquare>(2, 2);
        regTelSq1 = new RegularTeleportationSquare(new Teleporter(transSq1));
        regTelSq2 = new RegularTeleportationSquare(new Teleporter(transSq2));
        regTelSq3 = new RegularTeleportationSquare(new Teleporter(transSq3));
        teleportLevel.addSquareAt(new Coordinate(0, 1, 0), regTelSq1);
        teleportLevel.addSquareAt(new Coordinate(1, 0, 0), regTelSq2);
        teleportLevel.addSquareAt(new Coordinate(1, 1, 0), regTelSq3);

        regularShaft = new Shaft<RegularSquare>(new Coordinate(0, 1, 1),
                                                3, Direction.UP);
        regSq1 = new RegularSquare(); 
        regSq2 = new RegularSquare(); 
        regSq3 = new RegularSquare(); 
        regularShaft.addSquareAt(new Coordinate(0, 1, 1), regSq1);
        regularShaft.addSquareAt(new Coordinate(0, 1, 2), regSq2);
        regularShaft.addSquareAt(new Coordinate(0, 1, 3), regSq3);

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

    @Test
    public void getDirectionsAndNeighboursOf_legal() {
        Map<Direction, Square> map;
        map = dungeon.getDirectionsAndNeighboursOf(new Coordinate(0, 0, 0));
        assertTrue(map.isEmpty());

        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        map = dungeon.getDirectionsAndNeighboursOf(new Coordinate(0, 1, 0));
        assertEquals(regTelSq3, map.get(Direction.EAST));
        assertEquals(1, map.size());

        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        map = dungeon.getDirectionsAndNeighboursOf(new Coordinate(0, 1, 0));
        assertEquals(regTelSq3, map.get(Direction.EAST));
        assertEquals(regSq1, map.get(Direction.UP));
        assertEquals(2, map.size());

        assertClassInvariants(dungeon);
    }

    @Test
    public void getSubDungeonContaining_test() {
        assertEquals(null, dungeon.getSubDungeonContaining(
                                                new Coordinate(0, 1, 0)));

        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        assertEquals(teleportLevel, dungeon.getSubDungeonContaining(
                                                new Coordinate(0, 1, 0)));
        assertEquals(null, dungeon.getSubDungeonContaining(
                                                new Coordinate(0, 1, 2)));


        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        assertEquals(teleportLevel, dungeon.getSubDungeonContaining(
                                                new Coordinate(0, 1, 0)));
        assertEquals(regularShaft, dungeon.getSubDungeonContaining(
                                                new Coordinate(0, 1, 2)));
        assertEquals(null, dungeon.getSubDungeonContaining(
                                                new Coordinate(0, 1, 4)));

        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));
        assertEquals(teleportLevel, dungeon.getSubDungeonContaining(
                                                new Coordinate(0, 1, 0)));
        assertEquals(regularShaft, dungeon.getSubDungeonContaining(
                                                new Coordinate(0, 1, 2)));
        assertEquals(transparentLevel, dungeon.getSubDungeonContaining(
                                                new Coordinate(0, 1, 4)));
        assertClassInvariants(dungeon);
    }

    @Test
    public void translate_legal() {
        CoordinateSystem teleport, regular, transparent;
        transparent = transparentLevel.getCoordSyst();

        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));

        transparent.translate(new Coordinate(0, 0, 4));
        assertEquals(transparent, transparentLevel.getCoordSyst());

        teleport = teleportLevel.getCoordSyst();
        regular = regularShaft.getCoordSyst();
        transparent = transparentLevel.getCoordSyst();
        
        Coordinate regTelSq1Coord = new Coordinate(0, 1, 0);
        assertEquals(regTelSq1, dungeon.getSquareAt(regTelSq1Coord));

        Coordinate offset = new Coordinate(0, 0, 2);
        assertClassInvariants(dungeon);
        dungeon.translate(offset);
        assertClassInvariants(dungeon);

        assertEquals(regTelSq1, dungeon.getSquareAt(regTelSq1Coord.add(offset)));

        teleport.translate(offset);
        regular.translate(offset);
        transparent.translate(offset);
        assertEquals(teleport, teleportLevel.getCoordSyst());
        assertEquals(regular, regularShaft.getCoordSyst());
        assertEquals(transparent, transparentLevel.getCoordSyst());
    }

    @Test
    public void translate_exceptions() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));
        
        CoordinateSystem teleport, regular, transparent;
        teleport = teleportLevel.getCoordSyst();
        regular = regularShaft.getCoordSyst();
        transparent = transparentLevel.getCoordSyst();

        Coordinate regTelSq1Coord = new Coordinate(0, 1, 0);
        assertEquals(regTelSq1, dungeon.getSquareAt(regTelSq1Coord));

        try {
            dungeon.translate(new Coordinate(0, 0, 1));
            assertTrue(false);
        } catch (CoordinateConstraintsException cce) { /* nop */ }

        assertEquals(teleport, teleportLevel.getCoordSyst());
        assertEquals(regular, regularShaft.getCoordSyst());
        assertEquals(transparent, transparentLevel.getCoordSyst());
        assertEquals(regTelSq1, dungeon.getSquareAt(regTelSq1Coord));

        try {
            dungeon.translate(new Coordinate(-1, -1, -4));
            assertTrue(false);
        } catch (CoordinateConstraintsException cce) { /* nop */ }

        assertEquals(teleport, teleportLevel.getCoordSyst());
        assertEquals(regular, regularShaft.getCoordSyst());
        assertEquals(transparent, transparentLevel.getCoordSyst());
        assertEquals(regTelSq1, dungeon.getSquareAt(regTelSq1Coord));

        try {
            dungeon.translate(null);
            assertTrue(false);
        } catch (IllegalArgumentException iae) { /* nop */ }

        assertEquals(teleport, teleportLevel.getCoordSyst());
        assertEquals(regular, regularShaft.getCoordSyst());
        assertEquals(transparent, transparentLevel.getCoordSyst());
        assertEquals(regTelSq1, dungeon.getSquareAt(regTelSq1Coord));
        
        assertClassInvariants(dungeon);
    }

    @Test
    public void getSquareAt_legal() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));

        assertEquals(regTelSq1, dungeon.getSquareAt(new Coordinate(0, 1, 0)));
        assertEquals(regTelSq2, dungeon.getSquareAt(new Coordinate(1, 0, 0)));
        assertEquals(regTelSq3, dungeon.getSquareAt(new Coordinate(1, 1, 0)));
        assertEquals(regSq1, dungeon.getSquareAt(new Coordinate(0, 1, 1)));
        assertEquals(regSq2, dungeon.getSquareAt(new Coordinate(0, 1, 2)));
        assertEquals(regSq3, dungeon.getSquareAt(new Coordinate(0, 1, 3)));
        assertEquals(transSq1, dungeon.getSquareAt(new Coordinate(0, 1, 4)));
        assertEquals(transSq2, dungeon.getSquareAt(new Coordinate(1, 0, 4)));
        assertEquals(transSq3, dungeon.getSquareAt(new Coordinate(1, 1, 4)));

        assertClassInvariants(dungeon);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getSquareAt_null() {
        dungeon.getSquareAt(null);
    }
    @Test (expected = CoordinateNotOccupiedException.class)
    public void getSquareAt_notOccupied_inDungeon() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));
        dungeon.getSquareAt(new Coordinate (5, 0, 0));
    }
    @Test (expected = CoordinateNotOccupiedException.class)
    public void getSquareAt_notOccupied_notInDungeon() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));
        dungeon.getSquareAt(new Coordinate (-1, 0, 0));
    }

    @Test
    public void hasSquare_test() {
        assertFalse(dungeon.hasSquare(null));
        assertFalse(dungeon.hasSquare(regTelSq1));
        assertFalse(dungeon.hasSquare(regSq1));
        assertFalse(dungeon.hasSquare(transSq1));

        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        assertTrue(dungeon.hasSquare(regTelSq1));
        assertFalse(dungeon.hasSquare(regSq1));
        assertFalse(dungeon.hasSquare(transSq1));

        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        assertTrue(dungeon.hasSquare(regTelSq1));
        assertTrue(dungeon.hasSquare(regSq1));
        assertFalse(dungeon.hasSquare(transSq1));

        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));
        assertTrue(dungeon.hasSquare(regTelSq1));
        assertTrue(dungeon.hasSquare(regSq1));
        assertTrue(dungeon.hasSquare(transSq1));
    }

    @Test
    public void getNbSquares_test() {
        assertEquals(0, dungeon.getNbSquares());
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        assertEquals(3, dungeon.getNbSquares());
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        assertEquals(6, dungeon.getNbSquares());
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));
        assertEquals(9, dungeon.getNbSquares());
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

