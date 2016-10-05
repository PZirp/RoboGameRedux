package robotgameredux.tools;

import java.io.Serializable;

import robotgameredux.TargetInterfaces.TargetInterface;

public class HealthPack implements UsableTool, Serializable, Cloneable {

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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[Name = " + name + " Health = " + health + " Cost: " + cost + "]";
	}

	@Override
	public HealthPack clone() {
		try {
			HealthPack clone = (HealthPack) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;
		if (getClass() != otherObject.getClass())
			return false;
		HealthPack other = (HealthPack) otherObject;
		return name.equals(other.name) && health == other.health && cost == other.cost;
	}

	private final String name = "Health pack";
	private final int health;
	private final int cost = 5;

}
