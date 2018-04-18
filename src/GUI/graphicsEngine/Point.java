package GUI.graphicsEngine;

import java.awt.Graphics;

public class Point {
	public double x, y, z;

	public Point() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Point(double ix, double iy, double iz) {
		x = ix;
		y = iy;
		z = iz;
	}

	public void draw(Graphics g, Point reference, int angle) {
		Point p = getDrawingPoint(reference, angle);
		g.drawRect((int) p.x, (int) p.y, 1, 1);
	}

	public Point getDrawingPoint(Point reference, int angle) {
		angle += 180;
		angle %= 360;
		angle -= 180;
		double rad = Math.PI / 180.0;

		double dx = x - reference.x;
		double dy = y - reference.y;
		double dz = z - reference.z;

		// double xzDist = Math.sqrt(dx * dx + dz * dz);
		// System.out.println(xzDist);
		double xzTheta = Math.atan2(dx, dz);
		// double yzDist = Math.sqrt(dy * dy + dz * dz);
		double yzTheta = Math.atan2(dy, dz);
		xzTheta -= (angle * rad);
		if (dz < 0)
			yzTheta -= Math.PI;// xzTheta *= -1;
		// System.out.println(yzTheta / rad + " degrees");
		int cx = DiceRoller.getCenterX();
		int cy = DiceRoller.getCenterY();
		double xFinal = cx + (int) (xzTheta * cx*3);
		double yFinal = cy - (int) (yzTheta * cy*3);
		return new Point(xFinal, yFinal, 0);
	}

	public double distance(Point reference) {
		double x = this.x - reference.x;
		double y = this.y - reference.y;
		double z = this.z - reference.z;

		return Math.sqrt(x * x + y * y + z * z);
	}

	public void rotateAroundX(Point ref, double degrees) {
		double rad = Math.PI / 180.0;
		double dy = y - ref.y;
		double dz = z - ref.z;
		double cos = Math.cos(degrees * rad);
		double sin = Math.sin(degrees * rad);
		y = dy * cos - dz * sin + ref.y;
		z = dy * sin + dz * cos + ref.z;
	}

	public void rotateAroundY(Point ref, double degrees) {
		double rad = Math.PI / 180.0;
		double dx = x - ref.x;
		double dz = z - ref.z;
		double cos = Math.cos(degrees * rad);
		double sin = Math.sin(degrees * rad);
		x = dx * cos - dz * sin + ref.x;
		z = dx * sin + dz * cos + ref.z;
	}

	public void rotateAroundZ(Point ref, double degrees) {
		double rad = Math.PI / 180.0;
		double dx = x - ref.x;
		double dy = y - ref.y;
		double cos = Math.cos(degrees * rad);
		double sin = Math.sin(degrees * rad);
		x = dx * cos - dy * sin + ref.x;
		y = dx * sin + dy * cos + ref.y;
	}

	public Point copy() {
		return new Point(x, y, z);
	}

	public void translate(double deltaX, double deltaY, double deltaZ) {
		x += deltaX;
		y += deltaY;
		z += deltaZ;
	}
}
