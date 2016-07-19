package robotgameredux.tools;

import java.io.Serializable;

import robotgameredux.TargetInterfaces.TargetInterface;

public class EnergyPack implements UsableTool, Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7389530257121662893L;

	public EnergyPack() {
		this.energy = 50;
		this.cost = 5;
	}

	@Override
	public <T> void use(TargetInterface<T> robot) {
		robot.addEnergy(energy);
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
		return getClass().getName() + "[Name: " + name + " Energy: " + energy + " Cost: " + cost + "]";
	}

	@Override
	public EnergyPack clone() {
		try {
			EnergyPack clone = (EnergyPack) super.clone();
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
		EnergyPack other = (EnergyPack) otherObject;
		return name.equals(other.name) && energy == other.energy && cost == other.cost;
	}

	private String name = "Energy Pack";
	private int energy;
	private int cost;

}
