package rpg;

public enum Direction {
	NORTH {
		public String symbol() {return "N";}
		public Direction complement() {return SOUTH;}
	}, 
	SOUTH {
		public String symbol() {return "S";}
		public Direction complement() {return NORTH;}
	}, 
	WEST {
		public String symbol() {return "W";}
		public Direction complement() {return EAST;}
	}, 
	EAST {
		public String symbol() {return "E";}
		public Direction complement() {return WEST;}
	}, 
	DOWN {
		public String symbol() {return "D";}
		public Direction complement() {return UP;}
	}, 
	UP {
		public String symbol() {return "U";}
		public Direction complement() {return DOWN;}
	};

	public abstract String symbol();
	public abstract Direction complement();
}

