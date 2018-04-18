package GUI.graphicsEngine;

import java.awt.Color;
import java.awt.Graphics;

public class DieSide extends Polygon {
	private int value;

	public DieSide(int value, double[] xPoints, double[] yPoints, double[] zPoints, int nPoints) {
		super(xPoints, yPoints, zPoints, nPoints);
		this.value = value;
	}

	public DieSide(int value, Point[] points) {
		super(points);
		this.value = value;
	}

	@Override
	public void fill(Graphics g, Point reference, int angle) {
		super.fill(g, reference, angle);
		Point pDraw = drawingCenter(reference, angle);
		g.setColor(Color.BLACK);
		g.drawString("" + value, (int) pDraw.x, (int) pDraw.y);
	}

	private Point drawingCenter(Point reference, int angle) {
		double x = 0;
		double y = 0;
		for (Point p : points) {
			Point pDraw = p.getDrawingPoint(reference, angle);
			x += pDraw.x;
			y += pDraw.y;
		}
		x /= points.length;
		y /= points.length;
		return new Point(x, y, 0);
	}

	public int getValue() {
		return value;
	}
}
