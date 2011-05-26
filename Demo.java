import rpg.*;
import rpg.util.*;


/**
 * A class demonstrating the Dungeon class.
 *
 * @author Roald Frederickx
 */
public class Demo {
    
    /** 
     * Demo method for the Dungeon class.
     */
    public static void main(String args[]) {
        //create and pupulate a level and a shaft
        Level<Square> level = new Level<Square>(new Coordinate(0, 0, 9), 3, 3);
        for (Coordinate coordinate : level.getCoordSyst())
            level.addSquareAt(coordinate, new RegularSquare());

        Shaft<Square> shaft = new Shaft<Square>(new Coordinate(1, 3, 9),
                                                        5, Direction.NORTH);
        for (Coordinate coordinate : shaft.getCoordSyst())
            shaft.addSquareAt(coordinate, new RegularSquare());

        //make central square of shaft teleport to central square of level
        Square teleportationDest = level.getSquareAt(new Coordinate(1, 1, 9));
        Teleporter teleporter = new Teleporter(teleportationDest);
        TeleportationSquare teleportationSquare = 
                                new RegularTeleportationSquare(teleporter);
        shaft.deleteSquareAt(new Coordinate(1, 5, 9));
        shaft.addSquareAt(new Coordinate(1, 5, 9), teleportationSquare);

        //put wall at beginning of shaft
        new Wall(shaft.getSquareAt(new Coordinate(1, 3, 9)).getBorderAt(
                                                            Direction.SOUTH));

        //add level and shaft to a new composite dungeon
        CompositeDungeon<Square> dungeon = new CompositeDungeon<Square>(
                            new CoordinateSystem(new Coordinate(0, 0, 9),
                                                 new Coordinate(2, 7, 9)));
        dungeon.addSubDungeonAt(Coordinate.ORIGIN, level);
        dungeon.addSubDungeonAt(Coordinate.ORIGIN, shaft);

        for (Coordinate coordinate : level.getCoordSyst())
            System.out.println(
                  "Can navigate from the teleportation square to "
                  + "the square at " + coordinate + ": "
                  + teleportationSquare.canNavigateTo(
                                             level.getSquareAt(coordinate)));
        System.out.println(
              "Can navigate from the teleportation destination back to the "
              + "teleportation square in the shaft: "
              + teleportationDest.canNavigateTo(teleportationSquare));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

