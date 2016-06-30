package robotgameredux.tools;

import robotgameredux.actors.Robot;

public interface UsableTool {

	public void use(Robot robot);
	public String getName();
	public int getCost();
}
