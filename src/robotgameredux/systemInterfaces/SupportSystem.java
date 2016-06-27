package robotgameredux.systemInterfaces;

import Exceptions.InvalidTargetException;
import robotgameredux.Commands.SupportCommand;

public interface SupportSystem {

	Boolean execute(SupportCommand command) throws InvalidTargetException;
//Use object	
}
