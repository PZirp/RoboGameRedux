package robotgameredux.CommandsInterfaces;

import robotgameredux.actors.Faction;
import robotgameredux.core.Coordinates;
import robotgameredux.tools.UsableTool;

public interface SupportCommandInterface<T> extends Command {

	
	public UsableTool getActiveTool();
	public Faction getFaction();
	public Coordinates getTarget();
	public void removeUsedTool(UsableTool tool);
	
}
