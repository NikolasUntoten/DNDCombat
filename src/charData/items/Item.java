package charData.items;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Item implements Serializable {
	private double weight;
	private int value;
	private String name;
	
	public Item(String name, int initValue, double initWeight) {
		weight = initWeight;
		value = initValue;
		this.name = name;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public int getValue() {
		return value;
	}
	
	public static Item getItem(String name) throws FileNotFoundException {
		File f = new File("\\items\\Generic.txt");
		Scanner s = new Scanner(f);
		s.nextLine(); // throw away format line
		while (s.hasNextLine()) {
			String input = s.nextLine();
			if (input.startsWith(name)) {
				s.close();
				return makeItem(input);
			}
		}
		s.close();
		return null;
	}

	public static Item[] getItems() throws FileNotFoundException {
		ArrayList<Item> items = new ArrayList<Item>();

		String name = "items/Weapons.txt";
		// gets the inputstream from the filename, contained in the jar file.
		InputStream in = Weapon.class.getClassLoader().getResourceAsStream(name);
		Scanner s = new Scanner(in);

		s.nextLine(); // throw away format line
		while (s.hasNextLine()) {
			String input = s.nextLine();
			items.add(makeItem(input));
		}
		
		Item[] arr = items.toArray(new Item[0]);
		s.close();
		return arr;
	}

	// Format: <name>|<range>|<rolls>|<sides>|<weight(lbs)>|<value(copper)>
	public static Item makeItem(String data) {
		String[] arr = data.split("[|]");
		if (arr.length < 3) throw new IllegalArgumentException();
		
		String name = arr[0];
		int value = Integer.parseInt(arr[1]);
		double weight = Double.parseDouble(arr[2]);
		
		return new Item(name, value, weight);
	}
	
	public String toString() {
		return name;
	}
}
