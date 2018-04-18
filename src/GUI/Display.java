package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GUI.graphicsEngine.DiceRoller;
import actions.AbstractAction;

public class Display extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Console console;
	private ChoiceWindow window;
	private ControlPanel controls;
	private DiceRoller dice;
	private UtilityViewer utilView;
	private boolean running;
	
	public Display() {
		super("COMBAT");
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//this.setUndecorated(true);
		
		setLayout(new BorderLayout());
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addConsole();
		addChoiceWindow();
		addControlPanel();
		utilView = new UtilityViewer();
		add(utilView, BorderLayout.WEST);
		addDiceRoller();
		addUtility(new CharacterView());
		
	}
	
	public Display(int width, int height) {
		super("COMBAT");
		setBounds(0, 0, width, height);
		setLayout(null);
		setVisible(true);
		console = new Console(500, 900);
		add(console);
		window = new ChoiceWindow(150, 500);
		add(window);
		dice = new DiceRoller(650, 0, 200, 200);
		add(dice);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void addUtility(JPanel panel) {
		utilView.add(panel);
	}
	
	public void removeUtility(JPanel panel) {
		utilView.remove(panel);
	}
	
	private void addConsole() {
		console = new Console(500, 900);
		add(console, BorderLayout.CENTER);
	}
	
	private void addChoiceWindow() {
		window = new ChoiceWindow(500, 100);
		add(window, BorderLayout.SOUTH);
	}
	
	private void addControlPanel() {
		controls = new ControlPanel(500, 100);
		add(controls, BorderLayout.NORTH);
	}
	
	private void addDiceRoller() {
		dice = new DiceRoller(650, 0, 200, 200);
		addUtility(dice);
	}
	
	public void start() {
		running = true;
		refresh();
	}
	
	public void stop() {
		running = false;
	}
	
	private void refresh() {
		JFrame f = this;
		new Thread() {
			@Override
			public void run() {
				while (running) {
					f.revalidate();
					f.repaint();
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();		
	}
	
	public int roll20() {
		return dice.roll(20);
	}
	
	public int roll(int n) {
		return dice.roll(n);
	}
	
	public void addChoice(String name, String command) {
		window.addChoice(new Choice(name, command));
	}
	
	public void addChoice(String name, String command, Color c) {
		window.addChoice(new Choice(name, command, c));
	}
	
	public void clearChoices() {
		window.clearChoices();
	}
	
	public void removeChoice(String name) {
		window.removeAction(name);
	}
	
	public Console getConsole() {
		return console;
	}
}
