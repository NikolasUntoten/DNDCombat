package world;


import java.io.IOException;
import java.io.Serializable;

import Main.CharacterCreator;
import Main.Client;
import charData.Character;

public class Hub implements Serializable {
	
	private Character person;
	private Location location;
	private boolean simulating;
	
	public Hub() {
		
	}
	
	public void initialize() {
		person = CharacterCreator.create();
		location = World.cityCenter;
	}
	
	public void changeLocation(Location newLoc) {
		location.exit();
		location = newLoc;
		location.enter();
	}
	
	public void simulateLocation() {
		location.describe();
		simulating = true;
		while (simulating) {
			Client.console.log("You are in " + location + ". What would you like to do?", Client.DM_COLOR);
			String input = Client.console.read();
			doThing(input);
		}
	}
	
	public void stopSimulation() {
		simulating = false;
	}
	
	private void doThing(String act) {
		if (person.hasAct(act)) {
			person.act(act);
		} else if (location.hasAct(act)) {
			location.act(act);
		} else if (act.equals("help")) {
			help();
		} else {
			Client.console.log("I don't know what you want to do.", Client.DM_COLOR);
		}
	}
	
	public void help() {
		Client.console.log("Available actions: ", Client.COMPUTER_COLOR);
		
		String[] strings = person.getActs();
		for (String s : strings) {
			Client.console.log(s, Client.COMPUTER_COLOR);
		}
		
		String[] strings2 = location.getActs();
		for (String s : strings2) {
			Client.console.log(s, Client.COMPUTER_COLOR);
		}
	}
	
	public boolean load(String saveName) {
		try {
			person = (Character) Client.loadObject(saveName, "Person");
			location = (Location) Client.loadObject(saveName, "Location");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		if (person == null) person = CharacterCreator.create();
		if (location == null) location = World.cityCenter;
		return true;
	}
	
	public boolean save(String saveName) {
		try {
			Client.saveObject(person, saveName, "Person");
			Client.saveObject(location, saveName, "Location");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public Character getPerson() {
		return person;
	}
}
