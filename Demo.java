public class Demo {
	public static void main(String args[]) {
		Square square1 = new Square(new Temperature(150), 5901);
		square1.setHasSlipperyFloor(true);
		square1.setBorderAt(4,false);
		square1.setBorderAt(5,false);
		square1.setBorderAt(6,false);

		Square square2 = new Square(new Temperature(40), 8990);
		square2.setBorderAt(1,false);
		square2.setBorderAt(5,false);
		square2.setBorderAt(6,false);

		System.out.println("Before merging:");
		System.out.println("square1:");
		System.out.println(square1);
		System.out.println("\nsquare2:");
		System.out.println(square2);

		square1.mergeWith(square2, 2);

		System.out.println("\n\nAfter merging:");
		System.out.println("square1:");
		System.out.println(square1);
		System.out.println("\nsquare2:");
		System.out.println(square2);
	}
}
