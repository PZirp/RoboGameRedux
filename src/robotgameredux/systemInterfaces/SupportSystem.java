package robotgameredux.systemInterfaces;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.CommandsInterfaces.SupportCommandInterface;

public interface SupportSystem {

	<T> Boolean execute(SupportCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException;
//Use object	
}
