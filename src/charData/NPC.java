package charData;

import java.util.List;
import java.util.Queue;

import Main.Client;
import actions.AbstractAction;
import actions.Attack;
import actions.Move;
import actions.PassTurn;
import world.locations.Arena;

public class NPC extends Character {
	public AbstractAction getNextAction(Arena field, Queue<Character> others, int moveDist, int actions, int bonusActions,
			int interactions) {
		Character enemy = others.peek();
		if (enemy.distance(this) > this.getReach() && moveDist > 0) {
			int deltaX = enemy.getX() - getX();
			int deltaY = enemy.getY() - getY();
			return new Move(field, this, moveDist, "move by " + deltaX + " " + deltaY);
		}
		if (actions > 0) {
			return new Attack(this, others.peek());
		} else {
			return new PassTurn();
		}
	}
	
	public Character[] getTargets(Arena field, int max) {
		Character[] targets = new Character[max];
		List<Character> people = field.getPeople();
		for (int i = 0; i < max; i++) {
			int n = (int) (Math.random() * people.size());
			targets[i] = people.get(n);
		}
		return targets;
	}
}
