package rpg.exceptions;

import rpg.dungeon.Dungeon;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signalling a problem with adding a subdungeon to a dungeon, 
 * where the subdungeon is already associated to another dungeon.
 * 
 * @author Roald Frederickx
 */
public class DungeonAlreadyAssociatedException extends RuntimeException {
    /**
     * Initialize this new dungeon already associated exception with the 
     * given offending subdungeon and dungeon.
     *
     * @param subDungeon
     * The offending subdungeon for this new dungeon already associated 
     * exception.
     * @param dungeon
     * The offending dungeon for this new dungeon already associated 
     * exception.
     * @post
     *   | new.getSubDungeon() == subDungeon
     * @post
     *   | new.getDungeon() == dungeon
     */
    public DungeonAlreadyAssociatedException(Dungeon<?> subDungeon,
                                             Dungeon<?> dungeon) {
        this.subDungeon = subDungeon;
        this.dungeon = dungeon;
    }

    /**
     * Return the offending subdungeon for this dungeon already associated 
     * exception.
     */
    @Immutable @Raw
    public Dungeon<?> getSubDungeon() {
        return subDungeon;
    }

    /**
     * Variable registering the offending subdungeon for this dungeon 
     * already associated exception.
     */
    private final Dungeon<?> subDungeon;

    /**
     * Return the offending dungeon for this dungeon already associated 
     * exception.
     */
    @Immutable @Raw
    public Dungeon<?> getDungeon() {
        return dungeon;
    }
    
    /**
     * Variable registering the offending dungeon for this dungeon already 
     * associated exception.
     */
    private final Dungeon<?> dungeon;

    static final long serialVersionUID = 1;
}

// vim: ts=4:sw=4:expandtab:smarttab

