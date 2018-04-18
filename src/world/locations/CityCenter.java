package world.locations;

import Main.Client;
import world.Location;
import world.World;

public class CityCenter implements Location {
	
	private final String[] acts = {"go to the merchant", "go to the blacksmith", "go to the arena"};

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
		// TODO Auto-generated method stub
		if (acts[0].equals(act)) {
			Client.hub.changeLocation(World.merchantShop);
		} else if (acts[1].equals(act)) {
			
		} else if (acts[2].equals(act)) {
			Client.hub.changeLocation(World.arena);
		}
	}

	@Override
	public void describe() {
		// TODO Auto-generated method stub
		Client.console.log("Around you are various small buildings, made of sandstone and clay.", Client.DM_COLOR);
		Client.console.log("There seem to be multiple shops and services, including a blacksmith and a merchant.", Client.DM_COLOR);
		Client.console.log("Although the area is crowded, the people seem to eye you with caution, giving you a wide birth.", Client.DM_COLOR);
		Client.console.log("You can see to one side of the buildings is the massive gladiator arena.", Client.DM_COLOR);
	}

	@Override
	public void enter() {
		Client.console.log("You enter the city center, a place of commerce for the people of this city.", Client.DM_COLOR);
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
	
	@Override
	public String toString() {
		return "the City Center";
	}
	
}
