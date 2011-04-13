package rpg;

import be.kuleuven.cs.som.annotate.*;

public class Door extends Border{
	/** 
	 * Create a new door with the given intrinsic slipperyness that borders 
	 * the given square.
	 * 
	 * @param square 
	 * The square that this door will border.
	 * @param isSlippery 
	 * The 'openness' of this new door.
	 * @post
	 *   | isOpen() == isOpened
	 * @effect
	 *   | super(square)
	 */
	@Raw
	public Door(Square square, boolean isOpened) {
		super(square);
		this.isOpened = isOpened;
	}

	/** 
	 * Create a new door with the given 'openness' that replaces the given 
	 * border.
	 * 
	 * @param border
	 * The border to replace with this new door.
	 * @param isOpened
	 * The 'openness' of this new door.
	 * @post
	 *   | isOpen() == isOpened
	 * @effect
	 *   | super(border)
	 */
	@Raw
	public Door(Border border, boolean isOpened) {
		super(border);
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
