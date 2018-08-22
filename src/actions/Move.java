package actions;

import charData.Character;
import world.locations.Arena;

import java.awt.Point;

import Main.Client;
import Main.Encounter;

public class Move implements AbstractAction {

	Character focus;
	private int deltaX;
	private int deltaY;
	private int distanceLeft;
	private int initDist;
	private String input;
	Encounter field;

	public Move(Encounter e, Character c, int dist, String input) {
		focus = c;
		deltaX = 0;
		deltaY = 0;
		distanceLeft = dist;
		initDist = dist;
		this.input = input;
		field = e;
	}

	@Override
	public void performAction() {
		String[] parts = input.split(" ");

		if (parts.length == 1) { //case "move"
			Client.console.log("Please either click on where you would like to move to, or manually enter distances.", Client.COMPUTER_COLOR);
			new Thread() {
				@Override
				public void run() {
					Point temp = field.getSelectedPoint();
					Client.console.logInput((temp.x - focus.getX()) + " " + (temp.y - focus.getY()));
					Client.console.putInput();
				}
			}.start();
			Client.console.log("Distances should be input as '<x> <y>'", Client.COMPUTER_COLOR);
			input = Client.console.read();
			parts = input.split(" ");
			directMove(parts[0], parts[1]);
			
		} else {
			if (parts[1].equals("by") || parts[1].equals("to")) { // case "move by # #"
				while (parts.length < 4) {
					Client.console.log("Improper input, Please re-enter in proper format.", Client.COMPUTER_COLOR);
					Client.console.log("Proper format is: 'move by <x> <y>'", Client.COMPUTER_COLOR);
					input = Client.console.read();
					parts = input.split(" ");
				}
				directMove(parts[2], parts[3]);

			} else { // case "move east #"
				Client.console.log("I don't understand what you want to do. Please use the format 'move by <x> <y>'", Client.COMPUTER_COLOR);
				input = Client.console.read();
				performAction();
			}
		}
		Client.console.log("Final position: " + focus.getX() + ", " + focus.getY(), Client.COMPUTER_COLOR);
	}
	
	private void directMove(String x, String y) {
		try {
			deltaX = Integer.parseInt(x);
		} catch (NumberFormatException e) {
			Client.console.log("Improper number format. What x distance would you like to input?", Client.COMPUTER_COLOR);
			deltaX = Client.console.readNum();
			return;
		}

		// catches invalid y input
		try {
			deltaY = Integer.parseInt(y);
		} catch (NumberFormatException e) {
			Client.console.log("Improper number format. What y distance would you like to input?", Client.COMPUTER_COLOR);
			deltaY = Client.console.readNum();
			return;
		}
		
		directMove();
	}

	private void directMove() {
		
		while (distanceLeft > 0 && (deltaX != 0 || deltaY != 0)) {
			
			if (deltaX != 0) {
				int value = (int) Math.signum(deltaX);
				move(value, 0);
				deltaX -= 1 * value;
				distanceLeft--;
			}
			
			if (deltaY != 0 && distanceLeft > 0) {
				int value = (int) Math.signum(deltaY);
				move(0, value);
				deltaY -= 1 * value;
				distanceLeft--;
			}
		}

		// tells the user they hit the max.
		if (distanceLeft == 0) {
			Client.console.log("You have moved the maximum amount.", Client.COMPUTER_COLOR);
		}
	}

	public int getDistance() {
		return initDist - distanceLeft;
	}

	private void move(int deltaX, int deltaY) {
		if (!field.occupied(focus.getX() + deltaX, focus.getY() + deltaY)) {
			focus.move(deltaX, deltaY);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public String toString() {
		return "move";
	}
}
