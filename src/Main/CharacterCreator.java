package Main;

import charData.Character;
import charData.CharacterData;
import charData.Player;
import charData.magic.Spell;
import charData.magic.mageSpells.MagicMissile;

public class CharacterCreator {

	public static Character create() {
		CharacterData cd = new CharacterData();
		Character c = new Player();

		Client.console.log("Would you like to create your own character?", Client.DM_COLOR);
		if (!Client.console.readBoolean()) {
			return defaultChar(false);
		}

		Client.console.log("What is your name?", Client.DM_COLOR);
		String name = Client.console.read();

		Client.console.log("Hello, " + name + ".", Client.DM_COLOR);
		Client.console.log("Now we can start making your character. What will your stats be?", Client.DM_COLOR);
		int[] statList = getStats();
		
		CharacterData.Race race = askRace();
		CharacterData.Class clss = askClass();
		
		if (clss == CharacterData.Class.WIZARD) {
			c.learnSpell(findCantrip(c));
		}
		cd.str = statList[0];
		cd.dex = statList[1];
		cd.con = statList[2];
		cd.intl = statList[3];
		cd.wis = statList[4];
		cd.cha = statList[5];
		
		cd.speed = 30;
		cd.clss = clss;
		cd.race = race;
		cd.alignment = CharacterData.Align.NEUTRAL;
		cd.style = CharacterData.Style.NEUTRAL;
		cd.name = name;
		
		c.setStats(cd);
		c.addCopper(1000);
		return c;
	}

	// returns a default character for you lazy people (me)
	public static Character defaultChar(boolean npc) {
		Character c = new Player();
		c.setStats(12, 15, 10, 11, 13, 11, 30, CharacterData.Class.FIGHTER, CharacterData.Race.HUMAN, CharacterData.Align.NEUTRAL, CharacterData.Style.NEUTRAL,
				generateName());
		c.addCopper(300);
		return c;
	}
	
	public static String generateName() {
		String[] letters = {"b", "c", "d", "f", "g", "k", "l", "n", "m", "s", "r", "t", "v"};
		
		String[] parts = {"An", "Ara", "Bra", "Ca", "Co", "Cu", "Da", "Day", "Do", "Du", "En", 
				"Fa", "Fe", "Ha", "Je", "La", "Le", "Ma", "Me", "Na", "Ne", "Ni", 
				"No", "Se", "Ste", "Ta", "Te", "Va"};
		
		String[] ends = {"low", "ando", "m", "le", "d", "garth", "n", 
				"dro", "len", "ken", "ck", "k", "ton", "ron", "gar", 
				"ras", "nt", "an", "mil", "tin", "ris", "nan", "fen", "lus", "lf", "mel"};
		
		String name = "";
		int len = findNameLength();
		for (int i = 0; i < len; i++) {
			int num = (int) (Math.random() * parts.length);
			if (i == 0) {
				name += parts[num];
			} else if (i == len - 1) {
				name += ends[num % ends.length];
			} else {
				name += parts[num].toLowerCase();
			}
		}
		if (len == 1) {
			name += letters[(int) (Math.random() * letters.length)];
		}
		return name;
	}
	
	private static int findNameLength() {
		int[] chances = {50, 500, 200, 50, 30, 5};
		
		int sum = 0;
		for (int i : chances) sum += i;
		int rand = (int) (Math.random() * sum);
		
		int tempSum = 0;
		for (int i = 0; i < chances.length; i++) {
			tempSum += chances[i];
			if (rand < tempSum) return i + 1;
		}
		return 1;
	}
	
	//get the user's preferred race
	private static CharacterData.Race askRace() {
		CharacterData.Race[] races = {CharacterData.Race.DWARF, CharacterData.Race.ELF, 
				CharacterData.Race.HALFLING, CharacterData.Race.HUMAN, CharacterData.Race.DRAGONBORN,
				 CharacterData.Race.GNOME, CharacterData.Race.HALF_ELF, CharacterData.Race.HALF_ORC,
				 CharacterData.Race.TEIFLING};
		
		Client.console.log("What race would you like your character to be?", Client.DM_COLOR);
		for (int i = 0; i < races.length; i++) {
			Client.console.log("#" + (i+1) + ": " + races[i], Client.COMPUTER_COLOR);
		}
		
		int index = Client.console.readNum();
		index--;
		if (index < 0 || index >= races.length) return CharacterData.Race.HUMAN;
		
		return races[index];
	}
	
	//get the user's preferred class
	private static CharacterData.Class askClass() {
		CharacterData.Class[] classes = {CharacterData.Class.BARBARIAN, CharacterData.Class.BARD,
				CharacterData.Class.CLERIC, CharacterData.Class.DRUID, CharacterData.Class.FIGHTER,
				CharacterData.Class.MONK, CharacterData.Class.PALADIN, CharacterData.Class.RANGER,
				CharacterData.Class.ROGUE, CharacterData.Class.SORCERER, CharacterData.Class.WARLOCK,
				CharacterData.Class.WIZARD};
		
		Client.console.log("What class would you like your character to be?", Client.DM_COLOR);
		for (int i = 0; i < classes.length; i++) {
			Client.console.log("#" + (i+1) + ": " + classes[i], Client.COMPUTER_COLOR);
		}
		
		int index = Client.console.readNum();
		index--;
		if (index < 0 || index >= classes.length) return CharacterData.Class.FIGHTER;
		
		return classes[index];
	}
	
	//TODO fix this
	private static Spell findCantrip(Character c) {
		Client.console.log("What would you like your cantrip to be?", Client.DM_COLOR);
		String input = Client.console.read();
		Client.console.log("fuk u, u get a magic missile ya nerd!");
		return new MagicMissile(c, 0, "Shooty McMagicMissile");
	}

	// determine the user's stats
	private static int[] getStats() {
		// set the stat rolls
		int[] rolls = new int[6];
		for (int i = 0; i < rolls.length; i++) {
			rolls[i] = statCreationRoll();
		}

		// find what stats the user wants for each roll.
		int[] statList = new int[6];
		String[] stats = { "strength", "dexterity", "constitution", "intelligence", "wisdom", "charisma" };
		for (int i = 0; i < 6; i++) {
			int index = nextStat(rolls, stats[i], true);
			statList[i] = rolls[index];
			rolls = remove(rolls, index);
		}
		return statList;
	}

	// returns the index of the roll the user would like for a given stat.
	private static int nextStat(int[] arr, String stat, boolean showFull) {
		if (showFull) {
			Client.console.log("Please input the number of the roll you would like for " + stat, Client.COMPUTER_COLOR);
			for (int i = 0; i < arr.length; i++) {
				Client.console.log("#" + (i + 1) + ": " + arr[i], Client.COMPUTER_COLOR);
			}
		}

		int index = Client.console.readNum() - 1;

		if (index < 0 || index >= arr.length) {
			Client.console.log("Number is not on list.", Client.COMPUTER_COLOR);
			return nextStat(arr, stat, false);
		}

		return index;
	}

	// removes an index from the given array
	private static int[] remove(int[] arr, int index) {
		int[] fixed = new int[arr.length - 1];
		for (int i = 0; i < index; i++) {
			fixed[i] = arr[i];
		}

		for (int i = index + 1; i < arr.length; i++) {
			fixed[i - 1] = arr[i];
		}
		return fixed;
	}

	// rolls 4 six sided die and takes the highest 3, returns cumulative total.
	private static int statCreationRoll() {
		int[] tempSet = new int[4];
		int total = 0;
		int smallest = 7;
		for (int j = 0; j < 4; j++) {
			tempSet[j] = Client.roll(6, true);
			total += tempSet[j];
			if (tempSet[j] < smallest)
				smallest = tempSet[j];
		}
		total -= smallest;
		return total;
	}
}
