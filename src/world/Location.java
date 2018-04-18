package world;

import java.io.Serializable;

public interface Location extends Serializable {
	
	public String[] getActs();
	public boolean hasAct(String act);
	public void act(String act);
	public void describe();
	public void enter();
	public void exit();
}
