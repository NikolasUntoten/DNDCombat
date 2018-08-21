package charData;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
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
import charData.Stats.CreatureSize;
import charData.items.Fists;
import charData.items.Item;
import charData.items.Weapon;
import charData.magic.Spell;
import world.locations.Arena;

/* Created by Nikolas Gaub
 * 
 * Instances of this class store all data related to a character
 * Think of this class like a character sheet in code.
 */

public class Character implements Serializable {
	
	//Character must be serializable to save data.
	private static final long serialVersionUID = 1991909017162727805L;
	
	
	private Stats stats;
	private int x;
	private int y;
	private Weapon weapon;
	public Inventory inventory;
	public MagicalInventory magic;
	private boolean unconcious;
	private double initiative;
	private int copper;

	private boolean npc;

	public Character() {
		x = (int) (Math.random() * 10);
		y = (int) (Math.random() * 10);
		stats = new Stats();
		weapon = new Fists();
		unconcious = false;
		npc = true;
		inventory = new Inventory(150);
		magic = new MagicalInventory();
		copper = 0;
	}

	public void setStats(int str, int dex, int con, int intl, int wis, int cha, int speed, Stats.Class clss,
			Stats.Race race, Stats.Align align, Stats.Style style, String name) {
		stats.setBasic(str, dex, con, intl, wis, cha);
		stats.setHealth(8 + modifier(stats.con));
		stats.name = name;
		stats.race = race;
		stats.clss = clss;
		stats.alignment = align;
		stats.style = style;

		stats.AC = 10 + (int) Math.round((stats.dex - 10) / 2.0);
	}

	public AbstractAction getNextAction(Arena field, Queue<Character> others, int moveDist, int actions, int bonusActions,
			int interactions) {
		Map<Translator.Actions, Boolean> available = new HashMap<Translator.Actions, Boolean>();
		available.put(Translator.Actions.ATTACK, actions > 0);
		available.put(Translator.Actions.MOVE, moveDist > 0);
		available.put(Translator.Actions.DRAW, interactions > 0);
		available.put(Translator.Actions.CAST, magic.getSpells().length > 0); //add cast time check?

		if (npc) {
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
		} else {
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
	}
	
	public Character[] getTargets(Arena field, int max) {
		Character[] targets = new Character[max];
		List<Character> people = field.getPeople();
		for (int i = 0; i < people.size(); i++) {
			Client.console.log(i+1 + ": " + people.get(i), Client.DM_COLOR);
		}
		for (int i = 0; i < max; i++) {
			Client.console.log("Who would you like your next target to be?");
			int n = Client.console.readNum();
			targets[i] = people.get(n - 1);
		}
		return targets;
	}
	
	public Spell[] getSpells() {
		return magic.getSpells();
	}
	
	private final String[] acts = {};
	
	public String[] getActs() {
		return acts;
	}
	
	public boolean hasAct(String act) {
		for (String s : acts) {
			if (s.equalsIgnoreCase(act)) {
				return true;
			}
		}
		return false;
	}
	
	public void act(String act) {
		
	}
	
	public void learnSpell(Spell s) {
		magic.learnSpell(s);
	}

	public void draw(Weapon e) {
		if (e instanceof Fists) {
			dequip();
		} else if (inventory.contains(e)) {
			dequip();
			weapon = e;
		} else {
			Client.console.log("You must first put this item into your inventory!", Client.DM_COLOR);
		}
	}

	public void dequip() {
		if (weapon instanceof Fists) {
			// Client.console.log("No Weapon equipped!", Client.DM_COLOR);
			return;
		}
		boolean success = inventory.add(weapon);
		if (success) {
			weapon = new Fists();
		} else {
			Client.console.log("Inventory full, Cannot dequip weapon!", Client.DM_COLOR);
		}
	}

	public boolean stow(Item e) {
		boolean success = inventory.add(e);
		if (success) {
			Client.console.log(e + " has been added to your inventory.", Client.DM_COLOR);
		} else {
			Client.console.log("Inventory full, Cannot stow item!", Client.DM_COLOR);
		}
		return success;
	}

	public boolean canStow(Item e) {
		return inventory.hasSpaceFor(e.getWeight());
	}

	public void drop(Item e) {
		boolean success = inventory.remove(e);
		if (success) {
			Client.console.log("Dropped " + e + " from inventory.", Client.DM_COLOR);
		}
	}

	public ArrayList<Weapon> getWeapons() {
		ArrayList<Weapon> weapons = new ArrayList<Weapon>();
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) instanceof Weapon) {
				weapons.add((Weapon) inventory.get(i));
			}
		}
		return weapons;
	}

	public int getCopper() {
		return copper;
	}

	public void addCopper(int c) {
		if (c < 0)
			return;
		copper += c;
	}

	public void useCopper(int c) {
		if (c < 0)
			return;
		if (c > copper) {
			Client.console.log("You do not have enough money for this transaction.", Client.DM_COLOR);
		}
		copper -= c;
	}

	public boolean attackCheck(int threshold) {
		int mod = modifier(stats.str);
		boolean advantage = false;
		boolean disadvantage = false;
		if (weapon.hasProperty(Weapon.Property.FINESSE)) {
			mod = finesse();
		}
		
		if (this.creatureSize() == CreatureSize.SMALL && weapon.hasProperty(Weapon.Property.HEAVY)) {
			disadvantage = true;
		}
		
		//make the roll
		if (advantage == disadvantage) { //both true or both false
			return roll20() + mod >= threshold;
			
		} else if(advantage) { //roll with advantage
			return advantageCheck(threshold, mod);
			
		} else if (disadvantage) { //roll with disadvantage
			return disadvantageCheck(threshold, mod);
			
		} else { //this should not happen, ever. Logic.
			Client.console.log("You fucked up my boi", Client.USER_COLOR);
			return false;
		}
	}
	
	public boolean disadvantageCheck(int threshold, int mod) {
		Client.console.log("Rolling with disadvantage.", Client.COMPUTER_COLOR);
		boolean roll1 = roll20() + mod >= threshold;
		boolean roll2 = roll20() + mod >= threshold;
		return roll1 && roll2;
	}
	
	public boolean advantageCheck(int threshold, int mod) {
		Client.console.log("Rolling with advantage!", Client.COMPUTER_COLOR);
		boolean roll1 = roll20() + mod >= threshold;
		boolean roll2 = roll20() + mod >= threshold;
		return roll1 || roll2;
	}
	
	private int finesse() {
		Client.console.log("Which modifier would you like to use?", Client.DM_COLOR);
		Client.console.log("#1: Strength (" + stats.str + ")");
		Client.console.log("#2: Dexterity (" + stats.dex + ")");
		int answer = Client.console.readNum();
		if (answer == 1) {
			return modifier(stats.str);
		} else if (answer == 2) {
			return modifier(stats.dex);
		}
		return modifier(stats.str);
	}

	public boolean strCheck(int threshold) {
		return roll20() + modifier(stats.str) >= threshold;
	}

	public boolean dexCheck(int threshold) {
		return roll20() + modifier(stats.dex) >= threshold;
	}

	public boolean conCheck(int threshold) {
		return roll20() + modifier(stats.con) >= threshold;
	}

	public boolean intlCheck(int threshold) {
		return roll20() + modifier(stats.intl) >= threshold;
	}

	public boolean wisCheck(int threshold) {
		return roll20() + modifier(stats.wis) >= threshold;
	}

	public boolean chaCheck(int threshold) {
		return roll20() + modifier(stats.cha) >= threshold;
	}

	public void setWeapon(Weapon e) {
		weapon = e;
	}

	public void setNPC(boolean isNPC) {
		npc = isNPC;
	}

	public boolean isNPC() {
		return npc;
	}

	public int getActions() {
		return 2;
	}

	public int getSpeed() {
		return stats.speed;
	}

	public int getBonusActions() {
		return 0;
	}

	public void rollInitiative() {
		initiative = Math.random();
	}

	public double getInitiative() {
		return initiative;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void move(int deltaX, int deltaY) {
		x += deltaX;
		y += deltaY;
	}

	public int getAC() {
		return stats.AC;
	}

	public int getDamage() {
		return weapon.getDamage();
	}

	public void damage(int damage) {
		stats.hitpoints -= damage;
		if (stats.hitpoints <= 0) {
			Client.console.log(this.toString() + " has become unconcious.");
			unconcious = true;
		}
	}

	// <GENJI>: "I NEED HEALING"
	public void heal(int healing) {
		if (stats.hitpoints + healing > stats.getMaxHitpoints()) {
			stats.hitpoints = stats.getMaxHitpoints();
		} else {
			stats.hitpoints += healing;
			if (isUnconcious()) {
				unconcious = false;
			}
		}
	}

	public void fullHeal() {
		stats.hitpoints = stats.getMaxHitpoints();
		unconcious = false;
	}

	public int modifier(int input) {
		return (int) (input / 2) - 5;
	}

	public String toString() {
		return stats.name;
	}

	public String toShortString() {
		return "he";
	}

	public BufferedImage toImage() {
		BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		int y = 20;
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 400, 400);

		g.setColor(Color.BLACK);
		g.drawString("Name: " + stats.name, 10, y);
		y += 20;
		g.drawString("HP: " + stats.hitpoints + " / " + stats.getMaxHitpoints(), 10, y);
		y += 20;
		g.drawString("Current Weapon: " + weapon, 10, y);
		y += 20;
		g.drawString("Unconcious: " + unconcious, 10, y);
		y += 20;
		g.drawString("Copper: " + copper, 10, y);
		return image;
	}

	public boolean isUnconcious() {
		return unconcious;
	}

	private int roll20() {
		return Client.roll(20, npc);
	}

	public double getReach() {
		return weapon.reach;
	}

	public double distance(Character other) {
		int deltaX = getX() - other.getX();
		int deltaY = getY() - other.getY();
		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
	}

	public Color getColor() {
		if (npc)
			return Color.DARK_GRAY;
		return Color.BLUE;
	}
	
	public CreatureSize creatureSize() {
		return null;
	}
}
