package GUI.graphicsEngine;

public class Die extends Object3D {
	
	public Die(DieSide[] sides) {
		super(sides);
	}
	
	public Point getCenter() {
		return new Point(0, 0, 0);
	}
	
	public int getValue(Point reference) {
		// Order by distance from reference
		Polygon[] polyArr = this.sort(polys, reference);
		
		// whichever one contains zero gets picked
		java.awt.Point zero = new java.awt.Point(0, 0);
		for (Polygon p : polyArr) {
			int n = p.points.length;
			int[] xPoints = new int[n];
			int[] yPoints = new int[n];
			for (int i = 0; i < n; i++) {
				Point point = p.points[i].getDrawingPoint(reference, 0);
				xPoints[i] = (int) point.x - DiceRoller.getCenterX();
				yPoints[i] = (int) point.y - DiceRoller.getCenterY();
			}
			java.awt.Polygon drawPol = new java.awt.Polygon(xPoints, yPoints, n);
			// System.out.println(((DieSide) p).getValue());
			if (drawPol.contains(zero)) {
				return ((DieSide) p).getValue();
			}
		}
		return 0;
	}
}
