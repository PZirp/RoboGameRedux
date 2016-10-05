package robotgameredux.systemInterfaces;

import robotgameredux.CommandsInterfaces.SupportCommandInterface;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;

public interface SupportSystem {

	<T> Boolean execute(SupportCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException;
	// Use object
}
