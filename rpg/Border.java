package rpg;

import rpg.util.Couple;
import be.kuleuven.cs.som.annotate.*;

public abstract class Border {
	public Border(Square square) {
		squares = new Couple<Square>(square);
	}

	/** 
	 * Returns whether or not this border is open, thus connecting the 
	 * interior of its adjacent squares.
	 */
	public abstract boolean isOpen();

	/** 
	 * Returns whether or not this border is a door.
	 */
	public abstract boolean isDoor();

	/** 
	 * Returns whether or not this border is a wall.
	 */
	public abstract boolean isWall();

	/** 
	 * Returns whether or not this border is slippery.
	 */
	public abstract boolean isSlippery();

	/** 
	 * Merge this border with the given border. 
	 * 
	 * @param other 
	 * The border to merge with this one.
	 * @pre
	 *   | this.squares.getNbElements() == 1
	 * @pre
	 *   | other.squares.getNbElements() == 1
	 * @pre
	 *   | !this.squares.getAnElement().equals(
	 *   |		other.squares.getAnElement())
	 * @pre
	 *   | !this.equals(other) 
	 */
	public void mergeWith(Border other) {
		assert this.squares.getNbElements() == 1;
		assert other.squares.getNbElements() == 1;
		assert !this.squares.getAnElement().equals(
								other.squares.getAnElement());
		//XXX
	}

	/** 
	 * Returns whether or not this border borders on the given square. 
	 * 
	 * @param square 
	 * The square to check.
	 * @return 
	 * Whether or not this border borders on the given square. 
	 */
	public boolean bordersOnSquare(Square square) {
		return squares.contains(square);
	}

	protected Couple<Square> squares;

	/** 
	 * Returns the level of 'openness' of a border. Only relative values 
	 * will be used to determine which type of border will result from 
	 * merging two borders.
	 *
	 * @return
	 * The openness of a border.
	 */
	protected abstract int openness();

	/** 
	 * Returns whether this border is shared by two squares. 
	 * 
	 * @return 
	 * Whether this border is shared by two squares. 
	 */
	public boolean isSharedByTwoSquares() {
		return squares.getNbElements() == 2;
	}

	/**
	 * Return the termination status for this border.
	 */
	@Basic @Raw
	public boolean isTerminated() {
		return isTerminated;
	}
	
	public void terminate(){
		//TODO
		isTerminated = true;
	}
	
	/**
	 * Variable registering the termination status for this border.
	 */
	private boolean isTerminated = false;
}
