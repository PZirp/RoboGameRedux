package robotgameredux.tools;

import java.io.Serializable;

import robotgameredux.TargetInterfaces.TargetInterface;

public class EnergyPack implements UsableTool, Serializable {

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


	private String name = "Energy Pack";
	private int energy;
	private int cost;
	
}
