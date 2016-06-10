package robotgameredux.systems;

import robotgameredux.input.AttackCommand;

public interface BattleSystem {

	public void execute(AttackCommand command);
	
}
