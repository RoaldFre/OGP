package rpg;

import be.kuleuven.cs.som.annotate.*;

public class Wall extends Border{
	public Wall(Square square, boolean isSlippery) {
		super(square);
		this.isSlippery = isSlippery;
	}

	@Override
	@Immutable
	public boolean isOpen() {
		return false;
	}

	@Override
	@Immutable
	public boolean isDoor() {
		return false;
	}

	@Override
	@Immutable
	public boolean isWall() {
		return true;
	}

	@Override
	@Immutable
	public boolean isSlippery() {
		return isSlippery;
	}

	@Immutable
	protected int openness() {
		return 10;
	}

	/** 
	 * Variable registering the slipperyness of this wall.
	 */
	private boolean isSlippery;
}
