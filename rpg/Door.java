package rpg;

import be.kuleuven.cs.som.annotate.*;

public class Door extends Border{
	/** 
	 * Create a new door.
	 * 
	 * @param isOpened 
	 * Whether or not this new door is opened.
	 * @post
	 *   | new.isOpen() == isOpened
	 */
	public Door(Square square, boolean isOpened) {
		super(square);
		this.isOpened = isOpened;
	}

	@Override
	public boolean isOpen() {
		return isOpened;
	}

	@Override
	@Immutable
	public boolean isDoor() {
		return true;
	}

	@Override
	@Immutable
	public boolean isWall() {
		return false;
	}

	@Override
	@Immutable
	public boolean isSlippery() {
		return false;
	}

	@Immutable
	protected int openness() {
		return 20;
	}

	/**
	 * Open this door.
	 *
	 * @post
	 *   | new.isOpened() == true
	 */
	@Raw
	public void open() {
		this.isOpened = true;
	}

	/**
	 * Close this door.
	 *
	 * @post
	 *   | new.isOpened() == false
	 */
	@Raw
	public void close() {
		this.isOpened = false;
	}
	
	/**
	 * Variable registering the opened or closed state for this door.
	 */
	private boolean isOpened;
}
