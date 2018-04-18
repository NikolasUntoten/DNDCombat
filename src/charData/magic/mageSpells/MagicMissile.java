package charData.magic.mageSpells;

import Main.Client;
import charData.Character;
import charData.magic.Spell;
import charData.magic.TargetSpell;

public class MagicMissile extends TargetSpell {

	private static final int MAX_TARGETS = 3;

	public MagicMissile(Character target, int lvl, String words) {
		super(target, lvl, Spell.CastingTime.ACTION, 120, words);
	}

	@Override
	public void cast(int castLevel, Character target) {
		if (withinRange(target)) {
			int damage = 1 + (int) (Math.random() * 4);
			target.damage(damage);
			Client.console.log("The magic missile hit " + target + " for " + damage + " damage!");
		}
	}

	@Override
	public int maxTargets(int castLevel) {
		return MAX_TARGETS;
	}

	public String toString() {
		return "Magic Missile";
	}
}
