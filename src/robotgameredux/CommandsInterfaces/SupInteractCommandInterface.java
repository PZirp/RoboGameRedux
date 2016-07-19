package robotgameredux.CommandsInterfaces;

import robotgameredux.core.Coordinates;
import robotgameredux.enums.RobotStates;
import robotgameredux.tools.UsableTool;

public interface SupInteractCommandInterface<T> extends Command {

	public Coordinates getTarget();

	public RobotStates getState();

	public Coordinates getCoords();

	public int getStrenght();

	public void addEnergy(Integer charge);

	public void addTool(UsableTool t);

}
