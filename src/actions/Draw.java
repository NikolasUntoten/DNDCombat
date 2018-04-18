package actions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import charData.Character;
import charData.items.Weapon;
import Main.Client;

public class Draw implements AbstractAction {
	
	private Character focus;
	
	public Draw(Character c) {
		focus = c;
	}

	@Override
	public void performAction() {
		ArrayList<Weapon> weapons = focus.getWeapons();
		
		Client.console.log("What weapon would you like to draw? (input 0 for no weapon)", Client.DM_COLOR);
		Client.console.log("Available Weapons: ", Client.COMPUTER_COLOR);
		for (int i = 0; i < weapons.size(); i++) {
			Client.console.log("#" + (i+1) + ": " + weapons.get(i), Client.COMPUTER_COLOR);
		}
		
		int index = Client.console.readNum() - 1;
		if (index == -1) {
			return;
		} else if(index < weapons.size()) {
			Weapon e = weapons.get(index);
			focus.draw(e);
		} else {
			Client.console.log("Number is not on list, try again.", Client.COMPUTER_COLOR);
			performAction();
		}
	}
	
	public String toString() {
		return "draw";
	}
}
