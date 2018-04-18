package GUI;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Main.Client;
import charData.Character;

public class CharacterView extends JPanel {
	
	
	public CharacterView() {
		setPreferredSize(new Dimension(200, 200));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Character guy = Client.hub.getPerson();
		if (guy != null) {
			g.drawImage(guy.toImage(), 0, 0, getWidth(), getHeight(), null);
		}
	}

}
