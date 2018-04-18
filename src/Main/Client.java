package Main;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import GUI.CharacterView;
import GUI.Console;
import GUI.Display;
import charData.Character;
import world.Hub;
import world.World;

public class Client {
	
	public static final Color DM_COLOR = new Color(5, 255, 213);
	public static final Color COMPUTER_COLOR = new Color(5, 255, 96);
	public static final Color USER_COLOR = new Color(246, 255, 5);

	public static Console console;
	public static Display display;
	public static Hub hub;

	public static void main(String[] args) {
		display = new Display();
		console = display.getConsole();
		display.start();
		
		hub = new Hub();
		
		console.log("Would you like to load a previous save?", COMPUTER_COLOR);
		boolean load = console.readBoolean();
		if (load) {
			load();
		} else {
			World.initialize();
			hub.initialize();
		}
		
		hub.changeLocation(World.cityCenter);
		hub.simulateLocation();
	}
	
	public static void autoSave() {
		World.save("autosave");
		hub.save("autosave");
	}
	
	public static void deleteAutoSave() {
		String location = System.getProperty("user.dir");
		File f = new File(location + "/save/autosave");
		if (f.exists()) {
			f.delete();
		}
	}
	
	public static void save() {
		Client.console.log("What would you like to name your save?", COMPUTER_COLOR);
		String saveName = Client.console.read();
		deleteAutoSave();
		boolean a = World.save(saveName);
		boolean b = hub.save(saveName);
		if (a && b) {
			Client.console.log("World successfully saved.", COMPUTER_COLOR);
		} else {
			Client.console.log("Oh Darnit. Something didn't save right. Please try again later!", COMPUTER_COLOR);
		}
	}
	
	public static void load() {
		String location = System.getProperty("user.dir");
		File f = new File(location + "/save/");
		String[] files = f.list();
		boolean a = true;
		boolean b = true;
		
		if (files == null) {
			Client.console.log("There are no saves to load. Generating new world.", COMPUTER_COLOR);
			World.initialize();
			hub.initialize();
			Client.console.log("World generated. Let the adventure begin!", COMPUTER_COLOR);	
			
		} else if (files.length == 1) {
			a = World.load(files[0]);
			b = hub.load(files[0]);
			
		} else {
			int num = getArrayEntry(files);
			String name = files[num];
			a = World.load(name);
			b = hub.load(name);
		}
		
		if (a && b) {
			Client.console.log("World successfully loaded. Let the adventure continue!", COMPUTER_COLOR);
		} else {
			Client.console.log("uh-oh, something went wrong! Generating new world.", COMPUTER_COLOR);
			World.initialize();
			hub.initialize();
			Client.console.log("World generated. Let the adventure begin!", COMPUTER_COLOR);
		}
	}
	
	public static void saveObject(Object object, String saveName, String name) throws IOException {
		String location = System.getProperty("user.dir");
		File f = new File(location + "/save/" + saveName + "/" + name);
		f.getParentFile().mkdirs();
		FileOutputStream fileOutput = new FileOutputStream(f);
		ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
		objectOutput.writeObject(object);
		objectOutput.close();
		fileOutput.close();
		System.out.println("Object " + name + " saved.");
	}
	
	public static Object loadObject(String saveName, String name) throws ClassNotFoundException {
		try {
			String location = System.getProperty("user.dir");
			FileInputStream fileInput = new FileInputStream(location + "/save/" + saveName + "/" + name);
			ObjectInputStream objectInput = new ObjectInputStream(fileInput);
			Object object = objectInput.readObject();
			objectInput.close();
			fileInput.close();
			System.out.println("Object " + name + " loaded.");
			return object;
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getArrayEntry(Object[] array) {
		for (int i = 0; i < array.length; i++) {
			console.log((i+1) + ": " + array[i], COMPUTER_COLOR);
		}
		int num = console.readNum() - 1;
		if (num < 0 || num >= array.length) {
			console.log("Improper input, please try again.", COMPUTER_COLOR);
			return getArrayEntry(array);
		}
		return num;
	}
	
	public static int roll(int num, boolean quick) {
		if (quick) {
			return (int) (Math.random() * num) + 1;
		}
		int result = display.roll(num);
		Client.console.log("You rolled a " + result + "!", COMPUTER_COLOR);
		return result;
	}
}
