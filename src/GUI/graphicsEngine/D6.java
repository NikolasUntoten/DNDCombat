package GUI.graphicsEngine;

public class D6 extends Die {
	public D6() {
		super(getSides());
	}
	
	public static final Point CENTER_POINT = new Point(0, 0, 0);
	
	@Override
	public Point getCenter() {
		return CENTER_POINT;
	}
	
	private static final int s = 3;;
	
	private static DieSide[] getSides() {
		DieSide[] polyArr = new DieSide[6];
		Point[] p = new Point[8];
		p[0] = new Point(-s, s, s);
		p[1] = new Point(-s, -s, s);
		p[2] = new Point(s, -s, s);
		p[3] = new Point(s, s, s);
		p[4] = new Point(-s, s, -s);
		p[5] = new Point(-s, -s, -s);
		p[6] = new Point(s, -s, -s);
		p[7] = new Point(s, s, -s);
		
		Point[] arr0 = {p[0].copy(), p[1].copy(), p[2].copy(), p[3].copy()};
		polyArr[0] = new DieSide(1, arr0);
		
		Point[] arr1 = {p[3].copy(), p[2].copy(), p[6].copy(), p[7].copy()};
		polyArr[1] = new DieSide(2, arr1);
		
		Point[] arr2 = {p[4].copy(), p[5].copy(), p[6].copy(), p[7].copy()};
		polyArr[2] = new DieSide(3, arr2);
		
		Point[] arr3 = {p[1].copy(), p[0].copy(), p[4].copy(), p[5].copy()};
		polyArr[3] = new DieSide(4, arr3);
		
		Point[] arr4 = {p[2].copy(), p[1].copy(), p[5].copy(), p[6].copy()};
		polyArr[4] = new DieSide(5, arr4);
		
		Point[] arr5 = {p[3].copy(), p[0].copy(), p[4].copy(), p[7].copy()};
		polyArr[5] = new DieSide(6, arr5);
		
		return polyArr;
	}
}
