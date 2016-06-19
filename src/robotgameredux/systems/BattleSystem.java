package robotgameredux.systems;

import Exceptions.InvalidTargetException;
import robotgameredux.input.AttackCommand;

public interface BattleSystem {

	public void execute(AttackCommand command) throws InvalidTargetException;
	
}
