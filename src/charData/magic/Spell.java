package charData.magic;

import charData.items.Item;
import world.locations.Arena;

import java.io.Serializable;

import Main.Client;
import charData.Character;

public abstract class Spell implements Serializable {
	
	protected static int level;
	protected static CastingTime castingTime;
	protected static int longCastTime; //only used if spell is LONG_CAST CastingTime
	protected static Duration duration;
	protected static int longDurationTime; //only used if spell is LONG_DURATION Duration.
	protected static int range;
	protected static String spellWords;
	protected static boolean verbal;
	protected static boolean somatic;
	protected static Item[] materials;
	
	protected Character caster;
	
	public enum CastingTime {
		ACTION, REACTION, BONUS, LONG_CAST
	}
	
	public enum Duration {
		INSTANTANEOUS, CONCENTRATION, LONG_DURATION
	}
	
	public Spell(Character caster, int lvl, CastingTime time, int spellRange, String words) {
		this.caster = caster;
		level = lvl;
		castingTime = time;
		range = spellRange;
		spellWords = words;
	}
	
	public void setLongCastTime(int time) {
		longCastTime = time;
	}
	
	public abstract void cast(Arena field, int castLevel);
	
	public void cast(Arena field) {
		Client.console.log("What level would you like to cast at? (minimum: " + level + ")", Client.DM_COLOR);
		int castLevel = Client.console.readNum();
		if (castLevel < level) {
			Client.console.log("You cannot cast this spell at that level!");
			cast(field);
			return;
		}
		if (caster.data.magic.getRemainingSlots(castLevel) < 0) {
			Client.console.log("You have no spell slots available at that level!");
			cast(field);
			return;
		}
		caster.data.magic.useSpellSlot(castLevel);
		cast(field, castLevel);
	}
}
