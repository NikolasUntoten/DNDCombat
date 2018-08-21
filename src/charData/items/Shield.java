package charData.items;

public class Shield extends Item {
	
	public int ACBonus;
	
	public Shield(String name, int initValue, double initWeight, int ACBonus) {
		super(name, initValue, initWeight);
		
		this.ACBonus = ACBonus;
	}
}
