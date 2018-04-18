package GUI.graphicsEngine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.TreeMap;

import javax.swing.JPanel;

public class DiceRoller extends JPanel {

	private Point camera;
	private Die currentDie;
	private static int centerX;
	private static int centerY;
	private static boolean rolling;
	private TreeMap<Integer, Die> dice;
	private static final int SLEEP = 50;

	public DiceRoller(int x, int y, int width, int height) {
		setLayout(null);
		setPreferredSize(new Dimension(width, height));
		camera = new Point(0, 0, -15);
		centerX = width / 2;
		centerY = height / 2;
		dice = new TreeMap<Integer, Die>();
		dice.put(20, new D20());
		dice.put(6, new D6());
		rolling = false;
	}
	
	public void test() {
		int[] test = new int[20];
		for (int i = 0; i < 1000000; i++) {
			int result = roll(20);
			test[result - 1]++;
		}
		for (int i = 0; i < test.length; i++) {
			System.out.println("[" + (i + 1) + "] = " + test[i]);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		if (currentDie != null) {
			currentDie.fill(g, camera, 0);
		}
		g.drawLine(centerX, 0, centerX, centerY*2);
        g.drawLine(0, centerY, centerX*2, centerY);
	}
	
	public static int getCenterX() {
		return centerX;
	}
	
	public static int getCenterY() {
		return centerY;
	}

	private static final double DECREMENT = 0.3;

	public int roll(int sides) {
		if (rolling) {
			System.out.println("Already rolling");
			return -1;
		}
		if (!dice.containsKey(sides)) {
			System.out.println("Incorrect number of sides");
			return (int) (Math.random() * sides) + 1;
		}
		currentDie = dice.get(sides);
		rolling = true;
		
		double xr = randSpeed();
		
		double yr = randSpeed();
		
		double zr = randSpeed();
		
		Point center = currentDie.getCenter();
		
		while (Math.abs(xr) > DECREMENT || Math.abs(yr) > DECREMENT || Math.abs(zr) > DECREMENT) {
			currentDie.rotateAroundX(center, xr);
			currentDie.rotateAroundY(center, yr);
			currentDie.rotateAroundZ(center, zr);
			
			if (Math.abs(xr) > DECREMENT)
				xr -= DECREMENT * Math.signum(xr);
			
			if (Math.abs(yr) > DECREMENT)
				yr -= DECREMENT * Math.signum(yr);
			
			if (Math.abs(zr) > DECREMENT)
				zr -= DECREMENT * Math.signum(zr);
			
			try {
				Thread.sleep(SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		rolling = false;
		return currentDie.getValue(camera);
	}

	private double randSpeed() {
		return  10 + Math.random() * 30 * randDirection();
	}

	private int randDirection() {
		return (int) (Math.random() * 2) * 2 - 1;
	}
}
