package Main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import GUI.BattleFieldViewer;
import actions.AbstractAction;
import actions.Attack;
import actions.Cast;
import actions.Draw;
import actions.Move;
import actions.PassTurn;
import charData.Character;
import charData.NPC;
import world.roomStuffs.Room;

public class Encounter {
	
	private BattleFieldViewer fieldView;
	
	private List<Character> combatants;
	
	private Queue<Character> battleQueue;
	
	private Room room;
	
	public Encounter(Room initRoom, List<Character> initCombatants) {
		combatants = initCombatants;
		room = initRoom;
	}
	
	public void run() {
		fieldView = new BattleFieldViewer(room, 200);
		Client.display.addUtility(fieldView);

		// sort the combatants based on initiative
		if (combatants.size() > 1) {
			combatants.sort(new Comparator<Character>() {
				@Override
				public int compare(Character c1, Character c2) {
					return (int) Math.ceil(c1.getInitiative() - c2.getInitiative());
				}
			});
		}

		battleQueue = new LinkedList<Character>();
		for (Character c : combatants) {
			battleQueue.add(c);
		}
		while (battleQueue.size() > 1) {
			Character current = battleQueue.poll();
			Client.console.log("Beginning turn for: " + current + " (NPC:" + (current instanceof NPC) + ").", Client.DM_COLOR);
			takeTurn(current);
			battleQueue.add(current);
		}

		Client.console.log(battleQueue.poll() + " has won the battle!", Client.DM_COLOR);
		Client.display.remove(fieldView);
	}
	
	private void takeTurn(Character focus) {
		int moveDist, actions, bonusActions;

		moveDist = focus.getSpeed();
		actions = focus.getActions();
		bonusActions = focus.getBonusActions();
		int interactions = 1;
		boolean endingTurn = false;

		while ((moveDist > 0 || actions > 0 || bonusActions > 0) && !endingTurn && battleQueue.size() > 0) {
			AbstractAction action = focus.getNextAction(this, battleQueue, moveDist, actions, bonusActions,
					interactions);
			if (action instanceof Attack) {
				actions--;
			} else if (action instanceof PassTurn) {
				endingTurn = true;
			}

			if (action != null) {
				action.performAction();
			}

			if (action instanceof Move) {
				int dist = ((Move) action).getDistance();
				moveDist -= dist;
			}

			if (action instanceof Draw) {
				interactions--;
			}
			
			if (action instanceof Cast) {
				//figure stuff out
			}

			clearDead(battleQueue);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Character> getPeople() {
		if (combatants == null)
			combatants = new ArrayList<Character>();
		return combatants;
	}
	
	private void clearDead(Queue<Character> list) {
		for (int i = 0; i < list.size(); i++) {
			Character c = list.poll();
			if (!c.isUnconcious()) {
				list.add(c);
			}
		}
	}

	public boolean occupied(int x, int y) {
		return room.occupied(x, y);
	}
	
	public Point getSelectedPoint() {
		return fieldView.getSelectedPoint();
	}
}
