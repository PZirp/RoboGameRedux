package robotgameredux.actors;

import robotgameredux.Commands.Command;
import robotgameredux.core.Vector2;
import robotgameredux.input.RobotStates;
import robotgameredux.systemInterfaces.SupportSystem;
import robotgameredux.systemsImplementations.StandardSupportSystem;
import robotgameredux.tools.UsableTool;

public interface Support {

	public UsableTool getActiveTool(Integer i);
	public Vector2 getCoords();
	public SupportSystem getSupportSystem();
	public void setCommand(Command c);
	public void setState(RobotStates state);
	public Faction getFaction();
}
