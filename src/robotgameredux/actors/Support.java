package robotgameredux.actors;

import robotgameredux.core.Vector2;
import robotgameredux.input.Command;
import robotgameredux.input.RobotStates;
import robotgameredux.systems.GameSystem;
import robotgameredux.systems.StandardSupportSystem;
import robotgameredux.systems.SupportSystem;
import robotgameredux.tools.UsableTool;

public interface Support {

	public UsableTool getActiveTool(Integer i);
	public Vector2 getCoords();
	public SupportSystem getSupportSystem();
	public void setCommand(Command c);
	public void setState(RobotStates state);

}
