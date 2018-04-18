package GUI.graphicsEngine;

public class D20 extends Die {
	public D20() {
		super(getSides());
	}

	private static final double CENTER = (2 * 5 * Math.cos(60 * Math.PI / 180.0) + 5 * Math.cos(30 * Math.PI / 180.0))
			/ 2.0;
	public static final Point CENTER_POINT = new Point(0, 0, CENTER);
	
	@Override
	public Point getCenter() {
		return CENTER_POINT;
	}

	// makes a polygon array of a d20
	private static DieSide[] getSides() {
		double l = 5;
		double rad = Math.PI / 180.0;
		double l1 = l * Math.cos(30 * rad);
		double z1 = l * Math.cos(60 * rad);
		Point p = new Point();
		Point p2 = new Point(0, l1, z1);
		Point p3 = new Point(l1 * Math.cos(18 * rad), l1 * Math.sin(18 * rad), z1);
		Point p4 = new Point(.5 * l, -l1 * Math.sin(72 * rad), z1);
		Point p5 = new Point(-.5 * l, -l1 * Math.sin(72 * rad), z1);
		Point p6 = new Point(-l1 * Math.cos(18 * rad), l1 * Math.sin(18 * rad), z1);

		double z2 = z1 + l1;

		Point p7 = new Point(.5 * l, l1 * Math.sin(72 * rad), z2);
		Point p8 = new Point(l1 * Math.cos(18 * rad), -l1 * Math.sin(18 * rad), z2);
		Point p9 = new Point(0, -l1, z2);
		Point p10 = new Point(-l1 * Math.cos(18 * rad), -l1 * Math.sin(18 * rad), z2);
		Point p11 = new Point(-0.5 * l, l1 * Math.sin(72 * rad), z2);

		Point p12 = new Point(0, 0, 2 * z1 + l1);

		DieSide[] poly = new DieSide[20];

		// ALL POINTS MUST BE COPIES, or else rotating multiple polygons using
		// the same
		// point causes over-rotation.
		// top cap
		Point[] arr = { p.copy(), p2.copy(), p3.copy() };
		poly[0] = new DieSide(1, arr);

		Point[] arr2 = { p.copy(), p3.copy(), p4.copy() };
		poly[1] = new DieSide(2, arr2);

		Point[] arr3 = { p.copy(), p4.copy(), p5.copy() };
		poly[2] = new DieSide(3, arr3);

		Point[] arr4 = { p.copy(), p5.copy(), p6.copy() };
		poly[3] = new DieSide(4, arr4);

		Point[] arr5 = { p.copy(), p6.copy(), p2.copy() };
		poly[4] = new DieSide(5, arr5);

		// middle ring top
		Point[] arr6 = { p2.copy(), p7.copy(), p3.copy() };
		poly[5] = new DieSide(6, arr6);

		Point[] arr7 = { p3.copy(), p8.copy(), p4.copy() };
		poly[6] = new DieSide(7, arr7);

		Point[] arr8 = { p4.copy(), p9.copy(), p5.copy() };
		poly[7] = new DieSide(8, arr8);

		Point[] arr9 = { p5.copy(), p10.copy(), p6.copy() };
		poly[8] = new DieSide(9, arr9);

		Point[] arr10 = { p6.copy(), p11.copy(), p2.copy() };
		poly[9] = new DieSide(10, arr10);

		// middle ring bottom
		Point[] arr11 = { p11.copy(), p2.copy(), p7.copy() };
		poly[10] = new DieSide(11, arr11);

		Point[] arr12 = { p7.copy(), p3.copy(), p8.copy() };
		poly[11] = new DieSide(12, arr12);

		Point[] arr13 = { p8.copy(), p4.copy(), p9.copy() };
		poly[12] = new DieSide(13, arr13);

		Point[] arr14 = { p9.copy(), p5.copy(), p10.copy() };
		poly[13] = new DieSide(14, arr14);

		Point[] arr15 = { p10.copy(), p6.copy(), p11.copy() };
		poly[14] = new DieSide(15, arr15);

		// bottom cap
		Point[] arr16 = { p12.copy(), p7.copy(), p8.copy() };
		poly[15] = new DieSide(16, arr16);

		Point[] arr17 = { p12.copy(), p8.copy(), p9.copy() };
		poly[16] = new DieSide(17, arr17);

		Point[] arr18 = { p12.copy(), p9.copy(), p10.copy() };
		poly[17] = new DieSide(18, arr18);

		Point[] arr19 = { p12.copy(), p10.copy(), p11.copy() };
		poly[18] = new DieSide(19, arr19);

		Point[] arr20 = { p12.copy(), p11.copy(), p7.copy() };
		poly[19] = new DieSide(20, arr20);

		return poly;
	}
}
