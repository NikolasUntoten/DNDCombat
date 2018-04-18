package charData.magic;

import java.awt.Point;
import charData.Character;
import world.locations.Arena;

public abstract class AOESpell extends Spell {
	
	private static AOE areaType;
	
	public enum AOE {
		CONE, CUBE, CYLINDER, LINE, SPHERE
	}

	public AOESpell(Character caster, int lvl, CastingTime time, int spellRange, String words) {
		super(caster, lvl, time, spellRange, words);
	}
	
	public abstract Point getOrigin();
	
	public abstract void cast(Arena field, Point origin, int castLevel);
	
	public void cast(Arena field, int castLevel) {
		cast(field, getOrigin(), castLevel);
	}
}
