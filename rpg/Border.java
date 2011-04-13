package rpg;

import rpg.util.Couple;
import rpg.exceptions.*;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class of borders that border squares.
 * Note that there is a strong existensial dependency with squares: a 
 * border cannot exist without being linked to at least one square (unless 
 * the border is terminated).
 * 
 *
 * @invar
 * No border has the same square on both sides.
 *   | hasNoDuplicateSquares()
 * @invar
 * Each border has proper associated squares.
 *   | hasProperSquares()
 * ...
 * ...
 * ...
 *
 * @author Roald Frederickx
 */
public abstract class Border {
	
	/** 
	 * Create a new border that is attached to the given square.
	 * Note that this will <b>not</b> initialize the bidirectional 
	 * association from the square to this border! It is your duty to make 
	 * that link with this newly created border.
	 * Hence, the newly object will still be raw!
	 * 
	 * @param square 
	 * The square to attach this border to.
	 * @post
	 *   | bordersOnSquare(square)
	 */
	@Raw
	Border(Square square) {
		squares = new Couple<Square>(square);
	}

	/** 
	 * Create a new border that replaces the given border.
	 * 
	 * @param border 
	 * The border to replace with this new border.
	 * @pre
	 *   | border != null  &amp;&amp;  !border.isTerminated()
	 * @post
	 *   | (new border).isTerminated()
	 * @post
	 * This new border borders every square that the old given border 
	 * bordered.
	 */
	@Raw
	public Border(Border border) {
		assert border != null  &&  !border.isTerminated();
		this.squares = border.squares;
		for (Square square : squares)
			square.updateBorder(border, this);
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
	 * @pre
	 * @pre
	 */
	//XXX package visibility only?
	public void mergeWith(Border other) throws BorderConstraintsException {
		assert other != null;
		assert !this.isTerminated();
		assert !other.isTerminated();
		assert !this.isSharedByTwoSquares();
		assert !other.isSharedByTwoSquares();
		//no loops, see invar
		assert this.squares.getAnElement() != other.squares.getAnElement();

		//Keep the least open border
		Border newBorder;		//the border to keep
		Border otherBorder;		//the other border
		Square foreignSquare;	//the square that goes with this other border
		if (this.openness() <= other.openness()){
			newBorder = this;
			otherBorder = other;
		} else {
			newBorder = other;
			otherBorder = this;
		}
		foreignSquare = otherBorder.squares.getAnElement();
		newBorder.squares.add(foreignSquare);
		foreignSquare.updateBorder(otherBorder, newBorder);
	}

	/** 
	 * Returns whether or not this border borders on the given square. 
	 * 
	 * @param square 
	 * The square to check.
	 * @pre
	 *   | square != null
	 * @return 
	 * Whether or not this border borders on the given square. This is 
	 * false if this square is terminated.
	 */
	@Raw
	public boolean bordersOnSquare(Square square) {
		assert square != null;
		if (isTerminated())
			return false;
		return squares.contains(square);
	}

	/** 
	 * Returns the neighbouring square of the given square along this 
	 * border. 
	 * 
	 * @param square 
	 * The square to get the neighbour from.
	 * @pre
	 *   | !isTerminated()
	 * @return 
	 * Null if the given square is not part of this border, otherwise 
	 * return the neighbour, or null if this border has only one assocated 
	 * square (the given one).
	 */
	public Square getNeighbour(Square square) {
		assert !isTerminated();
		return squares.getPartner(square);
	}

	/** 
	 * Check whether this border has no duplicate squares.
	 * 
	 * @return 
	 * If this border is terminated, or if this border is not shared by two 
	 * squares, then the result is false. Otherwise, the result is the 
	 * equality of the two associated squares of this border.
	 */
	public boolean hasNoDuplicateSquares() {
		if (isTerminated() || !isSharedByTwoSquares())
			return true;
		Square square1 = squares.getAnElement();
		Square square2 = squares.getPartner(square1);
		return !square1.equals(square2);
	}



	/** 
	 * Returns whether this border is shared by two squares. 
	 * 
	 * @pre
	 *   | !isTerminated()
	 * @return 
	 * Whether this border is shared by two squares. 
	 */
	public boolean isSharedByTwoSquares() {
		assert !isTerminated();
		return squares.getNbElements() == 2;
	}

	/** 
	 * Detatch the given square from this border.
	 *
	 * @param square 
	 * The square to detatch from this border
	 * @pre
	 *   | !isTerminated() &amp;&amp; !square.hasBorder(this)
	 * @post
	 *   | !new.bordersOnSquare(square)
	 * @post
	 *   | if (!old.isSharedByTwoSquares())
	 *   | then (new.isTerminated())
	 */
	void detatchFromSquare(@Raw Square square) {
		assert !isTerminated() && !square.hasBorder(this);

		if (!isSharedByTwoSquares()) {
			terminate();
			return;
		}
		squares.delete(square);
	}

	/** 
	 * Check whether this border has proper squares.
	 * 
	 * @return 
	 * True if this border is not terminated and all of the squares it 
	 * is adjacent to also have this border as a border.
	 */
	public boolean hasProperSquares() {
		if (isTerminated())
			return (squares == null);

		Square square1 = squares.getAnElement();
		Square square2 = squares.getPartner(square1);
		if (!square1.hasBorder(this))
			return false;

		return square2 == null  ||  square2.hasBorder(this);
	}
		
	/** 
	 * Variable referencing the square(s) that border(s) on this border.
	 */
	protected Couple<Square> squares;

	/**
	 * Return the termination status for this border.
	 */
	@Basic @Raw
	public boolean isTerminated() {
		return isTerminated;
	}
	
	/** 
	 * Terminate this border.
	 *
	 * @pre
	 * None of the associated squares may still have this border as a 
	 * border.
	 * @post
	 *   | new.isTerminated()
	 */
	@Raw
	private void terminate(){
		if (isTerminated())
			return;

		Square square1 = squares.getAnElement();
		Square square2 = squares.getPartner(square1);
		assert (!square1.hasBorder(this));
		assert (square2 == null  ||  !square2.hasBorder(this));

		squares = null;
		isTerminated = true;
	}
	
	/**
	 * Variable registering the termination status for this border.
	 */
	private boolean isTerminated = false;


	/** 
	 * Returns the level of 'openness' of a border. Only relative values 
	 * will be used to determine which type of border will result from 
	 * merging two borders.
	 *
	 * @return
	 * The openness of a border.
	 */
	protected abstract int openness();
}
