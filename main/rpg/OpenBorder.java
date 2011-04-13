package rpg;

import be.kuleuven.cs.som.annotate.*;

public class OpenBorder extends Border{

	/** 
	 * Create a new open border that borders the given square.
	 * 
	 * @param square 
	 * The square that this open border will border.
	 * @effect
	 *   | super(square)
	 */
	@Raw
	public OpenBorder(Square square) {
		super(square);
	}

	/** 
	 * Create a new open border that replaces the given border.
	 * 
	 * @param border
	 * The border to replace with this new open border.
	 * @effect
	 *   | super(border)
	 */
	@Raw
	public OpenBorder(Border border) {
		super(border);
	}

	@Override
	@Immutable
	public boolean isOpen() {
		return true;
	}

	@Override
	@Immutable
	public boolean isDoor() {
		return false;
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
		return 30;
	}
}
