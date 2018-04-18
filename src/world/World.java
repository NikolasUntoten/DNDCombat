package world;

import java.io.IOException;

import Main.Client;
import world.locations.Arena;
import world.locations.CityCenter;
import world.locations.MerchantShop;
import charData.Character;

public class World {
	
	public static Location arena;
	public static Location cityCenter;
	public static Location merchantShop;
	
	
	public static void initialize() {
		arena = new Arena();
		cityCenter = new CityCenter();
		merchantShop = new MerchantShop();
	}
	
	public static boolean load(String saveName) {
		try {
			arena = (Location) Client.loadObject(saveName, "Arena");
			cityCenter = (Location) Client.loadObject(saveName, "CityCenter");
			merchantShop = (Location) Client.loadObject(saveName, "MerchantShop");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		if (arena == null) arena = new Arena();
		if (cityCenter == null) cityCenter = new CityCenter();
		if (merchantShop == null) merchantShop = new MerchantShop();
		return true;
	}
	
	public static boolean save(String saveName) {
		try {
			Client.saveObject(arena, saveName, "Arena");
			Client.saveObject(cityCenter, saveName, "CityCenter");
			Client.saveObject(merchantShop, saveName, "MerchantShop");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
