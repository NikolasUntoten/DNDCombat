package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Main.Client;

public class Console extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Color DEFAULT_COLOR = Color.WHITE;
	
	private int width = 500;
	private int height = 700;

	
	private String last;
	private JTextArea input;
	private TextWindow log;

	public Console() {
		setup();
	}
	
	public Console(int width, int height) {
		this.width = width;
		this.height = height;
		
		setup();
	}
	
	private void setup() {
		this.setPreferredSize(new Dimension(width, height));
		this.setLayout(new BorderLayout());
		this.setBackground(Color.DARK_GRAY);
		
		log = new TextWindow(500, 500);
		last = "";

		input = new JTextArea();
		input.setEditable(true);
		input.setBackground(Color.LIGHT_GRAY);
		input.setForeground(Color.BLACK);
		
		input.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 20, 20, 20), new LineBorder(Color.BLACK)));
		
		input.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					putInput();
				}	
			}
		});
		add(input, BorderLayout.SOUTH);
		
		add(log, BorderLayout.CENTER);
	}
	
	public void putInput() {
		String text = input.getText();
		input.setText(null);
		text = Translator.clean(text);
		if (text != null && !text.equals("")) {
			
			log("<USER>: \"" + text + "\"", Client.USER_COLOR);
			synchronized (this) {
				this.notifyAll();
			}
			last = text;
		}
	}

	public void log(String text) {
		log(text, DEFAULT_COLOR);
	}
	
	public void log(String text, Color col) {
		log.log(text + "\n", col);
		
	}
	
	public void say(String name, String text, Color col) {
		log("<" + name + ">: \"" + text + "\"", col);
	}
	
	public void logInput(String text) {
		input.setText(text);
	}
	
	/*
	 * reads the next string input into the console
	 */
	public String read() {
		waitForInput();
		return last;
	}
	
	/*
	 * Reads the next number input into the console.
	 * recursively continues asking for a number until a proper number is input.
	 */
	public int readNum() {
		log("Please input a Number", Client.COMPUTER_COLOR);
		waitForInput();
		int num = 0;
		try {
			num = Integer.parseInt(last);
		} catch (NumberFormatException e) {
			return readNum();
		}
		return num;
	}
	
	public boolean readBoolean() {
		waitForInput();
		if (last.equalsIgnoreCase("yes")) {
			return true;
		} else if(last.equalsIgnoreCase("no")) {
			return false;
		} else {
			Client.console.log("I don't understand your input.", Client.COMPUTER_COLOR);
			return readBoolean();
		}
	}
	
	/*
	 * Halts the current thread until a new String is input into the console
	 */
	public void waitForInput() {
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
