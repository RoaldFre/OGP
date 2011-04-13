package rpg;

public enum Direction {
	NORTH {
		public Direction complement() {return SOUTH;}
	}, 
	SOUTH {
		public Direction complement() {return NORTH;}
	}, 
	WEST {
		public Direction complement() {return EAST;}
	}, 
	EAST {
		public Direction complement() {return WEST;}
	}, 
	DOWN {
		public Direction complement() {return UP;}
	}, 
	UP {
		public Direction complement() {return DOWN;}
	};

	public abstract Direction complement();
}

