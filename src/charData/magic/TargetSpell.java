package charData.magic;

import Main.Encounter;
import charData.Character;
import world.locations.Arena;

public abstract class TargetSpell extends Spell {
	
	public TargetSpell(Character caster, int lvl, CastingTime time, int spellRange, String words) {
		super(caster, lvl, time, spellRange, words);
	}
	
	protected boolean withinRange(Character target) {
		int xDist = target.getX() - caster.getX();
		int yDist = target.getY() - caster.getY();
		double distance = Math.sqrt(xDist*xDist + yDist*yDist);
		return distance < range;
	}
	
	public abstract void cast(int castLevel, Character target);
	
	public void cast(int castLevel, Character[] targets) {
		for (Character c : targets) {
			cast(castLevel, c);
		}
	}
	
	public abstract int maxTargets(int castLevel);
	
	public void cast(Encounter field, int castLevel) {
		Character[] targets = caster.getTargets(field, maxTargets(castLevel));
		cast(castLevel, targets);
	}

}
