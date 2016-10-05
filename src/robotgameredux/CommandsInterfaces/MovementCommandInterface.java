package robotgameredux.CommandsInterfaces;

import robotgameredux.core.Coordinates;

public interface MovementCommandInterface<T> extends Command {

	Coordinates getDestination();

	public Coordinates getCoords();

	void setCoords(Coordinates coords);

	int getRange();

	void resetActor();

}
