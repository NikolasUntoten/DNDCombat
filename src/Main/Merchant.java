package Main;

import charData.Inventory;
import charData.items.Item;
import charData.items.Weapon;

import java.awt.Color;
import java.io.FileNotFoundException;

import charData.Character;

public class Merchant {
	
	public static final Color MERCHANT_COLOR = new Color(255, 110, 0);
	public static final String NAME = "Merchant";
	
	private static Inventory inventory;
	
	public static void trade(Character c) {
		Client.console.say(NAME, "What would you like to purchase?", MERCHANT_COLOR);
		inventory = new Inventory(700);
		Weapon[] weapons = null;
		try {
			weapons = Weapon.getWeapons();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (Weapon w : weapons) {
			inventory.add(w);
		}
		purchaseItem(c, true);
	}
	
	public static void sell(Character c) {
		sellItem(c);
	}
	
	private static void sellItem(Character c) {
		
	}
	
	private static void purchaseItem(Character c, boolean showItems) {
		
		if (showItems) showItems();
		
		int index = Client.console.readNum() - 1;
		if (index == -1) {
			return;
		}
		if (index < 0 || index > inventory.size()) {
			Client.console.log("Number is not on the list, try again.", Client.COMPUTER_COLOR);
			purchaseItem(c, false);
			return;
		}
		
		Item e = inventory.get(index);
		if (!c.canStow(e)) {
			Client.console.log("You have no room in your bag for this item...", Client.DM_COLOR);
		}
		if (c.getCopper() >= e.getValue()) {
			c.useCopper(e.getValue());
			inventory.remove(index);
			c.stow(e);
			
			Client.console.say(NAME, "Here you are. Would you like to buy anything else?", MERCHANT_COLOR);
		} else {
			Client.console.say(NAME, "We are not a charity. You need more coin to purchase this.", MERCHANT_COLOR);
			Client.console.say(NAME, "Would you like to buy anything else? Something in your price range?", MERCHANT_COLOR);
		}
		
		if (Client.console.readBoolean()) {
			purchaseItem(c, true);
		}
		
		Client.console.say(NAME, "Alright, I wish you luck on your journey.", MERCHANT_COLOR);
	}

	private static void showItems() {
		Client.console.log("#0: None", Client.COMPUTER_COLOR);
		for (int i = 0; i < inventory.size(); i++) {
			Item item = inventory.get(i);
			Client.console.log("#" + (i+1) + ": " + item + " (" + item.getValue() + "c)", Client.COMPUTER_COLOR);
		}
	}
}
