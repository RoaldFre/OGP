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
	 * @effect
	 *   | equilibrateSquares()
	 */
	@Raw
	public OpenBorder(Square square) {
		super(square);
		equilibrateSquares();
	}

	/** 
	 * Create a new open border that replaces the given border.
	 * 
	 * @param border
	 * The border to replace with this new open border.
	 * @effect
	 *   | super(border)
	 * @effect
	 *   | equilibrateSquares()
	 */
	@Raw
	public OpenBorder(Border border) {
		super(border);
		equilibrateSquares();
	}

	@Override
	@Immutable
	public boolean isOpen() {
		return true;
	}

	/** 
	 * Return a small string as a symbol for this open border.
	 * 
	 * @return 
	 *   | result.equals("O")
	 */
	@Override
	@Immutable
	public String symbol() {
		return "O";
	}
	@Override
	@Immutable
	protected int openness() {
		return 30;
	}
}
