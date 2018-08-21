package charData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import charData.magic.Spell;

public class MagicalInventory implements Serializable {
	
	public static final int MAX_SPELL_LEVEL= 9;
	
	private int[] spellSlots;
	private int[] spellSlotsMax;
	private List<Spell> spells;
	
	public MagicalInventory() {
		spellSlots = new int[MAX_SPELL_LEVEL];
		spellSlotsMax = new int[MAX_SPELL_LEVEL];
		spells = new ArrayList<Spell>();
	}
	
	public void learnSpell(Spell s) {
		spells.add(s);
	}
	
	public Spell[] getSpells() {
		Spell[] arr = new Spell[spells.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = spells.get(i);
		}
		return arr;
	}
	
	public int getRemainingSlots(int level) {
		if (level < 0 || level >= spellSlots.length) {
			throw new IllegalArgumentException();
		}
		return spellSlots[level];
	}
	
	public boolean useSpellSlot(int level) {
		if (getRemainingSlots(level) > 0) {
			spellSlots[level]--;
			return true;
		}
		return false;
	}
	
	public void setSpellSlots(CharacterData.Race race, CharacterData.Class clss, int level) {
		
		//set spell slots here
		
		for (int i = 0; i < spellSlots.length; i++) {
			spellSlotsMax[i] = spellSlots[i];
		}
	}
}
