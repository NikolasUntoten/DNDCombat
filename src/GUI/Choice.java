package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Main.Client;

public class Choice extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 300;
	private static final int HEIGHT = 100;
	
	private String command;
	private String name;
	
	public Choice(String initName, String initCommand) {
		super(initName);
		this.setBackground(Color.WHITE);
		command = initCommand;
		name = initName;
		setup();
	}
	
	public Choice(String initName, String initCommand, Color color) {
		super(initName);
		this.setBackground(color);
		command = initCommand;
		name = initName;
		setup();
	}
	
	public void setup() {
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 500 / name.length()));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client.console.logInput(command);
				Client.console.putInput();
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int fontSize = (int) (1.4*this.getWidth() / name.length());
		this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getName() {
		return name;
	}
}
