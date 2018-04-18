package world.locations;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import GUI.BattleFieldViewer;
import Main.CharacterCreator;
import Main.Client;
import actions.AbstractAction;
import actions.Attack;
import actions.Cast;
import actions.Draw;
import actions.Move;
import actions.PassTurn;
import charData.Character;
import charData.items.Weapon;
import world.Location;
import world.World;
import world.roomStuffs.Room;
import world.roomStuffs.Wall;

public class Arena implements Location {
	
	private final String[] acts = { "enter battle", "leave", "spectate" };

	private Queue<Character> battleQueue;
	private List<Character> list;
	private BattleFieldViewer fieldView;
	private Room field;

	@Override
	public String[] getActs() {
		return acts;
	}

	@Override
	public boolean hasAct(String act) {
		for (String s : acts) {
			if (s.equalsIgnoreCase(act)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void act(String act) {
		if (acts[0].equals(act)) {
			battle();
		} else if (acts[1].equals(act)) {
			Client.hub.changeLocation(World.cityCenter);
		} else if (acts[2].equals(act)) {
			Client.console.log("You try to enter the viewing area, but a gaurd steps in your path.", Client.DM_COLOR);
			Client.console.log("It is clear that you are not allowed to spectate this event yet.", Client.DM_COLOR);
		}
	}

	@Override
	public void describe() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enter() {
		Client.console.log("You see in front of you a large steel gate, clearly for keeping fighters in the arena.", Client.DM_COLOR);
		Client.console.log("To your left is a staircase that leads upwards, with a gaurd standing close by.", Client.DM_COLOR);
		for (String s : acts) {
			Client.display.addChoice(s, s);
		}
	}

	@Override
	public void exit() {
		for (String s : acts) {
			Client.display.removeChoice(s);
		}
	}

	private void battle() {
		for (String s : acts) {
			Client.display.removeChoice(s);
		}
		Character guy = CharacterCreator.defaultChar(true);

		Client.console.log("You are now battling " + guy.toString(), Client.DM_COLOR);
		Client.console.log("Input 'attack' to attack, anything else to quit.", Client.COMPUTER_COLOR);

		list = new ArrayList<Character>();
		list.add(Client.hub.getPerson());
		list.add(guy);
		runBattle(list);

		afterMath(Client.hub.getPerson(), guy);

		Client.hub.changeLocation(new CityCenter());
	}

	public void runBattle(List<Character> list) {
		fieldView = new BattleFieldViewer(generateRoom(list), 200);
		Client.display.addUtility(fieldView);

		// sort the list based on initiative
		if (list.size() > 1) {
			list.sort(new Comparator<Character>() {
				@Override
				public int compare(Character c1, Character c2) {
					return (int) Math.ceil(c1.getInitiative() - c2.getInitiative());
				}
			});
		}

		battleQueue = new LinkedList<Character>();
		for (Character c : list) {
			battleQueue.add(c);
		}
		while (battleQueue.size() > 1) {
			Character current = battleQueue.poll();
			Client.console.log("Beginning turn for: " + current + " (NPC:" + current.isNPC() + ").", Client.DM_COLOR);
			takeTurn(current);
			battleQueue.add(current);
		}

		Client.console.log(battleQueue.poll() + " has won the battle!", Client.DM_COLOR);
		Client.display.remove(fieldView);
	}

	private Room generateRoom(List<Character> list) {
		field = new Room(10, 10);
		for (int i = 0; i < field.getWidth(); i++) {
			field.addProp(new Wall(), i, 0);
			field.addProp(new Wall(), i, field.getHeight() - 1);
		}
		
		for (int i = 1; i < field.getHeight() - 1; i++) {
			field.addProp(new Wall(), 0, i);
			field.addProp(new Wall(), field.getWidth() - 1, i);
		}
		for (Character c : list) {
			field.addPerson(c);
		}
		return field;
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
	
	private void afterMath(Character you, Character guy) {
		if (you.isUnconcious()) {
			int r = Client.roll(20, false);
			if (r >= 19) {
				Client.console.log("Congrats, they didn't like how you fought, so they let you die.", Client.DM_COLOR);
				Client.console.log("Time to begin again with a new hero.", Client.DM_COLOR);
				you = CharacterCreator.create();
			} else {
				Client.console.log("So you lost. That just means you lose some gold and a weapon.", Client.DM_COLOR);
				spoils(guy, you);
			}
		} else {
			Client.console.log("Now that you have won, you will be awarded spoils from your victim!", Client.DM_COLOR);
			spoils(you, guy);
		}
		you.fullHeal();
		guy.fullHeal();
	}

	public void spoils(Character victor, Character failure) {

		double percent = Math.random();
		int cost = (int) (failure.getCopper() * percent);
		failure.useCopper(cost);
		victor.addCopper(cost);
		Client.console.log(victor + " has taken " + cost + " copper from " + failure, Client.DM_COLOR);

		failure.dequip();
		ArrayList<Weapon> weapons = failure.getWeapons();
		if (weapons.size() > 0) {
			int i = (int) (Math.random() * weapons.size());
			Weapon weapon = weapons.get(i);
			Client.console.log(victor + " has taken your " + failure + "'s" + weapon, Client.DM_COLOR);
			failure.drop(weapon);
			victor.stow(weapon);
		}
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
		return field.occupied(x, y);
	}

	public List<Character> getPeople() {
		if (list == null)
			list = new ArrayList<Character>();
		return list;
	}
	
	public Point getSelectedPoint() {
		return fieldView.getSelectedPoint();
	}

	@Override
	public String toString() {
		return "the Arena";
	}

}
