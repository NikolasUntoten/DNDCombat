package charData.items;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import Main.Client;

public class Weapon extends Item {
	public int reach;
	private int rolls;
	private int sides;
	private Set<Property> properties;

	public enum Property {
		AMMUNITION, FINESSE, HEAVY, LIGHT, LOADING, RANGE, REACH, 
		SPECIAL, THROWN, TWO_HANDED, VERSATILE, IMPROVISED, SILVERED
	}
	
	private static HashMap<String, Property> propertyMap;
	
	static {
		propertyMap = new HashMap<String, Property>();
		propertyMap.put("ammunition", Property.AMMUNITION);
		propertyMap.put("finesse", Property.FINESSE);
		propertyMap.put("heavy", Property.HEAVY);
		propertyMap.put("light", Property.LIGHT);
		propertyMap.put("loading", Property.LOADING);
		propertyMap.put("range", Property.RANGE);
		propertyMap.put("reach", Property.REACH);
		propertyMap.put("special", Property.SPECIAL);
		propertyMap.put("thrown", Property.THROWN);
		propertyMap.put("two-handed", Property.TWO_HANDED);
		propertyMap.put("versatile", Property.VERSATILE);
		propertyMap.put("improvised", Property.IMPROVISED);
		propertyMap.put("silvered", Property.SILVERED);
	}

	public Weapon(String name, int initValue,  double initWeight, 
			int initReach, int initRolls, int initSides, Property[] props) {
		super(name, initValue, initWeight);
		reach = initReach;
		rolls = initRolls;
		sides = initSides;
		properties = new HashSet<Property>();
		for (Property p : props) {
			properties.add(p);
		}
	}

	public Weapon(int initWeight, int initValue) {
		super("Generic Weapon", initValue, initWeight);
		reach = 1;
		rolls = 1;
		sides = 4;
		properties = new HashSet<Property>();
	}

	public int getDamage() {
		int sum = 0;
		for (int i = 0; i < rolls; i++) {
			sum += Client.roll(sides, true);
		}
		return sum;
	}
	
	public boolean hasProperty(Property p) {
		
		return properties.contains(p);
	}

	public void setDamage(int numRolls, int numSides) {
		rolls = numRolls;
		sides = numSides;
	}

	public void setReach(int r) {
		reach = r;
	}

	public static Weapon getWeapon(String name) throws FileNotFoundException {
		File f = new File("\\items\\Weapons.txt");
		Scanner s = new Scanner(f);
		s.nextLine(); // throw away format line
		while (s.hasNextLine()) {
			String input = s.nextLine();
			if (input.startsWith(name)) {
				s.close();
				return makeWeapon(input);
			}
		}
		s.close();
		return null;
	}

	public static Weapon[] getWeapons() throws FileNotFoundException {
		ArrayList<Weapon> weapons = new ArrayList<Weapon>();

		String name = "items/Weapons.txt";
		// gets the inputstream from the filename, contained in the jar file.
		InputStream in = Weapon.class.getClassLoader().getResourceAsStream(name);
		Scanner s = new Scanner(in);

		s.nextLine(); // throw away format line
		while (s.hasNextLine()) {
			String input = s.nextLine();
			weapons.add(makeWeapon(input));
		}
		Weapon[] arr = weapons.toArray(new Weapon[0]);
		s.close();
		return arr;
	}

	// Format: <name>|<range>|<rolls>|<sides>|<weight(lbs)>|<value(copper)>
	public static Weapon makeWeapon(String data) {
		String[] arr = data.split("[|]");
		if (arr.length < 6) throw new IllegalArgumentException();
		
		String name = arr[0];
		int value = Integer.parseInt(arr[1]);
		double weight = Double.parseDouble(arr[2]);
		
		int range = Integer.parseInt(arr[3]);
		int rolls = Integer.parseInt(arr[4]);
		int sides = Integer.parseInt(arr[5]);
		
		Property[] props;
		if (arr.length == 7) {
			String[] propArr = arr[6].split(" ");
			props = findProps(propArr);
		} else {
			props = new Property[0];
		}
		
		return new Weapon(name, value, weight, range, rolls, sides, props);
	}
	
	
	private static Property[] findProps(String[] input) {
		Property[] props = new Property[input.length];
		for (int i = 0; i < input.length; i++) {
			if (propertyMap.containsKey(input[i])) {
				props[i] = propertyMap.get(input[i]);
			} else {
				props[i] = null;
			}
		}
		return props;
	}
}
