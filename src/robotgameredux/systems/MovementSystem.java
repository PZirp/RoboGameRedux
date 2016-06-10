package robotgameredux.systems;

import robotgameredux.actors.Robot;
import robotgameredux.input.MovementCommand;

public interface MovementSystem {
	void execute(MovementCommand command);
}
