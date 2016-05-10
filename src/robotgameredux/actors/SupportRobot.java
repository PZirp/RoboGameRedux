package robotgameredux.actors;

import java.util.ArrayList;

import robotgameredux.core.Vector2;
import robotgameredux.tools.UsableTool;
import robotgameredux.weapons.Weapon;

public class SupportRobot extends Robot{

	public SupportRobot(AttackRobotController reference, Vector2 coords) {
		super(reference, coords);
		//this.reference = reference;
		this.tools = new ArrayList<UsableTool>();		
	}
	
	private ArrayList<UsableTool> tools;
	private SupportRobotController reference;
	
}
