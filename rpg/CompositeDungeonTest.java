package rpg;

import rpg.Dungeon.*;
import rpg.util.*;
import rpg.exceptions.*;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class CompositeDungeonTest {

    Level<TransparentSquare> transparentLevel;
    TransparentSquare transSq1;
    TransparentSquare transSq2;
    TransparentSquare transSq3;

    Level<TeleportationSquare> teleportLevel;
    RegularTeleportationSquare regTelSq1;
    RegularTeleportationSquare regTelSq2;
    RegularTeleportationSquare regTelSq3;
    Coordinate telCoord1;
    Coordinate telCoord2;
    Coordinate telCoord3;

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
        telCoord1 = new Coordinate (0, 1, 0);
        telCoord2 = new Coordinate (1, 0, 0);
        telCoord3 = new Coordinate (1, 1, 0);
        teleportLevel.addSquareAt(telCoord1, regTelSq1);
        teleportLevel.addSquareAt(telCoord2, regTelSq2);
        teleportLevel.addSquareAt(telCoord3, regTelSq3);

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

    public static void assertClassInvariants(
                                    CompositeDungeon<?> compositeDungeon) {
        assertTrue(compositeDungeon.isNotRaw());
    }
    public static void assertClassInvariantsDeep(
                                    CompositeDungeon<?> compositeDungeon) {
        assertTrue(compositeDungeon.isNotRaw());
        for (Square square : compositeDungeon.getSquares())
            assertTrue(square.isNotRaw());
        for (Dungeon<?> dunguon : compositeDungeon.getSubDungeons())
            assertTrue(dunguon.isNotRaw());
        assertTrue(compositeDungeon.getParentDungeon() == null
                        || compositeDungeon.getParentDungeon().isNotRaw());
    }

    @Test
    public void constructor_legal() {
        CoordinateSystem coordSyst = new CoordinateSystem(
                new Coordinate(0, 0, 0),
                new Coordinate(9, 9, 9));
        CompositeDungeon<Square> dungeon = 
                                new CompositeDungeon<Square>(coordSyst);
        assertClassInvariantsDeep(dungeon);
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

        assertClassInvariantsDeep(dungeon);
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
        assertClassInvariantsDeep(dungeon);
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
        
        assertEquals(regTelSq1, dungeon.getSquareAt(telCoord1));

        Coordinate offset = new Coordinate(0, 0, 2);
        assertClassInvariantsDeep(dungeon);
        dungeon.translate(offset);
        assertClassInvariantsDeep(dungeon);

        assertEquals(regTelSq1, dungeon.getSquareAt(telCoord1.add(offset)));

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

        assertEquals(regTelSq1, dungeon.getSquareAt(telCoord1));

        try {
            dungeon.translate(new Coordinate(0, 0, 1));
            assertTrue(false);
        } catch (CoordinateConstraintsException cce) { /* nop */ }

        assertEquals(teleport, teleportLevel.getCoordSyst());
        assertEquals(regular, regularShaft.getCoordSyst());
        assertEquals(transparent, transparentLevel.getCoordSyst());
        assertEquals(regTelSq1, dungeon.getSquareAt(telCoord1));

        try {
            dungeon.translate(new Coordinate(-1, -1, -4));
            assertTrue(false);
        } catch (CoordinateConstraintsException cce) { /* nop */ }

        assertEquals(teleport, teleportLevel.getCoordSyst());
        assertEquals(regular, regularShaft.getCoordSyst());
        assertEquals(transparent, transparentLevel.getCoordSyst());
        assertEquals(regTelSq1, dungeon.getSquareAt(telCoord1));

        try {
            dungeon.translate(null);
            assertTrue(false);
        } catch (IllegalArgumentException iae) { /* nop */ }

        assertEquals(teleport, teleportLevel.getCoordSyst());
        assertEquals(regular, regularShaft.getCoordSyst());
        assertEquals(transparent, transparentLevel.getCoordSyst());
        assertEquals(regTelSq1, dungeon.getSquareAt(telCoord1));
        
        assertClassInvariantsDeep(dungeon);
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

        assertClassInvariantsDeep(dungeon);
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

    @Test
    public void addSquareMappingTo_test() {
        Map<Coordinate, Square> map = new HashMap<Coordinate, Square>();
        dungeon.addSquareMappingTo(map);
        assertTrue(map.isEmpty());

        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSquareMappingTo(map);
        assertEquals(regTelSq1, map.get(telCoord1));
        assertEquals(regTelSq2, map.get(telCoord2));
        assertEquals(regTelSq3, map.get(telCoord3));
    }

    @Test
    public void getFilteredSquareIterator_test() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));

        Set<Square> squareSet;

        SquareFilter teleportationSquaresFilter = new SquareFilter() {
            public boolean filter(LeafDungeon<? extends Square> d, Square s) {
                return s instanceof TeleportationSquare;
            }
        };
        squareSet = new HashSet<Square>();
        for (Square square :
                    dungeon.getFilteredSquares(teleportationSquaresFilter))
            squareSet.add(square);
        assertEquals(3, squareSet.size());
        assertTrue(squareSet.contains(regTelSq1));
        assertTrue(squareSet.contains(regTelSq2));
        assertTrue(squareSet.contains(regTelSq3));
        

        squareSet = new HashSet<Square>();
        SquareFilter transparentSquaresFilter = new SquareFilter() {
            public boolean filter(LeafDungeon<? extends Square> d, Square s) {
                return s instanceof TransparentSquare;
            }
        };
        squareSet = new HashSet<Square>();
        for (Square square :
                    dungeon.getFilteredSquares(transparentSquaresFilter))
            squareSet.add(square);
        assertEquals(3, squareSet.size());
        assertTrue(squareSet.contains(transSq1));
        assertTrue(squareSet.contains(transSq2));
        assertTrue(squareSet.contains(transSq3));
        squareSet = new HashSet<Square>();


        SquareFilter squaresInShaftFilter = new SquareFilter() {
            public boolean filter(LeafDungeon<? extends Square> d, Square s) {
                return d instanceof Shaft<?>;
            }
        };
        squareSet = new HashSet<Square>();
        for (Square square :
                    dungeon.getFilteredSquares(squaresInShaftFilter))
            squareSet.add(square);
        assertEquals(3, squareSet.size());
        assertTrue(squareSet.contains(regSq1));
        assertTrue(squareSet.contains(regSq2));
        assertTrue(squareSet.contains(regSq3));

        squareSet = new HashSet<Square>();
        for (Square square :
                    dungeon.getFilteredSquares(Dungeon.acceptAllSquaresFilter))
            squareSet.add(square);
        assertEquals(9, squareSet.size());
        assertTrue(squareSet.contains(regTelSq1));
        assertTrue(squareSet.contains(regTelSq2));
        assertTrue(squareSet.contains(regTelSq3));
        assertTrue(squareSet.contains(transSq1));
        assertTrue(squareSet.contains(transSq2));
        assertTrue(squareSet.contains(transSq3));
        assertTrue(squareSet.contains(regSq1));
        assertTrue(squareSet.contains(regSq2));
        assertTrue(squareSet.contains(regSq3));

        assertClassInvariantsDeep(dungeon);
    }

    @Test
    public void getSubDungeons_test() {
        assertTrue(dungeon.getSubDungeons().isEmpty());
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));
        assertEquals(3, dungeon.getSubDungeons().size());
        assertTrue(dungeon.getSubDungeons().contains(teleportLevel));
        assertTrue(dungeon.getSubDungeons().contains(regularShaft));
        assertTrue(dungeon.getSubDungeons().contains(transparentLevel));
        assertClassInvariantsDeep(dungeon);
    }

    @Test
    public void hasAsSubDungeon_test() {
        assertFalse(dungeon.hasAsSubDungeon(teleportLevel));
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        assertTrue(dungeon.hasAsSubDungeon(teleportLevel));
        assertClassInvariantsDeep(dungeon);
    }

    @Test
    public void addSubDungeonAt_leaves() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));

        assertTrue(dungeon.hasAsSubDungeon(teleportLevel));
        assertTrue(dungeon.hasAsSubDungeon(regularShaft));
        assertTrue(dungeon.hasAsSubDungeon(transparentLevel));

        assertEquals(dungeon, teleportLevel.getParentDungeon());
        assertEquals(dungeon, regularShaft.getParentDungeon());
        assertEquals(dungeon, transparentLevel.getParentDungeon());

        assertClassInvariantsDeep(dungeon);
        LevelTest.assertClassInvariants(teleportLevel);
        ShaftTest.assertClassInvariants(regularShaft);
        LevelTest.assertClassInvariants(transparentLevel);
    }
    @Test
    public void addSubDungeonAt_compositeDungeon() {
        CompositeDungeon<Square> subDungeon
            = new CompositeDungeon<Square>(transparentLevel.getCoordSyst());
        subDungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(subDungeon, new Coordinate(0, 0, 4));

        assertTrue(dungeon.hasAsSubDungeon(teleportLevel));
        assertTrue(dungeon.hasAsSubDungeon(regularShaft));
        assertTrue(dungeon.hasAsSubDungeon(subDungeon));
        assertTrue(subDungeon.hasAsSubDungeon(transparentLevel));

        assertEquals(dungeon, teleportLevel.getParentDungeon());
        assertEquals(dungeon, regularShaft.getParentDungeon());
        assertEquals(dungeon, subDungeon.getParentDungeon());
        assertEquals(subDungeon, transparentLevel.getParentDungeon());

        assertClassInvariantsDeep(dungeon);
        assertClassInvariantsDeep(subDungeon);
        LevelTest.assertClassInvariants(teleportLevel);
        ShaftTest.assertClassInvariants(regularShaft);
        LevelTest.assertClassInvariants(transparentLevel);
    }

    @Test (expected = IllegalArgumentException.class)
    public void addSubDungeonAt_illegalDungeon() {
        dungeon.addSubDungeonAt(null, new Coordinate(0, 0, 0));
    }
    @Test (expected = IllegalArgumentException.class)
    public void addSubDungeonAt_illegalCoordinate() {
        dungeon.addSubDungeonAt(teleportLevel, null);
    }

    @Test (expected = IllegalStateException.class)
    public void addSubDungeonAt_terminated() {
        dungeon.terminate();
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
    }
    @Test (expected = IllegalStateException.class)
    public void addSubDungeonAt_subDungeonTerminated() {
        teleportLevel.terminate();
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
    }

    @Test (expected = DungeonAlreadyAssociatedException.class)
    public void addSubDungeonAt_alreadyAssociatedWithOther() {
        CompositeDungeon<Square> otherDungeon =
                        new CompositeDungeon<Square>(dungeon.getCoordSyst());
        otherDungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
    }
    @Test (expected = DungeonAlreadyAssociatedException.class)
    public void addSubDungeonAt_alreadyAssociatedWithSelf() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 1));
    }

    @Test (expected = SubDungeonDoesNotFitException.class)
    public void addSubDungeonAt_subDungeonTooBig() {
        Level<Square> massiveLevel = new Level<Square>(1000, 1000);
        dungeon.addSubDungeonAt(massiveLevel, new Coordinate(0, 0, 1));
    }
    @Test (expected = SubDungeonDoesNotFitException.class)
    public void addSubDungeonAt_subDungeonOverlaps() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 0));
    }


    @Test
    public void terminate_empty() {
        dungeon.terminate();
        assertTrue(dungeon.isTerminated());
        assertClassInvariantsDeep(dungeon);
    }
    @Test
    public void terminate_withLeaves() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));

        dungeon.terminate();

        assertTrue(dungeon.isTerminated());
        assertTrue(regularShaft.isTerminated());
        assertTrue(teleportLevel.isTerminated());
        assertTrue(transparentLevel.isTerminated());
        assertClassInvariantsDeep(dungeon);
        LevelTest.assertClassInvariants(teleportLevel);
        ShaftTest.assertClassInvariants(regularShaft);
        LevelTest.assertClassInvariants(transparentLevel);
    }
    @Test
    public void terminate_withCompositeSubdungeons() {
        CompositeDungeon<Square> subDungeon
            = new CompositeDungeon<Square>(transparentLevel.getCoordSyst());
        subDungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(subDungeon, new Coordinate(0, 0, 4));

        dungeon.terminate();

        assertTrue(dungeon.isTerminated());
        assertTrue(subDungeon.isTerminated());
        assertTrue(regularShaft.isTerminated());
        assertTrue(teleportLevel.isTerminated());
        assertTrue(transparentLevel.isTerminated());
        assertClassInvariantsDeep(dungeon);
        assertClassInvariantsDeep(subDungeon);
        LevelTest.assertClassInvariants(teleportLevel);
        ShaftTest.assertClassInvariants(regularShaft);
        LevelTest.assertClassInvariants(transparentLevel);
    }

    @Test
    public void terminate_withParent() {
        CompositeDungeon<Square> subDungeon
            = new CompositeDungeon<Square>(transparentLevel.getCoordSyst());
        subDungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(subDungeon, new Coordinate(0, 0, 4));

        subDungeon.terminate();

        assertEquals(null, subDungeon.getParentDungeon());
        assertFalse(dungeon.hasAsSubDungeon(subDungeon));

        assertFalse(dungeon.isTerminated());
        assertTrue(subDungeon.isTerminated());
        assertFalse(regularShaft.isTerminated());
        assertFalse(teleportLevel.isTerminated());
        assertTrue(transparentLevel.isTerminated());
        assertClassInvariantsDeep(dungeon);
        assertClassInvariantsDeep(subDungeon);
        LevelTest.assertClassInvariants(teleportLevel);
        ShaftTest.assertClassInvariants(regularShaft);
        LevelTest.assertClassInvariants(transparentLevel);
    }

    @Test
    public void deleteSquareAt_legal() {
        dungeon.addSubDungeonAt(teleportLevel, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(regularShaft, new Coordinate(0, 0, 0));
        dungeon.addSubDungeonAt(transparentLevel, new Coordinate(0, 0, 4));
        dungeon.deleteSquareAt(telCoord1);
        assertFalse(dungeon.hasSquare(regTelSq1));
        assertFalse(dungeon.isOccupied(telCoord1));
        assertClassInvariantsDeep(dungeon);
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

