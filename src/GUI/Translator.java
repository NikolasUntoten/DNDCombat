package GUI;

import java.util.HashMap;
import java.util.Map;

import Main.Client;

public class Translator {
	
	public enum Actions {
		ATTACK, MOVE, END_TURN, DRAW, CAST
	}
	
	private static Map<String, Actions> commands;
	
	static {
		commands = new HashMap<>();
		commands.put("attack", Actions.ATTACK);
		commands.put("move", Actions.MOVE);
		commands.put("end turn", Actions.END_TURN);
		commands.put("end", Actions.END_TURN);
		commands.put("stop", Actions.END_TURN);
		commands.put("draw", Actions.DRAW);
		commands.put("cast", Actions.CAST);
	}
	
	public static Actions toAction(String input) {
		input = input.toLowerCase();
		
		if (input.equalsIgnoreCase("help")) {
			help();
			Client.console.log("What would you like to do?", Client.COMPUTER_COLOR);
			return toAction(Client.console.read());
			
		} else if (commandExists(input)) {
			return getCommand(input);
			
		} else {
			Client.console.log("I don't understand what you mean, try again.", Client.DM_COLOR);
			return toAction(Client.console.read());
		}
	}
	
	public static boolean commandExists(String input) {
		return getCommand(input) != null;
	}
	
	public static Actions getCommand(String input) {
		for (String s : commands.keySet()) {
			if (input.startsWith(s)) {
				return commands.get(s);
			}
		}
		return null;
	}
	
	public static void help() {
		Client.console.log("AVAILABLE COMMANDS: ", Client.COMPUTER_COLOR);
		for (String s : commands.keySet()) {
			Client.console.log(s, Client.COMPUTER_COLOR);
		}
	}
	
	public static String clean(String input) {
		if (input.indexOf("\n") >= 0) {
			input = input.substring(0, input.indexOf("\n"));
		}
		return input;
	}
	
	//returns the percentage of matching characters between two strings
	public double percentMatch(String value, String key) {
		int minLength = Math.min(value.length(), key.length());
		int maxLength = Math.max(value.length(), key.length());
		double percentPerChar = 100.0 / minLength;
		double percentCorrect = 0.0;
		for (int i = 0; i < minLength; i++) {
			if (value.charAt(i) == key.charAt(i)) {
				percentCorrect += percentPerChar;
			}
		}
		return percentCorrect;
	}
}
