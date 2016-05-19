package robotgameredux.input;

import robotgameredux.actors.Robot;

public interface Command {

	public void execute(Robot robot);	
}
