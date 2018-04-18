package GUI.graphicsEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ImagePolygon {
	Point[] points;
	private BufferedImage image;

	public ImagePolygon(double[] xPoints, double[] yPoints, double[] zPoints, int nPoints) {
		if (xPoints.length != nPoints || yPoints.length != nPoints || zPoints.length != nPoints) {
			throw new IllegalArgumentException();
		}
		points = new Point[nPoints];
		for (int i = 0; i < nPoints; i++) {
			points[i] = new Point(xPoints[i], yPoints[i], zPoints[i]);
		}
	}

	public ImagePolygon(Point[] points) {
		this.points = points;
	}

	public void setImage(BufferedImage i) {
		image = i;
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
		if (image != null)
			imFill(g, reference, angle);
		else
			colorFill(g, reference, angle);
	}

	// Fills the polygon with a given image
	private void imFill(Graphics g, Point reference, int angle) {
		int imW = image.getWidth(null);
		int imH = image.getHeight(null);

		double xLow = minX();
		double yLow = minY();
		double zLow = minZ();

		Point ref = points[1];
		double dx = ref.x - xLow;
		double dy = ref.y - yLow;
		double dz = ref.z - zLow;
		double xRot = Math.atan(dy / dz);
		double yRot = Math.atan(dx / dz);
		double zRot = Math.atan(dx / dy);

		Map<java.awt.Point, Point> imageToReal = new HashMap<java.awt.Point, Point>();
		Point zero = new Point(0, 0, 0);
		for (int x = 0; x < imW; x++) {
			for (int y = 0; y < imH; y++) {
				Point p = new Point(x, y, 0);
				// p.rotateAroundX(zero, xRot);
				// p.rotateAroundY(zero, yRot);
				// p.rotateAroundZ(zero, zRot);
				p.translate(xLow, yLow, zLow);
				imageToReal.put(new java.awt.Point(x, y), p);
			}
		}

		for (int x = 0; x < imW; x++) {
			for (int y = 0; y < imH; y++) {
				Point p = imageToReal.get(new java.awt.Point(x, y));
				Color c = new Color(image.getRGB(x, y));
				g.setColor(c);
				Point p2 = p.getDrawingPoint(reference, angle);
				g.drawRect((int) p2.x, (int) p2.y, 1, 1);
			}
		}
	}

	private double minX() {
		double lowest = points[0].x;
		for (Point p : points) {
			if (p.x < lowest)
				lowest = p.x;
		}
		return lowest;
	}

	private double minY() {
		double lowest = points[0].y;
		for (Point p : points) {
			if (p.y < lowest)
				lowest = p.y;
		}
		return lowest;
	}

	private double minZ() {
		double lowest = points[0].z;
		for (Point p : points) {
			if (p.z < lowest)
				lowest = p.z;
		}
		return lowest;
	}

	private void colorFill(Graphics g, Point reference, int angle) {
		int n = points.length;
		int[] xPoints = new int[n];
		int[] yPoints = new int[n];
		for (int i = 0; i < points.length; i++) {
			Point p = points[i].getDrawingPoint(reference, angle);
			xPoints[i] = (int) p.x;
			yPoints[i] = (int) p.y;
		}

		g.setColor(Color.RED);
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
