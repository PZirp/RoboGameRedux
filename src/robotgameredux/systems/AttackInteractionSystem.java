package robotgameredux.systems;

import robotgameredux.input.AtkInteractCommand;
import robotgameredux.input.InteractCommand;

public interface AttackInteractionSystem {

	void execute(AtkInteractCommand command);
	
}
