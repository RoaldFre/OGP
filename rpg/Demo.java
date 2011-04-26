package rpg;

/**
 * A class demonstrating the Square class.
 *
 * @author Roald Frederickx
 */
public class Demo {
	
	/** 
	 * Demo method for the Square class.
	 */
	public static void main(String args[]) {
		Square square_0_10_0 = new Square(new Temperature(100), 5000);
		new Wall(square_0_10_0.getBorderAt(Direction.WEST));
		new Wall(square_0_10_0.getBorderAt(Direction.SOUTH));
		new Wall(square_0_10_0.getBorderAt(Direction.EAST));

		Square square_1_10_0 = new Square(new Temperature(90),  2500);
		new Wall(square_1_10_0.getBorderAt(Direction.SOUTH));
		new Wall(square_1_10_0.getBorderAt(Direction.WEST));
		new Wall(square_1_10_0.getBorderAt(Direction.NORTH));

		Square square_0_11_0 = new Square(new Temperature(100), 5000);
		new Wall(square_0_11_0.getBorderAt(Direction.WEST));
		new Wall(square_0_11_0.getBorderAt(Direction.NORTH));
		new Door(square_0_11_0.getBorderAt(Direction.EAST), false);

		Square square_1_11_0 = new Square(new Temperature(-30), 7000);
		new Wall(square_1_11_0.getBorderAt(Direction.EAST));

		Square square_1_12_0 = new Square(new Temperature(-30), 5000);
		new Wall(square_1_12_0.getBorderAt(Direction.EAST));
		new Wall(square_1_12_0.getBorderAt(Direction.WEST));

		Dungeon dungeon = new Dungeon(new Coordinate(20, 20, 20));
		dungeon.addSquareAt(new Coordinate(0, 10, 0), square_0_10_0);
		dungeon.addSquareAt(new Coordinate(1, 10, 0), square_1_10_0);
		dungeon.addSquareAt(new Coordinate(0, 11, 0), square_0_11_0);
		dungeon.addSquareAt(new Coordinate(1, 11, 0), square_1_11_0);
		dungeon.addSquareAt(new Coordinate(1, 12, 0), square_1_12_0);

		System.out.println("Before opening door:");
		System.out.println(square_0_10_0);

		Door door = (Door) square_0_11_0.getBorderAt(Direction.EAST);
		door.open();

		System.out.println("\nAfter opening door:");
		System.out.println(square_0_10_0);
	}
}
