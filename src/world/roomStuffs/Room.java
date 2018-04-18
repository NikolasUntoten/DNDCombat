package world.roomStuffs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import charData.Character;

public class Room {
	private int width;
	private int height;
	private Color groundColor;
	private Prop[][] props;
	
	public Room(int initWidth, int initHeight) {
		width = initWidth;
		height = initHeight;
		groundColor = Color.GRAY;
		props = new Prop[width][height];
	}
	
	public Room(int initWidth, int initHeight, Color ground) {
		width = initWidth;
		height = initHeight;
		groundColor = ground;
		props = new Prop[width][height];
	}
	
	public void addPerson(Character p) {
		if (!withinArea(p.getX(), p.getY())) {
			moveToArea(p);
		}
		
		fixCollision(p);
		props[p.getX()][p.getY()] = new Token(p);
	}
	
	public void addProp(Prop p, int x, int y) {
		if (props[x][y] != null) throw new IllegalArgumentException();
		props[x][y] = p;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean withinArea(int x, int y) {
		boolean xWithin = x >= 0 && x < width;
		boolean yWithin = y >= 0 && y < height;
		return xWithin && yWithin;
	}
	
	public Prop get(int x, int y) {
		if (props[x][y] instanceof Token) {
			fix(x, y);
		}
		return props[x][y];
	}
	
	private void fix(int x, int y) {
		Token t = (Token) props[x][y];
		Character c = t.tokenChar;
		if (c.getX() != x || c.getY() != y) {
			props[x][y] = null;
			props[c.getX()][c.getY()] = t;
		}
	}
	
	public Color getColor(int x, int y) {
		Prop p = get(x, y);
		if (p != null) {
			return p.color;
		} else {
			return groundColor;
		}
	}
	
	public String getName(int x, int y) {
		Prop p = get(x, y);
		if (p != null) {
			return p.name;
		} else {
			return "";
		}
	}
	
	public boolean occupied(int x, int y) {
		return props[x][y] != null;
	}
	
	private void fixCollision(Character current) {
		int x = current.getX();
		int y = current.getY();
		while (props[x][y] != null) {
			int deltaX = randDir();
			int deltaY = randDir();
			if (x + deltaX < width && x + deltaX >= 0) {
				current.move(deltaX, 0);
			}
			if (y + deltaY < height && y + deltaY >= 0) {
				current.move(0, deltaY);
			}
			x = current.getX();
			y = current.getY();
		}
	}
	
	private void moveToArea(Character c) {
		int x = c.getX();
		if (x >= width) {
			c.move(width - (x + 1), 0);
		} else if(x < 0) {
			c.move(-x, 0);
		}
		
		int y = c.getY();
		if (y >= height) {
			c.move(0, height - (y + 1));
		} else if(y < 0) {
			c.move(0, -y);
		}
	}
	
	private int randDir() {
		return ((int) (Math.random() * 3) - 1);
	}
	
}
