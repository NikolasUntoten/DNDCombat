package world.roomStuffs;

import charData.Character;

public class Token extends Prop {
	
	public Character tokenChar;
	
	public Token(Character person) {
		this.color = person.getColor();
		this.name = person.toString();
		tokenChar = person;
	}
}
