package world.locations;

import Main.Client;
import Main.Merchant;
import world.Location;
import world.World;

public class MerchantShop implements Location {

	private final String[] acts = {"buy items", "sell items", "leave"};

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
			Merchant.trade(Client.hub.getPerson());
			
		} else if (acts[1].equals(act)) {
			Merchant.sell(Client.hub.getPerson());
			
		} else if (acts[2].equals(act)) {
			Client.hub.changeLocation(World.cityCenter);
		}
	}

	@Override
	public void describe() {
		Client.console.log("The shop smells deeply of mildew and decay. You can see moss growing in the corners of the building.", Client.DM_COLOR);
		Client.console.log("The owner is standing near the back of the shop, with a wide grin on his gnarled face.", Client.DM_COLOR);
		Client.console.log("This merchant has clearly seen a number of years too many, and his expression makes you hesitate briefly", Client.DM_COLOR);
		
	}

	@Override
	public void enter() {
		Client.console.log("As you enter the shop, the merchant beckons you towards him.", Client.DM_COLOR);
		Client.console.say(Merchant.NAME,"Come, weary traveller! I have plenty of goods!" , Merchant.MERCHANT_COLOR);
		for (String s : acts) {
			Client.display.addChoice(s, s);
		}
	}

	@Override
	public void exit() {
		Client.console.say(Merchant.NAME, "Come again soon! With more money next time!", Merchant.MERCHANT_COLOR);
		for (String s : acts) {
			Client.display.removeChoice(s);
		}
	}
	
	@Override
	public String toString() {
		return "the merchant's shop";
	}

}
