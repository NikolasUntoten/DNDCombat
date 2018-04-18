package charData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Stats implements Serializable {
	
	public int speed;
	public String name;
	public Race race;
	public Class clss;
	public Style style;
	public Align alignment;
	
	public int str;
	public int dex;
	public int con;
	public int intl;
	public int wis;
	public int cha;
	
	public int passivePerc;
	
	public int exp;
	public int savingSuccesses;
	public int savingFailures;
	public int inspiration;
	public int proficiencyBonus;
	
	public int AC;
	private int maxHitpoints;
	public int hitpoints;
	public int tempHitpoints;
	public int level;
	
	public List<Proficiency> proficiencies;
	
	public enum Race {
		DWARF, ELF, HALFLING, HUMAN, DRAGONBORN, GNOME, 
		HALF_ELF, HALF_ORC, TEIFLING
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
	
	public Stats() {
		speed = 6;
		name = "null";
		race = Race.HUMAN;
		style = Style.NEUTRAL;
		alignment = Align.NEUTRAL;
		
		str = 0;
		dex = 0;
		con = 0;
		intl = 0;
		wis = 0;
		cha = 0;
		
		passivePerc = 0;
		
		exp = 0;
		savingSuccesses = 0;
		savingFailures = 0;
		inspiration = 0;
		proficiencyBonus = 0;
		AC = 0;
		setHealth(0);
		level = 0;
		
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
