package charData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import GUI.Translator;
import Main.Client;
import actions.AbstractAction;
import actions.Attack;
import actions.Cast;
import actions.Draw;
import actions.Move;
import actions.PassTurn;
import world.locations.Arena;

public class Player extends Character {
	
	public AbstractAction getNextAction(Arena field, Queue<Character> others, int moveDist, int actions,
			int bonusActions, int interactions) {
		
		Map<Translator.Actions, Boolean> available = new HashMap<Translator.Actions, Boolean>();
		available.put(Translator.Actions.ATTACK, actions > 0);
		available.put(Translator.Actions.MOVE, moveDist > 0);
		available.put(Translator.Actions.DRAW, interactions > 0);
		// add cast time to check?
		available.put(Translator.Actions.CAST, data.magic.getSpells().length > 0); 
		
		if (available.get(Translator.Actions.ATTACK))
			Client.display.addChoice("Attack", "attack");

		if (available.get(Translator.Actions.MOVE))
			Client.display.addChoice("Move", "move");

		if (available.get(Translator.Actions.DRAW))
			Client.display.addChoice("Draw", "draw");

		if (available.get(Translator.Actions.CAST))
			Client.display.addChoice("Cast Spell", "cast");

		Client.display.addChoice("End Turn", "end turn");

		String input = Client.console.read();

		Translator.Actions choice = Translator.toAction(input);
		Client.display.clearChoices();

		switch (choice) {
		case ATTACK:
			if (!available.get(Translator.Actions.ATTACK)) {
				Client.console.log("No more actions available!");
				return null;
			}
			return new Attack(this, others.peek());
		case MOVE:
			if (!available.get(Translator.Actions.MOVE)) {
				Client.console.log("No more movement allowed!");
				return null;
			}
			return new Move(field, this, moveDist, input);
		case END_TURN:
			return new PassTurn();
		case DRAW:
			return new Draw(this);
		case CAST:
			return new Cast(this, field);
		default:
			return null;
		}
	}

	public Character[] getTargets(Arena field, int max) {
		Character[] targets = new Character[max];
		List<Character> people = field.getPeople();
		for (int i = 0; i < people.size(); i++) {
			Client.console.log(i + 1 + ": " + people.get(i), Client.DM_COLOR);
		}
		for (int i = 0; i < max; i++) {
			Client.console.log("Who would you like your next target to be?");
			int n = Client.console.readNum();
			targets[i] = people.get(n - 1);
		}
		return targets;
	}
}
