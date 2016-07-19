package robotgameredux.systemInterfaces;

import robotgameredux.CommandsInterfaces.AtkInteractCommandInterface;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;

public interface AttackInteractionSystem {

	<T> Boolean execute(AtkInteractCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException;
}
