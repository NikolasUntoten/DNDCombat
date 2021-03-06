package charData;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;

import Main.Client;
import actions.AbstractAction;
import charData.CharacterData.CreatureSize;
import charData.items.Fists;
import charData.items.Item;
import charData.items.Weapon;
import charData.magic.Spell;
import world.locations.Arena;

/* Created by Nikolas Gaub
 * This class is meant to act as a bridge between the character sheet and the world.
 * The functions contained in this class have to main purposes: storing and using the
 * character data, and communicating with the user about
 */

public abstract class Character implements Serializable {
	
	private static final long serialVersionUID = 1991909017162727805L;
	
	public CharacterData data;
	public int x;
	public int y;

	public Character() {
		x = (int) (Math.random() * 10);
		y = (int) (Math.random() * 10);
		data = new CharacterData();
	}
	
	public Character(CharacterData initData) {
		data = initData;
		
		x = (int) (Math.random() * 10);
		y = (int) (Math.random() * 10);
		
	}
	
	public abstract AbstractAction getNextAction(Arena field, Queue<Character> others, int moveDist, int actions,
			int bonusActions, int interactions);
	
	public abstract Character[] getTargets(Arena field, int max);

	public void setStats(int str, int dex, int con, int intl, int wis, int cha, int speed, CharacterData.Class clss,
			CharacterData.Race race, CharacterData.Align align, CharacterData.Style style, String name) {
		data.setBasic(str, dex, con, intl, wis, cha);
		data.setHealth(8 + modifier(data.con));
		data.name = name;
		data.race = race;
		data.clss = clss;
		data.alignment = align;
		data.style = style;

		data.AC = 10 + (int) Math.round((data.dex - 10) / 2.0);
	}
	
	public void setStats(CharacterData initData) {
		data = initData;
	}
	
	public Spell[] getSpells() {
		return data.magic.getSpells();
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
		data.magic.learnSpell(s);
	}

	public void draw(Weapon e) {
		if (e instanceof Fists) {
			dequip();
		} else if (data.inventory.contains(e)) {
			dequip();
			data.drawnWeapon = e;
		} else {
			Client.console.log("You must first put this item into your inventory!", Client.DM_COLOR);
		}
	}

	public void dequip() {
		if (data.drawnWeapon instanceof Fists) {
			// Client.console.log("No Weapon equipped!", Client.DM_COLOR);
			return;
		}
		boolean success = data.inventory.add(data.drawnWeapon);
		
		if (success) {
			data.drawnWeapon = new Fists();
		} else {
			Client.console.log("Inventory full, Cannot dequip weapon!", Client.DM_COLOR);
		}
	}

	public boolean stow(Item e) {
		boolean success = data.inventory.add(e);
		if (success) {
			Client.console.log(e + " has been added to your inventory.", Client.DM_COLOR);
		} else {
			Client.console.log("Inventory full, Cannot stow item!", Client.DM_COLOR);
		}
		return success;
	}

	public boolean canStow(Item e) {
		return data.inventory.hasSpaceFor(e.getWeight());
	}

	public void drop(Item e) {
		boolean success = data.inventory.remove(e);
		if (success) {
			Client.console.log("Dropped " + e + " from inventory.", Client.DM_COLOR);
		}
	}

	public ArrayList<Weapon> getWeapons() {
		ArrayList<Weapon> weapons = new ArrayList<Weapon>();
		for (int i = 0; i < data.inventory.size(); i++) {
			if (data.inventory.get(i) instanceof Weapon) {
				weapons.add((Weapon) data.inventory.get(i));
			}
		}
		return weapons;
	}

	public int getCopper() {
		return data.copper;
	}

	public void addCopper(int c) {
		if (c < 0)
			return;
		data.copper += c;
	}

	public boolean useCopper(int c) {
		if (c < 0)
			return false;
		if (c > data.copper) {
			Client.console.log("You do not have enough money for this transaction.", Client.DM_COLOR);
			return false;
		}
		data.copper -= c;
		return true;
	}

	public boolean attackCheck(int threshold) {
		int mod = modifier(data.str);
		boolean advantage = false;
		boolean disadvantage = false;
		if (data.drawnWeapon.hasProperty(Weapon.Property.FINESSE)) {
			mod = finesse();
		}
		
		if (this.creatureSize() == CreatureSize.SMALL && data.drawnWeapon.hasProperty(Weapon.Property.HEAVY)) {
			disadvantage = true;
		}
		
		//make the roll
		if (advantage == disadvantage) { //both true or both false
			return Client.roll(20) + mod >= threshold;
			
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
		boolean roll1 = Client.roll(20) + mod >= threshold;
		boolean roll2 = Client.roll(20) + mod >= threshold;
		return roll1 && roll2;
	}
	
	public boolean advantageCheck(int threshold, int mod) {
		Client.console.log("Rolling with advantage!", Client.COMPUTER_COLOR);
		boolean roll1 = Client.roll(20) + mod >= threshold;
		boolean roll2 = Client.roll(20) + mod >= threshold;
		return roll1 || roll2;
	}
	
	private int finesse() {
		Client.console.log("Which modifier would you like to use?", Client.DM_COLOR);
		Client.console.log("#1: Strength (" + data.str + ")");
		Client.console.log("#2: Dexterity (" + data.dex + ")");
		int answer = Client.console.readNum();
		if (answer == 1) {
			return modifier(data.str);
		} else if (answer == 2) {
			return modifier(data.dex);
		}
		return modifier(data.str);
	}

	public boolean strCheck(int threshold) {
		return Client.roll(20) + modifier(data.str) >= threshold;
	}

	public boolean dexCheck(int threshold) {
		return Client.roll(20) + modifier(data.dex) >= threshold;
	}

	public boolean conCheck(int threshold) {
		return Client.roll(20) + modifier(data.con) >= threshold;
	}

	public boolean intlCheck(int threshold) {
		return Client.roll(20) + modifier(data.intl) >= threshold;
	}

	public boolean wisCheck(int threshold) {
		return Client.roll(20) + modifier(data.wis) >= threshold;
	}

	public boolean chaCheck(int threshold) {
		return Client.roll(20) + modifier(data.cha) >= threshold;
	}

	public void setWeapon(Weapon e) {
		data.drawnWeapon = e;
	}

	public int getActions() {
		return 2;
	}

	public int getSpeed() {
		return data.speed;
	}

	public int getBonusActions() {
		return 0;
	}

	public void rollInitiative() {
		data.initiative = (int) (Math.random()*20);
	}

	public double getInitiative() {
		return data.initiative;
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
		return data.AC;
	}

	public int getDamage() {
		return data.drawnWeapon.getDamage();
	}

	public void damage(int damage) {
		data.hitpoints -= damage;
		if (data.hitpoints <= 0) {
			Client.console.log(this.toString() + " has become unconcious.");
			data.unconcious = true;
		}
	}

	// <GENJI>: "I NEED HEALING"
	public void heal(int healing) {
		if (data.hitpoints + healing > data.getMaxHitpoints()) {
			data.hitpoints = data.getMaxHitpoints();
		} else {
			data.hitpoints += healing;
			if (isUnconcious()) {
				data.unconcious = false;
			}
		}
	}

	public void fullHeal() {
		data.hitpoints = data.getMaxHitpoints();
		data.unconcious = false;
	}

	public int modifier(int input) {
		return (int) (input / 2) - 5;
	}

	public String toString() {
		return data.name;
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
		g.drawString("Name: " + data.name, 10, y);
		y += 20;
		g.drawString("HP: " + data.hitpoints + " / " + data.getMaxHitpoints(), 10, y);
		y += 20;
		g.drawString("Current Weapon: " + data.drawnWeapon, 10, y);
		y += 20;
		g.drawString("Unconcious: " + data.unconcious, 10, y);
		y += 20;
		g.drawString("Copper: " + data.copper, 10, y);
		return image;
	}

	public boolean isUnconcious() {
		return data.unconcious;
	}

	public double getReach() {
		return data.drawnWeapon.reach;
	}

	public double distance(Character other) {
		int deltaX = getX() - other.getX();
		int deltaY = getY() - other.getY();
		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
	}

	public Color getColor() {
		if (this instanceof NPC)
			return Color.DARK_GRAY;
		return Color.BLUE;
	}
	
	public CreatureSize creatureSize() {
		return null;
	}
}
