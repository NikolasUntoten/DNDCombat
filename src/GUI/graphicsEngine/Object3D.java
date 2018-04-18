package GUI.graphicsEngine;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Object3D {
	Polygon[] polys;

	public Object3D(Polygon[] initPolys) {
		polys = initPolys;
	}

	public void draw(Graphics g, Point reference, int angle) {
		for (Polygon p : polys) {
			p.draw(g, reference, angle);
		}
	}

	public void fill(Graphics g, Point reference, int angle) {
		Polygon[] polyArr = sort(polys, reference);

		for (int i = polyArr.length - 1; i >= 0; i--) {
			polyArr[i].fill(g, reference, angle);
		}
	}

	protected Polygon[] sort(Polygon[] polyArr, Point reference) {
		ArrayList<Polygon> arr = new ArrayList<Polygon>();
		for (Polygon p : polyArr) {
			arr.add(p);
		}
		Collections.sort(arr, new Comparator<Polygon>() {
			@Override
			public int compare(Polygon p1, Polygon p2) {
				return (int) (p1.distance(reference) - p2.distance(reference));
			}
		});
		
		Polygon[] result = new Polygon[arr.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = arr.get(i);
		}
		return result;
	}

	public void rotateAroundX(Point ref, double degrees) {
		for (Polygon p : polys) {
			p.rotateAroundX(ref, degrees);
		}
	}

	public void rotateAroundY(Point ref, double degrees) {
		for (Polygon p : polys) {
			p.rotateAroundY(ref, degrees);
		}
	}

	public void rotateAroundZ(Point ref, double degrees) {
		for (Polygon p : polys) {
			p.rotateAroundZ(ref, degrees);
		}
	}

	public void translate(double deltaX, double deltaY, double deltaZ) {
		for (Polygon p : polys) {
			p.translate(deltaX, deltaY, deltaZ);
		}
	}
}
