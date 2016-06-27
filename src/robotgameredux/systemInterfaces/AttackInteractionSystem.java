package robotgameredux.systemInterfaces;

import Exceptions.InvalidTargetException;
import robotgameredux.Commands.AtkInteractCommand;

public interface AttackInteractionSystem {

	Boolean execute(AtkInteractCommand command) throws InvalidTargetException;
	
}
