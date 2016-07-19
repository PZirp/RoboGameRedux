package robotgameredux.CommandsInterfaces;

import robotgameredux.core.Coordinates;
import robotgameredux.enums.Faction;
import robotgameredux.tools.UsableTool;

public interface SupportCommandInterface<T> extends Command {

	public UsableTool getActiveTool();

	public Faction getFaction();

	public Coordinates getTarget();

	public void removeUsedTool(UsableTool tool);

}
