package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class TextWindow extends JPanel {
	
	private static final double PIXELS_PER_LETTER = 20.0;
	
	private List<Entry> log;
	
	private static final int OFFSET = 20;;
	
	public TextWindow(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		log = new ArrayList<Entry>();
	}
	
	public void log(String text, Color c) {
		log.add(new Entry(text, c));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		int width = getWidth() - OFFSET*2;
		int height = getHeight() - OFFSET*2;
		
		g.setColor(Color.BLACK);
		g.fillRect(OFFSET, OFFSET, width, height);
		
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		int y = height;
		int i = log.size() - 1;
		while (y > OFFSET && i >= 0) {
			Entry e = log.get(i);
			g.setColor(e.color);
			g.drawString(e.string, OFFSET, y);
			i--;
			y -= PIXELS_PER_LETTER;
		}
	}
	
	private class Entry {
		public String string;
		public Color color;
		public Entry(String s, Color c) {
			string = s;
			color = c;
		}
	}
}
