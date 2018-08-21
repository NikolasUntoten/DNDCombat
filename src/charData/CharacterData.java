package charData;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import charData.items.Armor;
import charData.items.Fists;
import charData.items.Shield;
import charData.items.Weapon;

public class CharacterData implements Serializable {
	
	public String name;
	
	/* Preliminary information
	 * 
	 * This data is mostly used for window dressing, such as saying the name of the player,
	 * referencing the player's background, or their alignment.
	 */
	public Class clss;
	public int level;
	public Background background;
	public String playerName;
	public Race race;
	public Style style;
	public Align alignment;
	public int exp;
	
	//Basic stats
	public int str;
	public int dex;
	public int con;
	public int intl;
	public int wis;
	public int cha;
	
	public int passivePerception;
	
	public int inspiration;
	public int proficiencyBonus;
	
	//TODO saving throws
	
	/* Skills
	 * Skills, also known as proficiencies, are used when an action can make use of a specific talent.
	 * On the use of a talent which the character has proficiency for, the proficiency modifier is added
	 * to the player's roll.
	 */
	public List<Proficiency> proficiencies;
	
	/* Combat Stats
	 * These stats are used to determine current combat state
	 */
	public int AC;
	public int initiative;
	public int speed;
	public int hitpoints;
	private int maxHitpoints;
	public int tempHitpoints;
	public int hitdice;
	public int savingSuccesses;
	public int savingFailures;
	public boolean unconcious;
	
	/* Equipment
	 * Used for determining Attack power, and Armor class.
	 */
	public List<Weapon> weapons;
	protected Weapon drawnWeapon;
	public Armor armor;
	public Shield shield;
	
	/* Inventory
	 * List of all stowed items
	 */
	public Inventory inventory;
	protected int copper;
	
	public MagicalInventory magic;
	
	/*
	 * Attributes about the character, used as window dressing.
	 * These will likely be used for nothing other than display,
	 * as doing something with them would mean making a natural language processor.
	 */
	public String personalityTraits;
	public String ideals;
	public String bonds;
	public String flaws;
	public String featuresAndTraits;
	public int age;
	public int height; //IN CM
	public int weight;
	public Color eyes;
	public Color skin;
	public Color hairColor;
	public String hairstyle;
	public String characterAppearence;
	public String backstory;
	public List<String> alliesAndOrganizations;
	public String treasure;
	
	public enum Race {
		DWARF, ELF, HALFLING, HUMAN, DRAGONBORN, GNOME, 
		HALF_ELF, HALF_ORC, TEIFLING
	}
	
	public enum Background {
		
	}
	
	public enum CreatureSize {
		TINY, SMALL, MEDIUM, LARGE, HUGE, GARGANTUAN
	}
	
	public enum Class {
		BARBARIAN, BARD, CLERIC, DRUID, FIGHTER, MONK, 
		PALADIN, RANGER, ROGUE, SORCERER, WARLOCK, WIZARD
	}
	
	public enum Style {
		ORDERED, NEUTRAL, CHAOTIC
	}
	
	public enum Align {
		GOOD, NEUTRAL, EVIL
	}
	
	public enum Proficiency {
		ACROBATICS, ANIMAL_HANDLING, ARCANA, ATHLETICS, DECEPTION, HISTORY, INSIGHT, INTIMIDATION, 
		INVESTIGATION, MEDICINE, NATURE, PERCEPTION, PERFORMANCE, PERSUASION, RELIGION, SLIEGHT_OF_HAND, 
		STEALTH, SURVIVAL
	}
	
	public CharacterData() {
		speed = 6;
		name = "null";
		race = Race.HUMAN;
		style = Style.NEUTRAL;
		alignment = Align.NEUTRAL;
		drawnWeapon = new Fists();
		inventory = new Inventory(150);
		magic = new MagicalInventory();
		
		proficiencies = new ArrayList<Proficiency>();
	}
	
	public int getMaxHitpoints() {
		return maxHitpoints;
	}
	
	public void setMaxHitpoints(int deltaHitpoints) {
		maxHitpoints += deltaHitpoints;
	}

	public void setBasic(int str2, int dex2, int con2, int intl2, int wis2, int cha2) {
		str = str2;
		dex = dex2;
		con = con2;
		intl = intl2;
		wis = wis2;
		cha = cha2;
	}

	public void setHealth(int hp) {
		setMaxHitpoints(hp);
		hitpoints = maxHitpoints;
		tempHitpoints = 0;
	}
}
