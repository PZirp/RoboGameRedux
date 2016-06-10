package robotgameredux.systems;

import robotgameredux.input.Command;

public interface GameSystem {
	
	public <T extends Command> void execute(T command);
}
