package robotgameredux.systems;

import robotgameredux.input.SupInteractCommand;

public interface SupportInteractionSystem {

	void execute(SupInteractCommand command);
	//Take object from station, move obstacle, recharge
}
