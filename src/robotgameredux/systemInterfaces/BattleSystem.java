package robotgameredux.systemInterfaces;

import Exceptions.InvalidTargetException;
import robotgameredux.Commands.AttackCommand;

public interface BattleSystem {

	public Boolean execute(AttackCommand command) throws InvalidTargetException;
	
}
