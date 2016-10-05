package robotgameredux.systemInterfaces;

import robotgameredux.CommandsInterfaces.AttackCommandInterface;
import robotgameredux.exceptions.InsufficientEnergyException;
import robotgameredux.exceptions.InvalidTargetException;

public interface BattleSystem {

	public <T> Boolean execute(AttackCommandInterface<T> command)
			throws InvalidTargetException, InsufficientEnergyException;
}
