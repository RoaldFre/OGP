package rpg;

import be.kuleuven.cs.som.annotate.*;

public enum Direction {
	NORTH {
		@Basic public String symbol() {return "N";}
		@Basic public Direction complement() {return SOUTH;}
	}, 
	SOUTH {
		@Basic public String symbol() {return "S";}
		@Basic public Direction complement() {return NORTH;}
	}, 
	WEST {
		@Basic public String symbol() {return "W";}
		@Basic public Direction complement() {return EAST;}
	}, 
	EAST {
		@Basic public String symbol() {return "E";}
		@Basic public Direction complement() {return WEST;}
	}, 
	DOWN {
		@Basic public String symbol() {return "D";}
		@Basic public Direction complement() {return UP;}
	}, 
	UP {
		@Basic public String symbol() {return "U";}
		@Basic public Direction complement() {return DOWN;}
	};

	/** 
	 * Returns a short representation of this direction.
	 */
	@Basic
	public abstract String symbol();

	/** 
	 * Returns the complementary direction of this direction. 
	 */
	@Basic
	public abstract Direction complement();
}

