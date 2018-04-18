package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import Main.Client;
import charData.Character;
import world.locations.Arena;
import world.roomStuffs.Room;

public class BattleFieldViewer extends JPanel {
	
	private Room arena;
	private Point selected;
	private String currentName;
	
	public BattleFieldViewer(Room r, int size) {
		setPreferredSize(new Dimension(size, size));
		arena = r;
		currentName = "";
		addMouseControls();
	}
	
	private void addMouseControls() {
		BattleFieldViewer ths = this;
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point onscreen = e.getPoint();
				selected = simplify(onscreen);
				synchronized (ths) {
					ths.notify();
				}
			}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseReleased(MouseEvent arg0) {}
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override public void mouseDragged(MouseEvent arg0) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				Point onscreen = e.getPoint();
				Point loc = simplify(onscreen);
				if (arena.withinArea(loc.x, loc.y)) {
					currentName = arena.getName(loc.x, loc.y);
				} else {
					currentName = "";
				}
			}
		});
	}
	
	private Point simplify(Point p) {
		double xScale = (double) getWidth() / (arena.getWidth());
		double yScale = (double) (getHeight()-15) / (arena.getHeight());
		int fieldX = (int) (p.x / xScale);
		int fieldY = arena.getHeight() - (int) (p.y / yScale) - 1;
		return new Point(fieldX, fieldY);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		double xScale = (double) getWidth() / (arena.getWidth());
		double yScale = (double) (getHeight() - 15) / (arena.getHeight());
		for (int i = 0; i < arena.getWidth(); i++) {
			for (int j = 0; j < arena.getHeight(); j++) {
				g.setColor(arena.getColor(i, j));
				g.fillRect((int) (i*xScale), (int) ((getHeight() - 15) - (j + 1)*yScale), (int) xScale, (int) yScale);
				g.setColor(Color.BLACK);
				g.drawRect((int) (i*xScale), (int) ((getHeight() - 15) - (j + 1)*yScale), (int) xScale, (int) yScale);
			}
		}
		
		g.drawString(currentName, 0, getHeight());
	}
	
	public Point getSelectedPoint() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return selected;
	}
}
