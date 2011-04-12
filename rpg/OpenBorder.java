package rpg;

import be.kuleuven.cs.som.annotate.*;

public class OpenBorder extends Border{
	public OpenBorder(Square square) {
		super(square);
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
