package actions;

import java.awt.Image;

import Main.Client;
import Main.Encounter;
import charData.magic.Spell;
import world.locations.Arena;
import charData.Character;

public class Cast implements AbstractAction {
	
	private Character caster;
	private Encounter field;
	
	public Cast(Character c, Encounter f) {
		caster = c;
		this.field = f;
	}

	@Override
	public void performAction() {
		Spell[] spells = caster.getSpells();
		Client.console.log("Available spells:", Client.COMPUTER_COLOR);
		for (int i = 0; i < spells.length; i++) {
			Client.console.log(i+1 + ": " + spells[i], Client.COMPUTER_COLOR);
		}
		Client.console.log("Which spell would you like to use?", Client.DM_COLOR);
		int n = Client.console.readNum();
		Spell spell = spells[n - 1];
		spell.cast(field);
	}

}
