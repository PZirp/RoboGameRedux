package robotgameredux.actors;

import robotgameredux.core.Vector2;
import robotgameredux.input.Command;
import robotgameredux.input.RobotStates;

public class MoveCommand implements Command {

	public MoveCommand() {
	}
	
	public void setInput(Vector2 input) {
		this.destination = input;
	}
	
	@Override
	public void execute(Robot robot) {
		robot.setDest(destination);
		robot.setState(RobotStates.MOVING);
	}

	private Vector2 destination;
	
}
