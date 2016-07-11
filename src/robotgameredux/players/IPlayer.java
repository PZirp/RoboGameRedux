package robotgameredux.players;

public interface IPlayer {

	void setActive(Boolean active);
	Boolean isActve();
	void resetMoved();
	Boolean hasMoved();
}
