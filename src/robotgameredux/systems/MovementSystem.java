package robotgameredux.systems;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.actors.Robot;
import robotgameredux.input.MovementCommand;

public interface MovementSystem {
	void execute(MovementCommand command) throws InvalidTargetException, InsufficientEnergyException;
}
