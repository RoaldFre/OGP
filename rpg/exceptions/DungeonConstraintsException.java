package rpg.exceptions;

import rpg.*;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signalling a violation of the constraints on dungeons.
 * 
 * @author Roald Frederickx
 */
public class DungeonConstraintsException extends RuntimeException {
	/** 
	 * Initialize this new dungeon constraints exception with the given 
	 * offending square, dungeon and direction.
	 *
	 * @param square
	 * The offending square for this new dungeon constraints exception.
	 * @param dungeon
	 * The offending dungeon for this new dungeon constraints exception.
	 * @post
	 * The offending dungeon for this new dungeon constraints exception is 
	 * equal to the given offending dungeon.
	 *   | new.getBorder() == dungeon
	 * @post
	 * The offending square for this new dungeon constraints exception is 
	 * equal to the given offending square.
	 *   | new.getSquare() == square
	 */
	public DungeonConstraintsException(Square square, Dungeon dungeon) {
		this.square = square;
		this.dungeon = dungeon;
	}

	/**
	 * Return the offending square for this dungeon constraints exception.
	 */
	@Immutable @Raw
	public Square getSquare() {
		return square;
	}
	
	/**
	 * Variable registering the offending square for this dungeon 
	 * constraints exception.
	 */
	private Square square;

	/**
	 * Return the offending dungeon for this dungeon constraints exception.
	 */
	@Immutable @Raw
	public Dungeon getDungeon() {
		return dungeon;
	}
	
	/**
	 * Variable registering the offending dungeon for this dungeon 
	 * constraints exception.
	 */
	private Dungeon dungeon;

	static final long serialVersionUID = 1;
}

