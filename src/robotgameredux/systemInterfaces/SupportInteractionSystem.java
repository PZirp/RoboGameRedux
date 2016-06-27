package robotgameredux.systemInterfaces;

import Exceptions.InvalidTargetException;
import robotgameredux.Commands.SupInteractCommand;

public interface SupportInteractionSystem {

	Boolean execute(SupInteractCommand command) throws InvalidTargetException;
	//Take object from station, move obstacle, recharge
}
