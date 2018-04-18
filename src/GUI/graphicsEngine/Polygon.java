package GUI.graphicsEngine;

import java.awt.Color;
import java.awt.Graphics;

public class Polygon {
	Point[] points;
	private Color color;

	public Polygon(double[] xPoints, double[] yPoints, double[] zPoints, int nPoints) {
		if (xPoints.length != nPoints || yPoints.length != nPoints || zPoints.length != nPoints) {
			throw new IllegalArgumentException();
		}
		points = new Point[nPoints];
		for (int i = 0; i < nPoints; i++) {
			points[i] = new Point(xPoints[i], yPoints[i], zPoints[i]);
		}
		color = Color.RED;
	}

	public Polygon(Point[] points) {
		this.points = points;
		color = Color.RED;
	}
	
	public void setColor(Color c) {
		color = c;
	}

	public void draw(Graphics g, Point reference, int angle) {
		for (int i = 1; i < points.length; i++) {
			Point a = points[i - 1].getDrawingPoint(reference, angle);
			Point b = points[i].getDrawingPoint(reference, angle);

			g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
		}
		// Wrap around
		Point a = points[points.length - 1].getDrawingPoint(reference, angle);
		Point b = points[0].getDrawingPoint(reference, angle);

		g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
	}

	public void fill(Graphics g, Point reference, int angle) {
		int n = points.length;
		int[] xPoints = new int[n];
		int[] yPoints = new int[n];
		for (int i = 0; i < points.length; i++) {
			Point p = points[i].getDrawingPoint(reference, angle);
			xPoints[i] = (int) p.x;
			yPoints[i] = (int) p.y;
		}

		g.setColor(color);
		g.fillPolygon(xPoints, yPoints, n);

		g.setColor(Color.BLACK);
		g.drawPolygon(xPoints, yPoints, n);
	}

	public void rotateAroundX(Point ref, double degrees) {
		for (Point p : points) {
			p.rotateAroundX(ref, degrees);
		}
	}

	public void rotateAroundY(Point ref, double degrees) {
		for (Point p : points) {
			p.rotateAroundY(ref, degrees);
		}
	}

	public void rotateAroundZ(Point ref, double degrees) {
		for (Point p : points) {
			p.rotateAroundZ(ref, degrees);
		}
	}

	public double distance(Point ref) {
		double total = 0;
		for (Point p : points) {
			double dist = p.distance(ref);
			total += dist;
		}
		return total / points.length;
	}

	public void translate(double deltaX, double deltaY, double deltaZ) {
		for (Point p : points) {
			p.translate(deltaX, deltaY, deltaZ);
		}
	}
}
