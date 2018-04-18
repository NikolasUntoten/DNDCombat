package actions;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import Main.Client;
import charData.Character;

public class Attack implements AbstractAction {
	
	private Character perp;
	private Character target;
	
	public Attack(Character perp, Character target) {
		this.perp = perp;
		this.target = target;
	}
	
	@Override
	public void performAction() {
		if (perp.distance(target) > perp.getReach()) {
			Client.console.log(perp.toString() + " tries to hit " + target.toString() 
							   	+ ", but " + target.toShortString() + " is out of reach!");
			return;
		}
		
		if (perp.strCheck(target.getAC())) {
			int damage = perp.getDamage();
			target.damage(damage);
			Client.console.log(perp.toString() + " hit " + target.toString() + " for " + damage + " hitpoints!");
		} else {
			Client.console.log(perp.toString() + " missed the attack.");
		}
	}
	
	public String toString() {
		return "attack";
	}
}
