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
import Main.Encounter;
import actions.AbstractAction;
import actions.Attack;
import actions.Cast;
import actions.Draw;
import actions.Move;
import actions.PassTurn;
import charData.Character;
import charData.NPC;
import charData.items.Weapon;
import world.Location;
import world.World;
import world.roomStuffs.Room;
import world.roomStuffs.Wall;

public class Arena implements Location {
	
	private final String[] acts = { "enter battle", "leave", "spectate" };

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

		List<Character> list = new ArrayList<Character>();
		list.add(Client.hub.getPerson());
		list.add(guy);
		Encounter enc = new Encounter(generateRoom(list), list);
		enc.run();

		afterMath(Client.hub.getPerson(), guy);

		Client.hub.changeLocation(new CityCenter());
	}

	private Room generateRoom(List<Character> list) {
		Room r = new Room(10, 10);
		for (int i = 0; i < r.getWidth(); i++) {
			r.addProp(new Wall(), i, 0);
			r.addProp(new Wall(), i, r.getHeight() - 1);
		}
		
		for (int i = 1; i < r.getHeight() - 1; i++) {
			r.addProp(new Wall(), 0, i);
			r.addProp(new Wall(), r.getWidth() - 1, i);
		}
		for (Character c : list) {
			r.addPerson(c);
		}
		return r;
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

	@Override
	public String toString() {
		return "the Arena";
	}
}
