package charData.items;

public class Armor extends Item {

	public int ACBonus;
	
	public Armor(String name, int initValue, double initWeight, int ACBonus) {
		super(name, initValue, initWeight);
		
		this.ACBonus = ACBonus;
	}
}
