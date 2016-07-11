package robotgameredux.systemInterfaces;

import Exceptions.InsufficientEnergyException;
import Exceptions.InvalidTargetException;
import robotgameredux.Commands.RobotAttackCommand;
import robotgameredux.CommandsInterfaces.AttackCommandInterface;

public interface BattleSystem {

	public <T> Boolean execute(AttackCommandInterface<T> command) throws InvalidTargetException, InsufficientEnergyException;
	
}
