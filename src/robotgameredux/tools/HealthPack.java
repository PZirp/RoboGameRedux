package robotgameredux.tools;

import robotgameredux.actors.Robot;

public class HealthPack implements UsableTool {

	public HealthPack() {
		this.health = 50;
	};
	
	@Override
	public void use(Robot robot) {
		robot.updateHealth(this.health);

	}
	
	public String getName() {
		return name;
	}
	
	private final String name = "Health pack";
	private final int health;
	
	

}
