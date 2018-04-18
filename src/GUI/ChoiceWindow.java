package GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import Main.Client;

public class ChoiceWindow extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Choice> choices;
	private Choice selected;
	private GridBagConstraints cons;

	public ChoiceWindow() {
		setup();
	}

	public ChoiceWindow(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		setup();
	}

	private void setup() {
		this.setLayout(new GridBagLayout());
		cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx = 8;
		cons.weighty = 1;
		cons.gridy = 0;
		
		choices = new ArrayList<Choice>();
		selected = null;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		if (selected != null) {
			g.setColor(Color.GREEN);
			((Graphics2D) g).setStroke(new BasicStroke(3));
			g.drawRect(selected.getX(), selected.getY(), selected.getWidth(), selected.getHeight());
		}
	}

	public void pushChoice(String choice) {
		synchronized (this) {
			this.notifyAll();
		}
		Client.console.logInput(selected.getCommand());
		Client.console.putInput();
	}

	public Choice getSelection() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return selected;
	}

	public void addChoice(Choice choice) {
		choices.add(choice);
		this.add(choice, cons);
		trimListings();
	}

	public void removeChoice(Choice choice) {
		choices.remove(choice);
		this.remove(choice);
		trimListings();
	}

	public void removeAction(String name) {
		for (int i = 0; i < choices.size(); i++) {
			Choice c = choices.get(i);
			if (c.getName().equals(name)) {
				removeChoice(c);
			}
		}
	}

	public void clearChoices() {
		for (Choice l : choices) {
			this.remove(l);
		}
		choices = new ArrayList<Choice>();
		selected = null;
	}

	private void trimListings() {
		if (choices.size() == 0)
			return;
		int height = this.getHeight() / choices.size();
		for (Choice l : choices) {
			//ImageIcon icon = (ImageIcon) l.getIcon();
			//Image i = icon.getImage().getScaledInstance(this.getWidth(), height, Image.SCALE_SMOOTH);
			//l.setIcon(new ImageIcon(i));
		}
	}
}
