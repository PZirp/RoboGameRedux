package robotgameredux.tools;

import java.io.Serializable;

import robotgameredux.TargetInterfaces.TargetInterface;
import robotgameredux.actors.Robot;

public class HealthPack implements UsableTool, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8631385135609367633L;
	public HealthPack() {
		this.health = 50;
	};
	
	@Override
	public <T> void use(TargetInterface<T> robot) {
		robot.heal(this.health);

	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getClass().getName() + "[Name = " + name + " Health = " + health + "]";
	}
	
	public int getCost() {
		return cost;
	}
	
	private final String name = "Health pack";
	private final int health;
	private final int cost = 5;
	

}
