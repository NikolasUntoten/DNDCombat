package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Main.Client;

public class ControlPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 765790272334915868L;
	
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_HEIGHT = 100;
	
	private GridBagConstraints cons;
	
	public ControlPanel() {
		setup();
	}
	
	public ControlPanel(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		setup();
	}
	
	private void setup() {
		this.setLayout(new GridBagLayout());
		cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx = 8;
		cons.weighty = 1;
		cons.gridy = 0;
		
		addButtons();
	}
	
	public void addButtons() {
		addSave();
		addLoad();
		addQuit();
	}
	
	public void addSave() {
		JButton button = new JButton("SAVE");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						Client.save();
					}
				}.start();	
			}
		});
		button.setSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		add(button, cons);
	}
	
	public void addLoad() {
		JButton button = new JButton("LOAD");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						Client.load();
					}
				}.start();
			}
		});
		button.setSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		add(button, cons);
	}
	
	public void addQuit() {
		JButton button = new JButton("QUIT");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						Client.autoSave();
						System.exit(0);
					}
				}.start();
			}
		});
		button.setSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		add(button, cons);
	}
	
}
